package com.oppo.moeslimbuddy.ui.prayertime

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.oppo.moeslimbuddy.data.source.remote.request.ReqPrayerTime
import com.oppo.moeslimbuddy.databinding.ActivityPrayerTimeBinding
import com.oppo.moeslimbuddy.domain.model.PrayTime
import com.oppo.moeslimbuddy.ui.base.BaseActivity
import com.oppo.moeslimbuddy.ui.base.ProgressView
import com.oppo.moeslimbuddy.util.RecyclerViewUtils
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDateTime

class PrayerTimeActivity : BaseActivity() {
    companion object {
        private const val MY_PERMISSIONS_REQUEST_LOCATION = 99
        private const val MY_PERMISSIONS_REQUEST_BACKGROUND_LOCATION = 66
        fun open(activity: FragmentActivity) {
            val intent = Intent(activity, PrayerTimeActivity::class.java)
            ActivityCompat.startActivity(activity, intent, null)
        }
    }

    private lateinit var binding: ActivityPrayerTimeBinding
    private val viewModel: PrayTimeViewModel by viewModel()
    private var prayTimeAdapter = PrayTimeAdapter()
    private val now = LocalDateTime.now()
    private var hasCalled = false

    private var fusedLocationProvider: FusedLocationProviderClient? = null
    private val locationRequest: LocationRequest = LocationRequest.create().apply {
        interval = 30
        fastestInterval = 10
        priority = Priority.PRIORITY_BALANCED_POWER_ACCURACY
        maxWaitTime = 60
    }

    private var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val locationList = locationResult.locations
            if (locationList.isNotEmpty()) {
                //The last location in the list is the newest
                val location = locationList.last()
                currentLocation = location
                viewModel.getLocationAddress(this@PrayerTimeActivity, currentLocation)
                ProgressView.close()
            }
        }
    }
    private lateinit var currentLocation: Location

    override fun setupView() {
        binding = ActivityPrayerTimeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this)
        checkLocationPermission()

        binding.rvPrayTime.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = prayTimeAdapter
            addItemDecoration(RecyclerViewUtils.ItemDecoration(6, 0, 16))
        }
    }

    override fun setupListener() {
        binding.back.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        prayTimeAdapter.delegate = object : PrayTimeDelegate {
            override fun onClick(t: PrayTime?, position: Int, viewBinding: ViewBinding) {

            }

            override fun onDraw(t: PrayTime?, position: Int, viewBinding: ViewBinding) {
            }

        }
    }

    override fun setupObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.recentLocation.collectLatest {
                    binding.address.text = buildString {
                        append(it?.locality)
                        append(", ")
                        append(it?.subAdminArea)
                    }
                    val latitude = it?.location?.latitude
                    val longitude = it?.location?.longitude
                    if (latitude != null && longitude != null && !hasCalled) {
                        val request = ReqPrayerTime(
                            now,
                            latitude,
                            longitude,
                            null,
                        )
                        getPrayerTime(request)
                    }
                }
            }
        }

        viewModel.prayTimeList.observe(this) {
            prayTimeAdapter.setData(it)
        }
    }

    override fun initData() {
        binding.textTime.text = String.format("%s:%s", now.hour, now.minute)
    }

    private fun getPrayerTime(request: ReqPrayerTime) {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getPrayerTime(request).collect {
                    viewModel.setPrayTimeList(
                        it.data?.timings?.getAsPrayTimeList ?: emptyList()
                    )
                    hasCalled = true
                }
            }
        }
    }

    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this, Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(this).setTitle("Location Permission Needed")
                    .setMessage("This app needs the Location permission, please accept to use location functionality")
                    .setPositiveButton(
                        "OK"
                    ) { _, _ ->
                        //Prompt the user once explanation has been shown
                        requestLocationPermission()
                    }.create().show()
            } else {
                // No explanation needed, we can request the permission.
                requestLocationPermission()
            }
        } else {
            checkBackgroundLocation()
        }
    }

    private fun checkBackgroundLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestBackgroundLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
            ), MY_PERMISSIONS_REQUEST_LOCATION
        )
    }

    private fun requestBackgroundLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ), MY_PERMISSIONS_REQUEST_BACKGROUND_LOCATION
            )
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MY_PERMISSIONS_REQUEST_LOCATION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(
                            this, Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        fusedLocationProvider?.requestLocationUpdates(
                            locationRequest, locationCallback, Looper.getMainLooper()
                        )

                        // Now check background location
                        checkBackgroundLocation()
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show()

                    // Check if we are in a state where the user has denied the permission and
                    // selected Don't ask again
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(
                            this, Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    ) {
                        startActivity(
                            Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", this.packageName, null),
                            ),
                        )
                    }
                }
                return
            }

            MY_PERMISSIONS_REQUEST_BACKGROUND_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(
                            this, Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        fusedLocationProvider?.requestLocationUpdates(
                            locationRequest, locationCallback, Looper.getMainLooper()
                        )

                        Toast.makeText(
                            this, "Granted Background Location Permission", Toast.LENGTH_LONG
                        ).show()
                    }
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show()
                }
                return

            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProvider?.requestLocationUpdates(
                locationRequest, locationCallback, Looper.getMainLooper()
            )
        }
    }

    override fun onPause() {
        super.onPause()
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProvider?.removeLocationUpdates(locationCallback)
        }
    }

    override fun onDestroy() {
        fusedLocationProvider?.removeLocationUpdates(locationCallback)
        fusedLocationProvider = null
        super.onDestroy()
    }

}