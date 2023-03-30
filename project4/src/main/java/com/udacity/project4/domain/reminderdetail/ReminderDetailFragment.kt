package com.udacity.project4.domain.reminderdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.udacity.project4.R
import com.udacity.project4.base.BaseFragment
import com.udacity.project4.data.domain.Reminder
import com.udacity.project4.databinding.FragmentReminderDetailBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReminderDetailFragment : BaseFragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentReminderDetailBinding
    override val viewModel: ReminderDetailViewModel by viewModel()

    private lateinit var mMap: GoogleMap

    private val args: ReminderDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReminderDetailBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        viewModel.setParameters(args.reminderId)

        viewModel.reminder.observe(viewLifecycleOwner) {
            showLocationOnMap(it)
        }

        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        viewModel.findReminder()

    }

    private fun showLocationOnMap(reminder: Reminder) {
        if (!::mMap.isInitialized) return
        reminder.latLng?.let {
            mMap.addMarker(MarkerOptions().position(it).title(reminder.locationSnippet))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 15.5f))
        }

    }
}