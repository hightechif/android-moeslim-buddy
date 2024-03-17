package com.oppo.moeslimbuddy.ui.prayertime

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oppo.moeslimbuddy.databinding.ItemHomePraytimeBinding
import com.oppo.moeslimbuddy.domain.model.PrayTime

class PrayTimeAdapter : RecyclerView.Adapter<PrayTimeHolder>() {

    private val list = mutableListOf<PrayTime>()
    var delegate: PrayTimeDelegate? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrayTimeHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemHomePraytimeBinding.inflate(inflater, parent, false)
        val holder = PrayTimeHolder(itemBinding)
        holder.delegate = delegate
        return holder
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: PrayTimeHolder, position: Int) {
        holder.process(list, position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(input: List<PrayTime>) {
        if (input != list) {
            list.clear()
            list.addAll(input)
            notifyDataSetChanged()
        }
    }

}