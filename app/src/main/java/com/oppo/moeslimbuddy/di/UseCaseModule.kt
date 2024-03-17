package com.oppo.moeslimbuddy.di

import com.oppo.moeslimbuddy.domain.usecase.GetPrayerTime
import com.oppo.moeslimbuddy.domain.usecase.PrayerTimeUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single { GetPrayerTime(get()) }
    single { PrayerTimeUseCase(get()) }
}