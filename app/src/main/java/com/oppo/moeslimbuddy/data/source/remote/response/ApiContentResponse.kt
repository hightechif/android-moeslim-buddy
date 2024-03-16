package com.oppo.moeslimbuddy.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ApiContentResponse<T>(
    @SerializedName("code")
    val code: String?,
    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val data: ContentResponse<T>?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("timestamp")
    val timeStamp: String?
) {
    fun isSuccess() = ("00" == status || "01" == status) && data != null && data.content != null
}