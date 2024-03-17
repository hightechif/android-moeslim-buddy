package com.oppo.moeslimbuddy.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("code")
    val code: String?,
    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val data: T?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("timestamp")
    val timeStamp: String?
) {
    fun isSuccess() = "OK" == status
}
