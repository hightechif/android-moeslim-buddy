package com.oppo.moeslimbuddy.ui.qibla

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.oppo.moeslimbuddy.domain.model.RecentLocation

class QiblaViewModel : ViewModel() {

    private var _recentLocation = MutableLiveData<RecentLocation?>(null)
    val recentLocation: LiveData<RecentLocation?> = _recentLocation

    fun setLocation(input: RecentLocation) {
        _recentLocation.postValue(input)
    }

}