package com.oppo.moeslimbuddy.data

import com.oppo.moeslimbuddy.data.source.local.HttpHeaderLocalSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

abstract class NetworkBoundGetResource<ResultType, RequestType>(
    private val localDataSource: HttpHeaderLocalSource,
) {

    protected fun shouldSave() = false
    protected abstract fun getCached(): Flow<ResultType>
    protected abstract fun shouldFetch(data: ResultType?): Boolean
    protected abstract suspend fun createCall(): Result<RequestType>
    protected abstract suspend fun saveCallResult(data: RequestType)

    private val result: Flow<Result<ResultType>> = flow {
        emit(Result.Loading())
        val dbSource = getCached().first()
        if (shouldFetch(dbSource)) {
            val response = createCall()
            when (response.status) {
                Result.Status.SUCCESS -> {
                    if (isValidData(response)) {
                        response.data?.let { saveCallResult(it) }
                        emitAll(
                            getCached().map {
                                Result.Success(it)
                            }
                        )
                    } else {
                        emitAll(
                            getCached().map {
                                Result.Error(
                                    response.code,
                                    response.message,
                                    it
                                )
                            }
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
                    emitAll(
                        getCached().map {
                            Result.Error(
                                response.code,
                                response.message,
                                it
                            )
                        }
                    )
                }
            }
        } else {
            emitAll(
                getCached().map {
                    Result.Success(it)
                }
            )
        }
    }

    open fun isValidData(response: Result<RequestType>) = response.data != null
    fun asFlow(): Flow<Result<ResultType>> = result
}