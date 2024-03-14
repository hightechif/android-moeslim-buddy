package com.oppo.moeslimbuddy.ui.qibla

import android.content.Intent
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.oppo.moeslimbuddy.databinding.ActivityQiblaBinding
import com.oppo.moeslimbuddy.ui.base.BaseActivity
import com.oppo.moeslimbuddy.util.CompassUtil
import io.github.derysudrajat.compassqibla.CompassQibla
import io.github.derysudrajat.compassqibla.CompassQiblaViewModel

class QiblaActivity : BaseActivity() {

    private val viewModel = CompassQiblaViewModel()

    private lateinit var binding: ActivityQiblaBinding
    private var currentCompassDegree = 0f
    private var currentNeedleDegree = 0f

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
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun setupObserver() {

    }

    override fun initData() {
        val currentLat = -6.202362
        val currentLong = 106.811935
        val kaabaLat = 21.4224779
        val kaabaLong = 39.8251832
        val degree = CompassUtil.calculationByDistance(currentLat, currentLong, kaabaLat, kaabaLong)
//        binding.ivCompass.rotation = degree.toFloat()

        CompassQibla.Builder(this).onPermissionGranted { permission ->
            Toast.makeText(this, "onPermissionGranted $permission", Toast.LENGTH_SHORT).show()
        }.onPermissionDenied {
            Toast.makeText(this, "onPermissionDenied", Toast.LENGTH_SHORT).show()
        }.onGetLocationAddress { address ->
            binding.tvLocation.text = buildString {
                append(address.locality)
                append(", ")
                append(address.subAdminArea)
            }
        }.onDirectionChangeListener { qiblaDirection ->
            binding.tvDirection.text = if (qiblaDirection.isFacingQibla) "You're Facing Qibla"
            else "${qiblaDirection.needleAngle.toInt()}°"

            val rotateCompass = RotateAnimation(
                currentCompassDegree, qiblaDirection.compassAngle, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f
            ).apply {
                duration = 1000
            }
            currentCompassDegree = qiblaDirection.compassAngle

            binding.ivCompass.startAnimation(rotateCompass)

            val rotateNeedle = RotateAnimation(
                currentNeedleDegree, qiblaDirection.needleAngle, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f
            ).apply {
                duration = 1000
            }
            currentNeedleDegree = qiblaDirection.needleAngle

            binding.ivNeedle.startAnimation(rotateNeedle)
        }.build()
    }


}