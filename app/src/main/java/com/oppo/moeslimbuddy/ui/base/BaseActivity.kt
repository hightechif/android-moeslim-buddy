package com.oppo.moeslimbuddy.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        setupListener()
        setupObserver()
        initData()
    }

    abstract fun setupView()
    abstract fun setupListener()
    abstract fun setupObserver()
    abstract fun initData()

}