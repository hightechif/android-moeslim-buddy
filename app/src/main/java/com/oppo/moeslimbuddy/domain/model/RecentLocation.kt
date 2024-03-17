package com.oppo.moeslimbuddy.domain.model

data class RecentLocation(
    val latitude: Double,
    val longitude: Double,
    val accuracy: Double?,
    val timestamp: Long?,
)
