package com.oppo.moeslimbuddy.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.location.LocationListenerCompat
import androidx.core.location.LocationManagerCompat
import com.google.android.material.snackbar.Snackbar
import com.oppo.moeslimbuddy.databinding.ActivityMainBinding
import com.oppo.moeslimbuddy.domain.model.RecentLocation
import com.oppo.moeslimbuddy.ui.base.BaseActivity
import com.oppo.moeslimbuddy.ui.base.ProgressView
import com.oppo.moeslimbuddy.ui.prayertime.PrayerTimeActivity
import com.oppo.moeslimbuddy.ui.qibla.QiblaActivity
import com.oppo.moeslimbuddy.util.PermissionUtils
import io.github.derysudrajat.compassqibla.LocationUtils.checkLocationPermission
import kotlin.system.exitProcess

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private var hasGetLocation = true

    private lateinit var locationManager: LocationManager
    private val locationListener = LocationListenerCompat { location ->
        ProgressView.show(this@MainActivity)
        viewModel.setLocation(
            RecentLocation(
                location.latitude,
                location.longitude,
                location.accuracy.toDouble(),
                System.currentTimeMillis()
            )
        )
        ProgressView.close()
        hasGetLocation = true
    }

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

            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1_000,
                0.5f,
                locationListener
            )
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                5_000,
                0.5f,
                locationListener
            )
        }

    override fun setupView() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkLocationPermission {
            ProgressView.show(this)
            requestLocation()
        }
    }

    override fun setupListener() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        binding.btnQibla.setOnClickListener {
            checkForLocationPermissions {
                requestLocation()
                if (hasGetLocation) {
                    QiblaActivity.open(this)
                }
            }
        }
        binding.btnPrayer.setOnClickListener {
            PrayerTimeActivity.open(this, viewModel.recentLocation.value)
        }
        binding.btnMosque.setOnClickListener {
            // TODO: Open NearMosqueActivity
            val snackbar =
                Snackbar.make(
                    this,
                    binding.root,
                    "Feature is under development.",
                    Snackbar.LENGTH_SHORT
                )
            snackbar.show()
        }
        binding.btnExit.setOnClickListener {
            finish()
            exitProcess(0)
        }
    }

    override fun setupObserver() {
        viewModel.recentLocation.observe(this) {
            if (it != null) {
                ProgressView.close()
            }
        }
    }

    override fun initData() {

    }

    private fun checkIsLocationEnabled(): Boolean =
        LocationManagerCompat.isLocationEnabled(locationManager)

    @SuppressLint("MissingPermission")
    private fun requestLocation() {
        checkForLocationPermissions {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1_000,
                0.5f,
                locationListener
            )

            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                5_000,
                0.5f,
                locationListener
            )
        }
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

    override fun onDestroy() {
        locationManager.removeUpdates(locationListener)
        super.onDestroy()
    }

}