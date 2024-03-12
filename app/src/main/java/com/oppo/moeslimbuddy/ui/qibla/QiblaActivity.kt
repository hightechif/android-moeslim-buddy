package com.oppo.moeslimbuddy.ui.qibla

import android.content.Intent
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.oppo.moeslimbuddy.databinding.ActivityQiblaBinding
import com.oppo.moeslimbuddy.ui.base.BaseActivity

class QiblaActivity : BaseActivity() {

    private lateinit var binding: ActivityQiblaBinding

    companion object {
        fun open(activity: FragmentActivity) {
            val intent = Intent(activity, QiblaActivity::class.java)
            ActivityCompat.startActivity(activity, intent, null)
        }
    }

    override fun setupView() {
        binding = ActivityQiblaBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun setupListener() {

    }

    override fun setupObserver() {

    }

    override fun initData() {

    }

}