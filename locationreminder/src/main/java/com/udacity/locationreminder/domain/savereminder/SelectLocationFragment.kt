package com.udacity.locationreminder.domain.savereminder

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.udacity.locationreminder.BuildConfig
import com.udacity.locationreminder.R
import com.udacity.locationreminder.base.BaseFragment
import com.udacity.locationreminder.data.domain.getLatLng
import com.udacity.locationreminder.data.domain.getLocation
import com.udacity.locationreminder.databinding.FragmentSelectLocationBinding
import com.udacity.locationreminder.util.hasPermission
import org.koin.android.ext.android.inject
import timber.log.Timber

class SelectLocationFragment : BaseFragment(), OnMapReadyCallback, MenuProvider {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: FragmentSelectLocationBinding
    override val viewModel: SaveReminderViewModel by inject()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true &&
                    permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true -> {
                requestBackgroundLocationPermission()
            }
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == false ||
                    permissions[Manifest.permission.ACCESS_FINE_LOCATION] == false ||
                    permissions[Manifest.permission.ACCESS_BACKGROUND_LOCATION] == false -> {
                showOpenPermissionSettingSnackbar()
            }
            permissions[Manifest.permission.ACCESS_BACKGROUND_LOCATION] == true -> {
                Toast.makeText(requireContext(), "All permissions are granted", Toast.LENGTH_LONG)
                    .show()
                Timber.d("All permissions are granted")
            }

        }
    }

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

        viewModel.selectedAddress.observe(viewLifecycleOwner) {
            it?.location?.let { location ->
                addGeofenceMarker(location.getLatLng(), it.getSnippet())
            }
        }
        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        if (checkAllLocationPermissions()) {
            setupCurrentLocation()
        } else {
            requestLocationPermissions()
        }
        mMap.setOnMapClickListener {
            clearMap()
            viewModel.getGeoCodeLocation(it.getLocation())
        }
    }

    @SuppressLint("MissingPermission")
    fun setupCurrentLocation() {
        if (!checkAllLocationPermissions() || !::mMap.isInitialized) return
        mMap.isMyLocationEnabled = true
        viewModel.reminder.value?.let {
            if (it.latLng != null) {
                addGeofenceMarker(it.latLng,it.locationSnippet)
            }else{
                viewModel.initFusedLocationClient()
            }
        } ?: viewModel.initFusedLocationClient()

    }

    private fun addGeofenceMarker(latLng: LatLng, snippet: String?) {
        if (!::mMap.isInitialized) return
        if(latLng.latitude == 0.0 || latLng.longitude == 0.0) return
        mMap.addMarker(MarkerOptions().position(latLng).title(snippet ?: ""))
        /*val circleOptions = CircleOptions().apply {
            center(latLng)
            radius(GeofenceHelper.GEOFENCE_RADIUS)
            strokeColor(ContextCompat.getColor(requireContext(), android.R.color.transparent))
            fillColor(ContextCompat.getColor(requireContext(), R.color.geofence_circle_fill_color))
        }
        mMap.addCircle(circleOptions)*/
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))

    }

    private fun clearMap() {
        if (::mMap.isInitialized) {
            mMap.clear()
            viewModel.clearGeofence()
        }
    }

    //Location permissions
    private fun requestLocationPermissions() {
        when {
            requireActivity().hasPermission(Manifest.permission.ACCESS_FINE_LOCATION) || requireActivity().hasPermission(
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) -> {
                requestBackgroundLocationPermission()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) || shouldShowRequestPermissionRationale(
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                showOpenPermissionSettingSnackbar()
            }
            else -> {
                requestPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                    )
                )
            }
        }
    }

    private fun requestBackgroundLocationPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            setupCurrentLocation()
            return
        }
        when {
            requireActivity().hasPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION) -> {
                setupCurrentLocation()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_BACKGROUND_LOCATION) -> {
                showOpenBackgroundPermissionSnackbar()
            }
            else -> {
                requestPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION))
            }
        }
    }

    private fun checkAllLocationPermissions(): Boolean {
        val isForegroundGranted =
            requireActivity().hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    && requireActivity().hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)

        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            isForegroundGranted
        } else {
            requireActivity().hasPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun showOpenBackgroundPermissionSnackbar() {
        Snackbar.make(
            binding.root,
            R.string.permission_denied_explanation,
            Snackbar.LENGTH_INDEFINITE
        ).apply {
            setAction(R.string.settings) {
                requestPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION))
            }
        }.show()
    }

    private fun showOpenPermissionSettingSnackbar() {
        Snackbar.make(
            binding.root,
            R.string.permission_denied_explanation,
            Snackbar.LENGTH_INDEFINITE
        ).apply {
            setAction(R.string.settings) {
                startActivity(Intent().apply {
                    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                })
            }
        }.show()
    }

    //Menu
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