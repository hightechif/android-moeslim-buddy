package com.oppo.moeslimbuddy.data.source.remote

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.MalformedJsonException
import com.oppo.moeslimbuddy.constant.ErrorMessage
import com.oppo.moeslimbuddy.data.Result
import com.oppo.moeslimbuddy.data.source.remote.response.ApiContentResponse
import com.oppo.moeslimbuddy.data.source.remote.response.ApiResponse
import okio.BufferedSource
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.nio.charset.Charset

/**
 * Abstract Base Data source class with error handling
 */
abstract class BaseDataSource {

    @Suppress("INFERRED_TYPE_VARIABLE_INTO_EMPTY_INTERSECTION_WARNING")
    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Result<T> {
        try {
            val response = call()
            val code = response.code()
            if (response.isSuccessful) {
                val body = response.body()
                return if (body != null) {
                    if (body is ApiResponse<*>) {
                        if (body.isSuccess()) {
                            Result.Success(body)
                        } else if (body is ApiContentResponse<*>) {
                            if (body.isSuccess()) {
                                Result.Success(body)
                            } else {
                                @Suppress("UNCHECKED_CAST")
                                Result.Error(body.status, body.message, body.data?.content as T?)
                            }
                        } else {
                            @Suppress("UNCHECKED_CAST")
                            Result.Error(body.status, body.message, body.data as T?)
                        }
                    } else {
                        Result.Success(body)
                    }
                } else {
                    Result.Error("BODYNULL", ErrorMessage().connection(), null)
                }
            } else {
                if (code == 401) {
                    (return if (response.errorBody() != null) {
                        val bufferedSource: BufferedSource = response.errorBody()!!.source()
                        bufferedSource.request(Long.MAX_VALUE) // Buffer the entire body.

                        val json =
                            bufferedSource.buffer.clone().readString(Charset.forName("UTF8"))

                        try {
                            val badResponse = Gson().fromJson<ApiResponse<Any>?>(
                                json,
                                object : TypeToken<ApiResponse<Any>?>() {}.type
                            )
                            if (badResponse.data != null) {
                                Result.Unauthorized(badResponse.message)
                            } else {
                                Result.Unauthorized(badResponse.message)
                            }
                        } catch (e: Exception) {
                            Result.Unauthorized(json)
                        }
                    } else {
                        Result.Unauthorized(null)
                    })
                } else
                    if (code == 400 || code == 500) {
                        if (response.errorBody() != null) {
                            val bufferedSource: BufferedSource = response.errorBody()!!.source()
                            bufferedSource.request(Long.MAX_VALUE) // Buffer the entire body.

                            val json =
                                bufferedSource.buffer.clone().readString(Charset.forName("UTF8"))

                            val badResponse = Gson().fromJson<ApiResponse<Any>?>(
                                json,
                                object : TypeToken<ApiResponse<Any>?>() {}.type
                            )
                            return if (code == 500) {
                                Result.Error("SystemError", badResponse.message, null)

                            } else {
                                Result.Error(badResponse.status, badResponse.message, null)
                            }
                        }
                    } else if (code == 503) {
                        return Result.Error("503", ErrorMessage().http503(), null)
                    }
            }
            return Result.Error(code.toString(), response.message(), null)
        } catch (e: Exception) {
            return if (e is ConnectException || e is UnknownHostException ||
                e is MalformedJsonException || e is SocketTimeoutException
            ) {
                Result.Error("ConnectionError", ErrorMessage().connection(), null)
            } else {
                Result.Error("999", ErrorMessage().system(e.message), null)
            }
        }
    }

}