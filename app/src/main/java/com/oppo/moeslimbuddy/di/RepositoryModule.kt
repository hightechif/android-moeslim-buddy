package com.oppo.moeslimbuddy.di

import com.oppo.moeslimbuddy.data.source.PrayerTimeRepository
import com.oppo.moeslimbuddy.data.source.local.HttpHeaderLocalSource
import com.oppo.moeslimbuddy.data.source.remote.PrayerTimeRemoteDataSource
import com.oppo.moeslimbuddy.domain.repository.IPrayerTimeRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { PrayerTimeRemoteDataSource(get()) }

    single { HttpHeaderLocalSource(get()) }

    single<IPrayerTimeRepository> { PrayerTimeRepository(get()) }
}