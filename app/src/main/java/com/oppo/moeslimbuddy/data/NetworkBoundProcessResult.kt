package com.oppo.moeslimbuddy.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

abstract class NetworkBoundProcessResource<ResultType, RequestType> {

    private val result: Flow<Result<ResultType>> = flow {
        emit(Result.Loading())

        val response = createCall()
        when (response.status) {
            Result.Status.SUCCESS -> {
                if (isValidData(response)) {
                    val responseData = callBackResult(response.data!!)
                    emit(Result.Success(responseData))
                } else {
                    emit(
                        Result.Error(
                            response.code,
                            response.message,
                            onResponseError(response.data)
                        )
                    )
                }
            }

            Result.Status.UNAUTHORIZED -> {
                emit(Result.Unauthorized<ResultType>(response.message))
            }

            else -> {
                emit(
                    Result.Error(
                        response.code,
                        response.message,
                        onResponseError(response.data)
                    )
                )
            }
        }
    }

    open fun isValidData(response: Result<RequestType>) = response.data != null

    protected open suspend fun onResponseError(data: Any?): ResultType? {
        return null
    }

    protected abstract suspend fun createCall(): Result<RequestType>

    protected abstract suspend fun callBackResult(data: RequestType): ResultType

    fun asFlow(): Flow<Result<ResultType>> = result
}