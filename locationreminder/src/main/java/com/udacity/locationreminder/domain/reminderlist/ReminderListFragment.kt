package com.udacity.locationreminder.domain.reminderlist

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.google.android.material.snackbar.Snackbar
import com.udacity.locationreminder.BuildConfig
import com.udacity.locationreminder.R
import com.udacity.locationreminder.base.BaseFragment
import com.udacity.locationreminder.databinding.FragmentRemindersListBinding
import com.udacity.locationreminder.util.hasPermission
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class ReminderListFragment : BaseFragment(), MenuProvider {

    private lateinit var binding: FragmentRemindersListBinding
    override val viewModel: ReminderListViewModel by viewModel()

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
        binding = FragmentRemindersListBinding.inflate(inflater)

        binding.addReminderFab.setOnClickListener {
            navigateToAddNewReminder()
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        if (!checkAllLocationPermissions()) {
            requestLocationPermissions()
        } else {
            requestNotificationPermission()
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.main_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == R.id.logout) {
            logout()
            return true
        }
        return false
    }

    private fun logout() {
        AuthUI.getInstance()
            .signOut(requireContext())
            .addOnCompleteListener {
                findNavController().navigate(ReminderListFragmentDirections.actionGlobalLaunchDestination())
            }
    }

    private fun navigateToAddNewReminder() {
        if (checkAllLocationPermissions()) {
            findNavController().navigate(ReminderListFragmentDirections.actionSaveReminder())
        } else {
            requestLocationPermissions()
        }
    }

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
            requestNotificationPermission()
            return
        }
        when {
            requireActivity().hasPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION) -> {
                requestNotificationPermission()
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

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
            && !requireContext().hasPermission(Manifest.permission.POST_NOTIFICATIONS)
        ) {
            requestPermissionLauncher.launch(arrayOf(Manifest.permission.POST_NOTIFICATIONS))
        }
    }

}