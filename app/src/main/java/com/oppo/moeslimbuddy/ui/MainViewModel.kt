package com.oppo.moeslimbuddy.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.oppo.moeslimbuddy.domain.model.RecentLocation

class MainViewModel : ViewModel() {

    private var _lat = MutableLiveData<Double?>(null)
    val lat: LiveData<Double?> = _lat
    private var _lng = MutableLiveData<Double?>(null)
    val lng: LiveData<Double?> = _lng
    private var _positionAccuracy = MutableLiveData<Double?>(null)
    val positionAccuracy: LiveData<Double?> = _positionAccuracy

    private var _recentLocation = MutableLiveData<RecentLocation?>(null)
    val recentLocation: LiveData<RecentLocation?> = _recentLocation

    fun setLocation(location: RecentLocation) {
        _recentLocation.postValue(location)
    }

}