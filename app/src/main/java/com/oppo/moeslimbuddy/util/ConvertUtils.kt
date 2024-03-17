package com.oppo.moeslimbuddy.util

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.oppo.moeslimbuddy.R
import kotlin.math.roundToInt

object ConvertUtils {

    val Int.dp: Int
        get() = (this * Resources.getSystem().displayMetrics.density).roundToInt()

    fun parseColor(context: Context, colorStr: String?): Int {
        return try {
            Color.parseColor(colorStr)
        } catch (e: IllegalArgumentException) {
            ContextCompat.getColor(context, R.color.darkGreen)
        }
    }
}