package com.oppo.moeslimbuddy.ui.base

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup.LayoutParams
import com.oppo.moeslimbuddy.databinding.ViewProgressBinding

class ProgressView {
    companion object {
        private var popup: Popup? = null

        fun show(context: Context) {
            val binding = ViewProgressBinding.inflate(
                LayoutInflater.from(context),
                null, false
            )
            if (popup == null) {
                popup = Popup.show(binding.root, width = LayoutParams.WRAP_CONTENT.toFloat())
            }
        }

        fun close() {
            try {
                popup?.dismiss()
                popup = null
            } catch (ignore: Exception) {

            }
        }
    }
}