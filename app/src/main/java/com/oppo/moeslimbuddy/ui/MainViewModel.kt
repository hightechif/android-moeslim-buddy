package com.oppo.moeslimbuddy.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private var _lat = MutableLiveData<Double?>(null)
    val lat: LiveData<Double?> = _lat
    private var _lng = MutableLiveData<Double?>(null)
    val lng: LiveData<Double?> = _lng
    private var _positionAccuracy = MutableLiveData<Double?>(null)
    val positionAccuracy: LiveData<Double?> = _positionAccuracy

    fun setLocation(latitude: Double, longitude: Double, accuracy: Float) {
        _lat.postValue(latitude)
        _lng.postValue(longitude)
        _positionAccuracy.postValue(accuracy.toDouble())
    }

}