package com.oppo.moeslimbuddy.data.source

import com.oppo.moeslimbuddy.data.NetworkBoundProcessResource
import com.oppo.moeslimbuddy.data.Result
import com.oppo.moeslimbuddy.data.mapper.PrayerTimeMapper
import com.oppo.moeslimbuddy.data.source.remote.PrayerTimeRemoteDataSource
import com.oppo.moeslimbuddy.data.source.remote.response.ApiResponse
import com.oppo.moeslimbuddy.data.source.remote.response.ResPrayerTime
import com.oppo.moeslimbuddy.domain.model.PrayerTime
import com.oppo.moeslimbuddy.domain.repository.IPrayerTimeRepository
import org.mapstruct.factory.Mappers
import timber.log.Timber

class PrayerTimeRepository(
    private val remoteDataSource: PrayerTimeRemoteDataSource
) : IPrayerTimeRepository {

    private val mapper = Mappers.getMapper(PrayerTimeMapper::class.java)
    override fun getPrayerTime(
        date: String, latitude: Double, longitude: Double, method: Int
    ) = object : NetworkBoundProcessResource<PrayerTime?, ApiResponse<ResPrayerTime?>>() {
        override suspend fun createCall(): Result<ApiResponse<ResPrayerTime?>> {
            return remoteDataSource.getPrayerTime(date, latitude, longitude, method)
        }

        override suspend fun callBackResult(data: ApiResponse<ResPrayerTime?>): PrayerTime? {
            Timber.d("DEBUG FADHIL: data=$data")
            return data.data?.let {
                return@let mapper.mapPrayerTimeResponseToModel(it)
            }
        }
    }.asFlow()

}