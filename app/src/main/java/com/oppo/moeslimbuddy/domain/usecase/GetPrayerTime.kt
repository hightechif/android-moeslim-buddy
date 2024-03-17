package com.oppo.moeslimbuddy.domain.usecase

import com.oppo.moeslimbuddy.data.Result
import com.oppo.moeslimbuddy.data.source.remote.request.ReqPrayerTime
import com.oppo.moeslimbuddy.domain.model.PrayerTime
import com.oppo.moeslimbuddy.domain.repository.IPrayerTimeRepository
import com.oppo.moeslimbuddy.util.DateTimeUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetPrayerTime(private val repository: IPrayerTimeRepository) {

    operator fun invoke(
        request: ReqPrayerTime
    ): Flow<Result<PrayerTime?>> {
        val date = DateTimeUtil.getIDDate(request.localDateTime, "dd-MM-yyyy")
        val method = request.method ?: 20 // 20 is default for Kemenag
        return repository.getPrayerTime(date, request.latitude, request.longitude, method).map {
            when (it.status) {
                Result.Status.SUCCESS -> it
                else -> it
            }
        }
    }

}