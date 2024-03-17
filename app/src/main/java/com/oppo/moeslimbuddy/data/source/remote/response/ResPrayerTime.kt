package com.oppo.moeslimbuddy.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ResPrayerTime(
    @field:SerializedName("timings")
    val timings: ResTimings?,
)
