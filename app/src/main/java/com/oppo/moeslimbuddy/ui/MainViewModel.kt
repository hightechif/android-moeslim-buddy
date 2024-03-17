package com.oppo.moeslimbuddy.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.oppo.moeslimbuddy.domain.model.RecentLocation

class MainViewModel : ViewModel() {

    private var _recentLocation = MutableLiveData<RecentLocation?>(null)
    val recentLocation: LiveData<RecentLocation?> = _recentLocation

    fun setLocation(input: RecentLocation) {
        _recentLocation.postValue(input)
    }

}