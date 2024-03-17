package com.oppo.moeslimbuddy.ui.mosque

import android.content.Intent
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.oppo.moeslimbuddy.databinding.ActivityNearMosquesBinding
import com.oppo.moeslimbuddy.ui.base.BaseActivity

class NearMosquesActivity : BaseActivity() {

    companion object {
        fun open(activity: FragmentActivity) {
            val intent = Intent(activity, NearMosquesActivity::class.java)
            ActivityCompat.startActivity(activity, intent, null)
        }
    }

    private lateinit var binding: ActivityNearMosquesBinding

    override fun setupView() {
        binding = ActivityNearMosquesBinding.inflate(layoutInflater)
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