package com.oppo.moeslimbuddy

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class MainApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // TODO: Setup Timber debug tree
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO) // Ignore Night Mode
    }

}