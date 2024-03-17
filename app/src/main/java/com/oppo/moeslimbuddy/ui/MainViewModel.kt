package com.oppo.moeslimbuddy.ui

import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oppo.moeslimbuddy.domain.model.RecentLocation
import kotlinx.coroutines.launch
import java.util.Locale

class MainViewModel : ViewModel() {

    private var _recentLocation = MutableLiveData<RecentLocation?>(null)
    val recentLocation: LiveData<RecentLocation?> = _recentLocation

    private fun setLocation(input: RecentLocation) {
        _recentLocation.postValue(input)
    }

    @Suppress("DEPRECATION")
    fun getLocationAddress(context: Context, location: Location) {
        viewModelScope.launch {
            Geocoder(context, Locale.getDefault()).apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    getFromLocation(location.latitude, location.longitude, 1) { addresses ->
                        if (addresses.isNotEmpty()) {
                            addresses.first().let {
                                setLocation(
                                    RecentLocation(
                                        location,
                                        it.locality,
                                        it.subAdminArea,
                                        System.currentTimeMillis()
                                    )
                                )
                            }
                        }
                    }
                } else {
                    getFromLocation(location.latitude, location.longitude, 1)?.first()?.let {
                        setLocation(
                            RecentLocation(
                                location,
                                it.locality,
                                it.subAdminArea,
                                System.currentTimeMillis()
                            )
                        )
                    }
                }
            }
        }
    }

}