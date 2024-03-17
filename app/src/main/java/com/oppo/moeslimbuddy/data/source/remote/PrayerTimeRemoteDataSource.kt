package com.oppo.moeslimbuddy.data.source.remote

import com.oppo.moeslimbuddy.data.source.remote.network.PrayerTimeApiService

class PrayerTimeRemoteDataSource(
    private val service: PrayerTimeApiService
) : BaseRemoteDataSource() {

    suspend fun getPrayerTime(
        date: String, latitude: Double, longitude: Double, method: Int
    ) = getResult {
        service.getPrayerTime(date, latitude, longitude, method)
    }

}