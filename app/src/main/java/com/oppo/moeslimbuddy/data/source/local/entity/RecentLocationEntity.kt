package com.oppo.moeslimbuddy.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_location")
data class RecentLocationEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "latitude")
    val latitude: Double,

    @ColumnInfo(name = "longitude")
    val longitude: Double,

    @ColumnInfo(name = "accuracy")
    val accuracy: Double?,

    @ColumnInfo(name = "timestamp")
    val timestamp: Double?,

    )
