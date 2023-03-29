package com.udacity.locationreminder.domain.savereminder

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.udacity.locationreminder.R
import com.udacity.locationreminder.base.BaseFragment
import com.udacity.locationreminder.databinding.FragmentSelectLocationBinding
import com.udacity.locationreminder.data.domain.Location
import com.udacity.locationreminder.util.map.GeofenceHelper
import org.koin.android.ext.android.inject

class SelectLocationFragment : BaseFragment(), OnMapReadyCallback, MenuProvider {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: FragmentSelectLocationBinding
    override val viewModel: SaveReminderViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val host: MenuHost = requireActivity()
        host.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        binding = FragmentSelectLocationBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        viewModel.showCurrentLocationEvent.observe(viewLifecycleOwner) {
            it?.let {
                moveCamera(LatLng(it.latitude, it.longitude))
            }
        }

        viewModel.navigateGeocodeDetailEvent.observe(viewLifecycleOwner) {
            findNavController().navigate(SelectLocationFragmentDirections.actionSelectLocationDestinationToSaveReminderDestination())
        }

        return binding.root
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.isMyLocationEnabled = true

        mMap.setOnMapClickListener {
            clearMap()
            addGeofenceMarker(mMap, it)
            viewModel.getGeocodeLocationInfo(Location(it.latitude, it.longitude))
        }
        val location = viewModel.reminder.value?.location
        if (location != null) {
            addGeofenceMarker(mMap, LatLng(location.latitude, location.longitude))
        } else {
            viewModel.getCurrentLocation()
        }
    }

    private fun addGeofenceMarker(map: GoogleMap, latLng: LatLng) {
        map.addMarker(MarkerOptions().position(latLng))
        val circleOptions = CircleOptions().apply {
            center(latLng)
            radius(GeofenceHelper.GEOFENCE_RADIUS)
            strokeColor(ContextCompat.getColor(requireContext(), android.R.color.transparent))
            fillColor(ContextCompat.getColor(requireContext(), R.color.geofence_circle_fill_color))
        }
        map.addCircle(circleOptions)
        moveCamera(latLng)
    }

    private fun clearMap() {
        if (::mMap.isInitialized) {
            mMap.clear()
            viewModel.clearGeofence()
        }
    }

    private fun moveCamera(latLng: LatLng) {
        mMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                latLng, 16f
            )
        )
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.map_options, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        if (!::mMap.isInitialized) return false
        when (menuItem.itemId) {
            R.id.hybrid_map -> {
                mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                return true
            }
            R.id.satellite_map -> {
                mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                return true
            }
            R.id.terrain_map -> {
                mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                return true
            }
            R.id.normal_map -> {
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                return true
            }
            R.id.clear_map -> {
                clearMap()
                return true
            }
            else -> {
                return false
            }
        }
    }

}