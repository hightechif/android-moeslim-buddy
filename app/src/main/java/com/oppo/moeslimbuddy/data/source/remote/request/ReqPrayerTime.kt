package com.oppo.moeslimbuddy.data.source.remote.request

import java.time.LocalDateTime

data class ReqPrayerTime(
    val localDateTime: LocalDateTime,
    val latitude: Double,
    val longitude: Double,
    val method: Int?
)
