package com.oppo.moeslimbuddy

import com.google.android.material.snackbar.Snackbar
import com.oppo.moeslimbuddy.databinding.ActivityMainBinding
import com.oppo.moeslimbuddy.ui.base.BaseActivity
import com.oppo.moeslimbuddy.ui.prayertime.PrayerTimeActivity
import com.oppo.moeslimbuddy.ui.qibla.QiblaActivity
import kotlin.system.exitProcess

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
            // TODO: Open NearMosqueActivity
            val snackbar =
                Snackbar.make(this, binding.root, "Feature is under development.", Snackbar.LENGTH_SHORT)
            snackbar.show()
        }
        binding.btnExit.setOnClickListener {
            finish()
            exitProcess(0)
        }
    }

    override fun setupObserver() {

    }

    override fun initData() {

    }

}