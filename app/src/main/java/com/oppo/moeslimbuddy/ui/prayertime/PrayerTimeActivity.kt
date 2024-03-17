package com.oppo.moeslimbuddy.ui.prayertime

import android.content.Intent
import android.location.Geocoder
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.oppo.moeslimbuddy.data.source.remote.request.ReqPrayerTime
import com.oppo.moeslimbuddy.databinding.ActivityPrayerTimeBinding
import com.oppo.moeslimbuddy.domain.model.PrayTime
import com.oppo.moeslimbuddy.domain.model.RecentLocation
import com.oppo.moeslimbuddy.ui.base.BaseActivity
import com.oppo.moeslimbuddy.util.RecyclerViewUtils
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
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
    private val viewModel: PrayTimeViewModel by viewModel()
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
        viewModel.recentLocation.observe(this) {
            binding.address.text = buildString {
                append(it?.locality)
                append(", ")
                append(it?.subAdminArea)
            }
        }
        viewModel.prayTimeList.observe(this) {
            prayTimeAdapter.setData(it)
        }
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
                    viewModel.setLocation(
                        RecentLocation(
                            address.latitude,
                            address.longitude,
                            0.0,
                            address.locality,
                            address.subAdminArea,
                            System.currentTimeMillis()
                        )
                    )
                }
            }
            val request = ReqPrayerTime(
                now,
                location.latitude,
                location.longitude,
                null
            )
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.getPrayerTime(request)
                        .collect {
                            viewModel.setPrayTimeList(
                                it.data?.timings?.getAsPrayTimeList ?: emptyList()
                            )
                            Timber.d("DEBUG FADHIL: data = ${it.data}")
                        }
                }
            }
        }
    }

}