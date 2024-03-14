package com.oppo.moeslimbuddy.ui.qibla

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import com.oppo.moeslimbuddy.databinding.ActivityQiblaBinding
import com.oppo.moeslimbuddy.ui.base.BaseActivity
import com.oppo.moeslimbuddy.util.PermissionUtils
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

    @SuppressLint("MissingPermission")
    private val permissionResultLauncher =
        registerForActivityResult((ActivityResultContracts.RequestMultiplePermissions())) {
            if (it.containsValue(false)) {
                Toast.makeText(
                    this,
                    "Mohon memberikan izin kepada aplikasi untuk mengakses lokasi & kamera",
                    Toast.LENGTH_SHORT
                ).show()

                return@registerForActivityResult
            }

//            locationManager.requestLocationUpdates(
//                LocationManager.GPS_PROVIDER,
//                1_000,
//                0.5f,
//                locationListener
//            )
//            locationManager.requestLocationUpdates(
//                LocationManager.NETWORK_PROVIDER,
//                5_000,
//                0.5f,
//                locationListener
//            )
        }

    override fun setupView() {
        binding = ActivityQiblaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkForLocationPermissions {
            binding.btnLocation.isVisible = false
            binding.llCompass.isVisible = true
        }
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

    override fun setupObserver() {
//        viewModel.lat.observe(this) {
//            binding.btnLocation.isVisible = it == null
//            binding.llCompass.isVisible = it != null
//        }
    }

    override fun initData() {
        val kaabaLat = 21.4224779
        val kaabaLong = 39.8251832

    }

    private fun checkForLocationPermissions(action: () -> Unit) {
        PermissionUtils.checkPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ),
            permissionResultLauncher,
            null
        ) {
            action.invoke()
        }
    }

}