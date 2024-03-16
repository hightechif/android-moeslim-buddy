package com.oppo.moeslimbuddy.data.source.local.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.oppo.moeslimbuddy.BuildConfig

@Database(
    entities = [], // [RecentKeywordSearchEntity::class]
    version = BuildConfig.DB_VERSION,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase()
