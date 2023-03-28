package com.udacity.politicalpreparedness.domain.representative

import android.Manifest
import android.content.Intent
import android.content.IntentSender
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.material.snackbar.Snackbar
import com.udacity.politicalpreparedness.BuildConfig
import com.udacity.politicalpreparedness.R
import com.udacity.politicalpreparedness.databinding.FragmentRepresentativesBinding
import com.udacity.politicalpreparedness.util.hasPermission
import com.udacity.politicalpreparedness.util.isInternetAvailable
import com.udacity.politicalpreparedness.util.showSnackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class RepresentativesFragment : Fragment() {

    private lateinit var binding: FragmentRepresentativesBinding

    private val viewModel: RepresentativesViewModel by viewModel()

    private val resolutionForResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { activityResult ->
        if (activityResult.resultCode == AppCompatActivity.RESULT_OK) {
            viewModel.getLocation()
        } else {
            showLocationSettingSnackbar()
        }
    }

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
                viewModel.checkDeviceLocationSettingsAndFindMyLocation()
            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRepresentativesBinding.inflate(inflater)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner


        val adapter =
            RepresentativeListAdapter(object : RepresentativeListAdapter.RepresentativeListener {
                override fun startIntent(url: String) {
                    val uri = Uri.parse(url)
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                }

            })
        binding.representativesRecyclerView.adapter = adapter

        viewModel.representativeList.observe(viewLifecycleOwner) {
            it?.let {
                adapter.submitList(it)
            }
        }

        viewModel.snackBarMessageEvent.observe(viewLifecycleOwner) {
            binding.root.showSnackbar(it)
        }
        listenLocationSettingRequestResponse()

        binding.buttonLocation.setOnClickListener {
            if (requireContext().isInternetAvailable()) {
                requestForegroundLocationPermissions()
            }else {
                binding.root.showSnackbar(getString(R.string.check_conn))
            }

        }

        return binding.root
    }

    private fun listenLocationSettingRequestResponse() {
        viewModel.checkLocationSettingFailureEvent.observe(viewLifecycleOwner) {
            it?.let { exception ->
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
    }

    private fun requestForegroundLocationPermissions() {
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
        when {
            (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) -> {
                viewModel.checkDeviceLocationSettingsAndFindMyLocation()
            }
            requireActivity().hasPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION) -> {
                viewModel.checkDeviceLocationSettingsAndFindMyLocation()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_BACKGROUND_LOCATION) -> {
                showOpenPermissionSettingSnackbar()
            }
            else -> {
                requestPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION))
            }
        }
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

    private fun showLocationSettingSnackbar() {
        Snackbar.make(
            binding.root,
            R.string.location_required_error,
            Snackbar.LENGTH_INDEFINITE
        ).apply {
            setAction(R.string.ok) {
                viewModel.checkDeviceLocationSettingsAndFindMyLocation()
            }
        }.show()
    }
}