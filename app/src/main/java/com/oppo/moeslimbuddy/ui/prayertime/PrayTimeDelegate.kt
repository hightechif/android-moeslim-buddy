package com.oppo.moeslimbuddy.ui.prayertime

import androidx.viewbinding.ViewBinding
import com.oppo.moeslimbuddy.domain.model.PrayTime

interface PrayTimeDelegate {
    fun onClick(t: PrayTime?, position: Int, viewBinding: ViewBinding)
    fun onDraw(t: PrayTime?, position: Int, viewBinding: ViewBinding)
}