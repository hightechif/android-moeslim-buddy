package com.oppo.moeslimbuddy.data.source.remote.network

import com.oppo.moeslimbuddy.data.source.remote.response.ApiResponse
import com.oppo.moeslimbuddy.data.source.remote.response.ResPrayerTime
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PrayerTimeApiService {
    @GET("v1/timings/{date}")
    suspend fun getPrayerTime(
        @Path("date") date: String, // format: dd-MM-yyyy
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("method") method: Int = 20 // 20 for Kemenag
    ): Response<ApiResponse<ResPrayerTime?>>

}