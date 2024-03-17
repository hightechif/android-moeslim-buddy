package com.oppo.moeslimbuddy.domain.model

import android.location.Location

data class RecentLocation(
    val location: Location,
    val locality: String,
    val subAdminArea: String,
    val timestamp: Long?,
)
