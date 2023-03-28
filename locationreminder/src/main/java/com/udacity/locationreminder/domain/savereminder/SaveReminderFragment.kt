package com.udacity.locationreminder.domain.savereminder

import android.content.IntentSender
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.ResolvableApiException
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
        if (activityResult.resultCode == AppCompatActivity.RESULT_OK) {
            Timber.d("Ok")
        } else {
            showLocationSettingSnackbar()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSaveReminderBinding.inflate(inflater)

        listenLocationSettingRequestResponse()

        binding.selectLocation.setOnClickListener {
            viewModel.checkDeviceLocationSettingsAndFindMyLocation()
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