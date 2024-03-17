package com.oppo.moeslimbuddy.di

import com.oppo.moeslimbuddy.ui.prayertime.PrayTimeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { PrayTimeViewModel(get()) }
}