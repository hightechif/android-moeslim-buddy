package com.oppo.moeslimbuddy.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ResTimings(
    @field:SerializedName("Imsak")
    val imsak: String,
    @field:SerializedName("Fajr")
    val fajr: String,
    @field:SerializedName("Sunrise")
    val sunrise: String,
    @field:SerializedName("Dhuhr")
    val dhuhr: String,
    @field:SerializedName("Asr")
    val asr: String,
    @field:SerializedName("Sunset")
    val sunset: String,
    @field:SerializedName("Maghrib")
    val maghrib: String,
    @field:SerializedName("Isha")
    val isha: String,
    @field:SerializedName("Firstthird")
    val firstthird: String,
    @field:SerializedName("Midnight")
    val midnight: String,
    @field:SerializedName("Lastthird")
    val lastthird: String,
)
