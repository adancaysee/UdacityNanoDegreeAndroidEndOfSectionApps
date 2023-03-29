package com.udacity.locationreminder.domain.savereminder

import android.content.IntentSender
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.google.android.material.snackbar.Snackbar
import com.udacity.locationreminder.R
import com.udacity.locationreminder.base.BaseFragment
import com.udacity.locationreminder.databinding.FragmentSaveReminderBinding
import org.koin.android.ext.android.inject
import timber.log.Timber

class SaveReminderFragment : BaseFragment() {

    private lateinit var binding: FragmentSaveReminderBinding
    override val viewModel: SaveReminderViewModel by inject()

    private val resolutionForResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { activityResult ->
        if (activityResult.resultCode == AppCompatActivity.RESULT_CANCELED) {
            showLocationSettingSnackbar()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSaveReminderBinding.inflate(inflater)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.selectLocation.setOnClickListener {
            checkDeviceLocationSettings()
        }

        return binding.root

    }

    private fun navigateToMap() {
        findNavController().navigate(SaveReminderFragmentDirections.actionSelectLocation())
    }

    private fun checkDeviceLocationSettings() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_LOW_POWER, 10000)
            .build()

        val locationSettingRequest =
            LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
                .build()

        val settingClient = LocationServices.getSettingsClient(requireContext())
        val locationSettingResponseTask =
            settingClient.checkLocationSettings(locationSettingRequest)

        locationSettingResponseTask.addOnCompleteListener {
            if (it.isSuccessful) {
                navigateToMap()
            }
        }.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    resolutionForResultLauncher.launch(
                        IntentSenderRequest.Builder(exception.resolution).build()
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    Timber.d("Error geting location settings resolution: " + sendEx.message)
                }
            } else {
                showLocationSettingSnackbar()
            }
        }
    }

    private fun showLocationSettingSnackbar() {
        Snackbar.make(
            binding.root, R.string.location_required_error, Snackbar.LENGTH_INDEFINITE
        ).apply {
            setAction(R.string.ok) {
                checkDeviceLocationSettings()
            }
        }.show()
    }
}