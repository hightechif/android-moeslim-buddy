package com.oppo.moeslimbuddy.ui.base

import android.annotation.TargetApi
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.Insets
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BaseActivity : AppCompatActivity(), BaseNavigator {

    private var pDialog: Dialog? = null

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

    override fun toast(message: String?) {
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(this@BaseActivity, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun finish() {
        super.finish()
    }

    override fun onSettingsEvent() {

    }

    fun delay(delay: Long, runnable: () -> Unit) {
        Handler().postDelayed({
            runnable.invoke()
        }, delay)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun hideLoading() {
        CoroutineScope(Dispatchers.Main).launch {
            if (pDialog?.isShowing == true) pDialog?.dismiss()
        }
    }

    override fun showLoading() {
        CoroutineScope(Dispatchers.Main).launch {
            if (pDialog?.isShowing == false) pDialog?.show()
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun requestPermissionsSafely(permissions: Array<String>, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode)
        }
    }

    override fun onApplyWindowEvent(insets: Insets) {

    }

    fun setStatusbarColor(color: Int) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, color)

    }

}