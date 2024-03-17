package com.oppo.moeslimbuddy.domain.repository

import com.oppo.moeslimbuddy.data.Result
import com.oppo.moeslimbuddy.data.source.remote.request.ReqPrayerTime
import com.oppo.moeslimbuddy.domain.model.PrayerTime
import kotlinx.coroutines.flow.Flow

interface IPrayerTimeRepository {
    fun getPrayerTime(
        date: String, latitude: Double, longitude: Double, method: Int
    ): Flow<Result<PrayerTime?>>
}