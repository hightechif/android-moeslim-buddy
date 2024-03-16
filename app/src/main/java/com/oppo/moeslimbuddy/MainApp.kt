package com.oppo.moeslimbuddy

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.oppo.moeslimbuddy.di.databaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class MainApp : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO) // Ignore Night Mode

        with(this) {
            startKoin {
                androidContext(applicationContext)
                modules(
                    listOf(
                        databaseModule,
                    )
                )
            }
        }

    }

}