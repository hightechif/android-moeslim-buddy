//package com.oppo.moeslimbuddy.ui.prayertime
//
//import android.os.CountDownTimer
//import androidx.lifecycle.MutableLiveData
//import java.text.SimpleDateFormat
//import java.util.Calendar
//
//class PrayTimeViewModel : BaseViewModel<PraytimeNavigator>(dataManager) {
//        override fun onEvent(obj: Any) {
//        }
//
//        var prayerTime = MutableLiveData(Prefs.praytime)
//
//        var newtimer: CountDownTimer? = null
//
//        val currentTime = ObservableField("")
//        val address = ObservableField(Prefs.userCity)
//        val hijriDate = ObservableField(getHijriDate())
//        val gregoryDate = ObservableField(dataManager.getGregorianDate())
//        val dates = ObservableField("${dataManager.getGregorianDate()}/${dataManager.getHijriDate()}")
//
//        val isDeviceHasCompass = ObservableField(false)
//
//        var textPrayerTime = ObservableField("Loading...")
//        var textUntil = ObservableField("Loading...")
//
//        var untilFajr = MutableLiveData("")
//        var untilDhuhr = MutableLiveData("")
//        var untilAsr = MutableLiveData("")
//        var untilMaghrib = MutableLiveData("")
//        var untilIsya = MutableLiveData("")
//
//        var praytimeFajr = MutableLiveData("")
//        var praytimeDhuhr = MutableLiveData("")
//        var praytimeAsr = MutableLiveData("")
//        var praytimeMaghrib = MutableLiveData("")
//        var praytimeIsya = MutableLiveData("")
//
//        var ringFajr = MutableLiveData(Prefs.ringFajr)
//        var ringDhuhr = MutableLiveData(Prefs.ringDhuhr)
//        var ringAsr = MutableLiveData(Prefs.ringAsr)
//        var ringMaghrib = MutableLiveData(Prefs.ringMaghrib)
//        var ringIsya = MutableLiveData(Prefs.ringIsya)
//
//        fun buildPrayers(prayer: PrayerTime) {
//            untilFajr.value =
//                PrayerTimeHelper.countTimeLight(
//                    prayer.fajr ?: "",
//                    LocaleProvider.getInstance().getString(LocaleConstants.FAJR)
//                )
//            praytimeFajr.value = prayer.fajr
//
//            untilDhuhr.value =
//                PrayerTimeHelper.countTimeLight(
//                    prayer.dhuhr ?: "",
//                    LocaleProvider.getInstance().getString(LocaleConstants.DHUHR)
//                )
//            praytimeDhuhr.value = prayer.dhuhr
//
//            untilAsr.value =
//                PrayerTimeHelper.countTimeLight(
//                    prayer.asr ?: "",
//                    LocaleProvider.getInstance().getString(LocaleConstants.ASR)
//                )
//            praytimeAsr.value = prayer.asr
//
//            untilMaghrib.value =
//                PrayerTimeHelper.countTimeLight(
//                    prayer.maghrib ?: "",
//                    LocaleProvider.getInstance().getString(LocaleConstants.MAGHRIB)
//                )
//            praytimeMaghrib.value = prayer.maghrib
//
//            untilIsya.value =
//                PrayerTimeHelper.countTimeLight(
//                    prayer.isya ?: "",
//                    LocaleProvider.getInstance().getString(LocaleConstants.ISYA)
//                )
//            praytimeIsya.value = prayer.isya
//
//        }
//
//        fun onClickQibla(){
//            navigator?.onClickQibla()
//        }
//
//        fun configureTicker() {
//            newtimer?.cancel()
//            newtimer = tick(Long.MAX_VALUE, 1000) {
//                buildPrayers(Prefs.praytime)
//                currentTime.set(SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().time))
//                textPrayerTime.set(Prefs.praytime.getCurrentPrayerTimeString())
//                textUntil.set(Prefs.praytime.getTimeUntilNextPrayerString())
//            }
//            newtimer?.start()
//        }
//}