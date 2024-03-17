package com.oppo.moeslimbuddy.data.source.remote.response

import com.google.gson.annotations.SerializedName

class PageableDataResponse (
    @SerializedName("pageNumber")
    val pageNumber: Int,
    @SerializedName("pageSize")
    val pageSize: Int
)
