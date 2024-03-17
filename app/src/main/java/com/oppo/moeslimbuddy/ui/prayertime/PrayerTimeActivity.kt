package com.oppo.moeslimbuddy.ui.prayertime

import android.content.Intent
import android.location.Geocoder
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.oppo.moeslimbuddy.databinding.ActivityPrayerTimeBinding
import com.oppo.moeslimbuddy.domain.model.PrayTime
import com.oppo.moeslimbuddy.domain.model.RecentLocation
import com.oppo.moeslimbuddy.ui.base.BaseActivity
import com.oppo.moeslimbuddy.util.RecyclerViewUtils
import java.time.LocalDateTime
import java.util.Locale

class PrayerTimeActivity : BaseActivity() {
    companion object {
        private const val EXTRA_LOCATION = "EXTRA_LOCATION"
        fun open(activity: FragmentActivity, location: RecentLocation?) {
            val intent = Intent(activity, PrayerTimeActivity::class.java)
            val locationAsJson = Gson().toJson(location)
            if (location != null) intent.putExtra(EXTRA_LOCATION, locationAsJson)
            ActivityCompat.startActivity(activity, intent, null)
        }
    }

    private lateinit var binding: ActivityPrayerTimeBinding
    private var prayTimeAdapter = PrayTimeAdapter()

    override fun setupView() {
        binding = ActivityPrayerTimeBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                val toast = Toast.makeText(this@PrayerTimeActivity, "clicked", Toast.LENGTH_SHORT)
                toast.show()
            }

            override fun onDraw(t: PrayTime?, position: Int, viewBinding: ViewBinding) {
            }

        }
    }

    override fun setupObserver() {

    }

    override fun initData() {
        val now = LocalDateTime.now()
        binding.textTime.text = String.format("%s:%s", now.hour, now.minute)
        val locationAsJson = intent.getStringExtra(EXTRA_LOCATION)
        if (locationAsJson != null) {
            val location = Gson().fromJson<RecentLocation>(
                locationAsJson,
                object : TypeToken<RecentLocation>() {}.type
            )
            Geocoder(this, Locale.getDefault()).apply {
                getFromLocation(location.latitude, location.longitude, 1)?.first()?.let { address ->
                    binding.address.text = buildString {
                        append(address.locality)
                        append(", ")
                        append(address.subAdminArea)
                    }
                }
            }
        }
        prayTimeAdapter.setData(
            mutableListOf(
                PrayTime("Imsak", "04:30"),
                PrayTime("Fajr", "04:40"),
                PrayTime("Dhuhr", "12:03"),
                PrayTime("Asr", "15:09"),
                PrayTime("Maghrib", "18:08"),
                PrayTime("Isya", "19:17"),
            )
        )
    }

}