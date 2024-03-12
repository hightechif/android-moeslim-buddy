package com.oppo.moeslimbuddy

import com.oppo.moeslimbuddy.databinding.ActivityMainBinding
import com.oppo.moeslimbuddy.ui.base.BaseActivity
import com.oppo.moeslimbuddy.ui.mosque.NearMosquesActivity
import com.oppo.moeslimbuddy.ui.prayertime.PrayerTimeActivity
import com.oppo.moeslimbuddy.ui.qibla.QiblaActivity

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun setupView() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun setupListener() {
        binding.btnQibla.setOnClickListener {
            QiblaActivity.open(this)
        }
        binding.btnPrayer.setOnClickListener {
            PrayerTimeActivity.open(this)
        }
        binding.btnMosque.setOnClickListener {
            NearMosquesActivity.open(this)
        }
    }

    override fun setupObserver() {

    }

    override fun initData() {

    }

}