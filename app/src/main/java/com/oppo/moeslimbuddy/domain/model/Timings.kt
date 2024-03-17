package com.oppo.moeslimbuddy.domain.model

import kotlin.reflect.KProperty0

data class Timings(
    val imsak: String,
    val fajr: String,
    val sunrise: String,
    val dhuhr: String,
    val asr: String,
    val sunset: String,
    val maghrib: String,
    val isha: String,
    val firstthird: String,
    val midnight: String,
    val lastthird: String,
) : HasStringProperties {

    override val properties: List<KProperty0<String>>
        get() = listOf(::imsak, ::fajr, ::dhuhr, ::asr, ::maghrib, ::isha)

    val getAsPrayTimeList: List<PrayTime>
        get() {
            val mutList = mutableListOf<PrayTime>()
            for (prop in properties) {
                mutList.add(PrayTime(prop.name, prop.get()))
            }
            return mutList.toList()
        }

}