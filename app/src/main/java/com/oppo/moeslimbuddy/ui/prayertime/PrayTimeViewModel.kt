package com.oppo.moeslimbuddy.ui.prayertime

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oppo.moeslimbuddy.data.Result
import com.oppo.moeslimbuddy.data.source.remote.request.ReqPrayerTime
import com.oppo.moeslimbuddy.domain.model.PrayTime
import com.oppo.moeslimbuddy.domain.model.RecentLocation
import com.oppo.moeslimbuddy.domain.usecase.PrayerTimeUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class PrayTimeViewModel(
    private val useCase: PrayerTimeUseCase
) : ViewModel() {

    private var _recentLocation = MutableLiveData<RecentLocation?>(null)
    val recentLocation: LiveData<RecentLocation?> = _recentLocation

    fun setLocation(input: RecentLocation) {
        _recentLocation.postValue(input)
    }

    private var _prayTimeList = MutableLiveData<List<PrayTime>>(listOf())
    val prayTimeList: LiveData<List<PrayTime>> = _prayTimeList

    fun setPrayTimeList(input: List<PrayTime>) {
        _prayTimeList.postValue(input)
    }

    fun getPrayerTime(request: ReqPrayerTime) =
        useCase.getPrayerTime(request)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(100L), Result.Loading())

}