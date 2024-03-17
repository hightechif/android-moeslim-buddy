package com.oppo.moeslimbuddy.domain.model

data class RecentLocation(
    val latitude: Double,
    val longitude: Double,
    val accuracy: Double?,
    val locality: String,
    val subAdminArea: String,
    val timestamp: Long?,
)
