package com.oppo.moeslimbuddy.data

import com.oppo.moeslimbuddy.data.source.local.HttpHeaderLocalSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

abstract class NetworkBoundProcessResource<ResultType, RequestType>(
    private val localDataSource: HttpHeaderLocalSource,
) {

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
                val cachedHeaders = localDataSource.getCached()
                val refreshToken = cachedHeaders?.get("refresh-token")
                localDataSource.setBearerToken(refreshToken)
                if (refreshToken != null) {
                    localDataSource.logout()
                    emit(Result.Unauthorized<ResultType>(response.message))
                } else {
                    localDataSource.logout()
                    emit(Result.Unauthorized<ResultType>(response.message))
                }
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