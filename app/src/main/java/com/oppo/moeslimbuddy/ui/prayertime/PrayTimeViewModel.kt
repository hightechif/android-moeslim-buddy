package com.oppo.moeslimbuddy.ui.prayertime

import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oppo.moeslimbuddy.data.Result
import com.oppo.moeslimbuddy.data.source.remote.request.ReqPrayerTime
import com.oppo.moeslimbuddy.domain.model.PrayTime
import com.oppo.moeslimbuddy.domain.model.RecentLocation
import com.oppo.moeslimbuddy.domain.usecase.PrayerTimeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale

class PrayTimeViewModel(
    private val useCase: PrayerTimeUseCase
) : ViewModel() {

    private var _recentLocation = MutableStateFlow<RecentLocation?>(null)
    val recentLocation: StateFlow<RecentLocation?> = _recentLocation

    private fun setLocation(input: RecentLocation) {
        if (input != _recentLocation.value) {
            _recentLocation.value = input
        }
    }

    private var _prayTimeList = MutableLiveData<List<PrayTime>>(listOf())
    val prayTimeList: LiveData<List<PrayTime>> = _prayTimeList

    fun setPrayTimeList(input: List<PrayTime>) {
        if (input != _prayTimeList.value) {
            _prayTimeList.postValue(input)
        }
    }

    fun getPrayerTime(request: ReqPrayerTime) =
        useCase.getPrayerTime(request)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(100L), Result.Loading())

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