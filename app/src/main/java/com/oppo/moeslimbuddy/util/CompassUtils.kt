package com.oppo.moeslimbuddy.util

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

object CompassUtils {
    fun calculationByDistance(
        initialLat: Double, initialLong: Double,
        finalLat: Double, finalLong: Double
    ): Double {
        val R = 6371 // km (Earth radius)
        val dLat = toRadians(finalLat - initialLat)
        val dLon = toRadians(finalLong - initialLong)
        val initialLatInRadians = toRadians(initialLat)
        val finalLatInRadians = toRadians(finalLat)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                sin(dLon / 2) * sin(dLon / 2) *
                cos(initialLatInRadians) *
                cos(finalLatInRadians)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return R * c
    }

    fun toRadians(deg: Double): Double {
        return deg * (Math.PI / 180)
    }
}