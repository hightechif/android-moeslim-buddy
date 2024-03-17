package com.oppo.moeslimbuddy.di

import com.oppo.moeslimbuddy.data.source.remote.network.PrayerTimeApiService
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val serviceModule = module {
    single { providePrayerTimeApiService(get(named("api"))) }
}

private fun providePrayerTimeApiService(retrofit: Retrofit) =
    retrofit.create(PrayerTimeApiService::class.java)