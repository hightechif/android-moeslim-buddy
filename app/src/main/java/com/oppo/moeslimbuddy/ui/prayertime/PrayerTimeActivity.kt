package com.oppo.moeslimbuddy.ui.prayertime

import android.content.Intent
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.oppo.moeslimbuddy.databinding.ActivityPrayerTimeBinding
import com.oppo.moeslimbuddy.ui.base.BaseActivity

class PrayerTimeActivity : BaseActivity() {

    private lateinit var binding: ActivityPrayerTimeBinding

    companion object {
        fun open(activity: FragmentActivity) {
            val intent = Intent(activity, PrayerTimeActivity::class.java)
            ActivityCompat.startActivity(activity, intent, null)
        }
    }

    override fun setupView() {
        binding = ActivityPrayerTimeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun setupListener() {
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun setupObserver() {

    }

    override fun initData() {

    }

}