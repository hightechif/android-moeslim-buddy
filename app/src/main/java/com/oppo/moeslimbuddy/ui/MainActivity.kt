package com.oppo.moeslimbuddy.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.location.LocationListenerCompat
import androidx.core.location.LocationManagerCompat
import com.google.android.material.snackbar.Snackbar
import com.oppo.moeslimbuddy.databinding.ActivityMainBinding
import com.oppo.moeslimbuddy.ui.base.BaseActivity
import com.oppo.moeslimbuddy.ui.prayertime.PrayerTimeActivity
import com.oppo.moeslimbuddy.ui.qibla.QiblaActivity
import com.oppo.moeslimbuddy.util.PermissionUtils
import kotlin.system.exitProcess

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    private lateinit var locationManager: LocationManager
    private val locationListener = LocationListenerCompat { location ->
        viewModel.setLocation(
            location.latitude,
            location.longitude,
            location.accuracy
        )
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

        checkLocPermission {
            requestLocation()
        }
    }

    override fun setupListener() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        binding.btnQibla.setOnClickListener {
            checkForLocationPermissions {
                requestLocation()
                QiblaActivity.open(this)
            }
        }
        binding.btnPrayer.setOnClickListener {
            PrayerTimeActivity.open(this)
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

    private fun checkLocPermission(onMissing: () -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            onMissing.invoke()
            return
        }
    }

    override fun onDestroy() {
        locationManager.removeUpdates(locationListener)
        super.onDestroy()
    }

}