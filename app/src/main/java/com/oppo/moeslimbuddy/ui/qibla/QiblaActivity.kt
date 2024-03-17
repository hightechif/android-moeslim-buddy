package com.oppo.moeslimbuddy.ui.qibla

import android.content.Intent
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import com.oppo.moeslimbuddy.databinding.ActivityQiblaBinding
import com.oppo.moeslimbuddy.ui.base.BaseActivity
import io.github.derysudrajat.compassqibla.CompassQibla

class QiblaActivity : BaseActivity() {
    companion object {
        fun open(activity: FragmentActivity) {
            val intent = Intent(activity, QiblaActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            ActivityCompat.startActivity(activity, intent, null)
        }
    }

    private val viewModel: QiblaViewModel by viewModels()

    private lateinit var binding: ActivityQiblaBinding
    private lateinit var compassBuilder: CompassQibla.Builder

    private var currentCompassDegree = 0f
    private var currentNeedleDegree = 0f

    override fun setupView() {
        binding = ActivityQiblaBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun setupListener() {
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.btnLocation.setOnClickListener {

        }

        compassBuilder = CompassQibla.Builder(this)
        compassBuilder.onPermissionGranted { permission ->
            binding.btnLocation.isVisible = false
            binding.llCompass.isVisible = true
            Toast.makeText(this, "onPermissionGranted $permission", Toast.LENGTH_SHORT).show()
        }.onPermissionDenied {
            binding.llCompass.isVisible = false
            binding.btnLocation.isVisible = true
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

    override fun setupObserver() {}

    override fun initData() {
//        val kaabaLat = 21.4224779
//        val kaabaLong = 39.8251832

    }

}