package com.oppo.moeslimbuddy.di

import androidx.room.Room
import com.oppo.moeslimbuddy.BuildConfig
import com.oppo.moeslimbuddy.data.source.local.persistence.AppDatabase
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        if (BuildConfig.DEBUG) {
            Room.databaseBuilder(
                androidContext(),
                AppDatabase::class.java,
                BuildConfig.DB_NAME
            ).fallbackToDestructiveMigration()
                .build()
        } else {
            val passphrase: ByteArray = SQLiteDatabase.getBytes(BuildConfig.DB_PASS.toCharArray())
            val factory = SupportFactory(passphrase)
            Room.databaseBuilder(
                androidContext(),
                AppDatabase::class.java,
                BuildConfig.DB_NAME
            ).fallbackToDestructiveMigration()
                .openHelperFactory(factory)
                .build()
        }
    }
}