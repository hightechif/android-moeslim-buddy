package com.oppo.moeslimbuddy.data.mapper

import com.oppo.moeslimbuddy.data.source.remote.response.ResPrayerTime
import com.oppo.moeslimbuddy.domain.model.PrayerTime
import org.mapstruct.Mapper
import org.mapstruct.Mappings

@Mapper
interface PrayerTimeMapper {

    @Mappings
    fun mapPrayerTimeResponseToModel(input: ResPrayerTime?): PrayerTime?

}