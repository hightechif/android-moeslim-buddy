package com.oppo.moeslimbuddy.ui.prayertime

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.oppo.moeslimbuddy.databinding.ItemHomePraytimeBinding
import com.oppo.moeslimbuddy.domain.model.PrayTime

class PrayTimeHolder(val binding: ItemHomePraytimeBinding) :
    RecyclerView.ViewHolder(binding.root) {

    var delegate: PrayTimeDelegate? = null

    fun process(list: MutableList<PrayTime>, position: Int) {
        val t = list[position]
        setData(t)

        if (delegate != null) {
            binding.root.setOnClickListener {
                delegate?.onClick(t, position, binding)
            }

            delegate?.onDraw(t, position, binding)
        }
    }

    fun setData(t: PrayTime?) {
        if (t != null) {
            binding.until.isVisible = false
            binding.title.text = t.prayName
            binding.praytime.text = t.time
        }
    }

}