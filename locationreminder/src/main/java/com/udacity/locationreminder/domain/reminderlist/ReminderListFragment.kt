package com.udacity.locationreminder.domain.reminderlist

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import com.google.android.material.snackbar.Snackbar
import com.udacity.locationreminder.BuildConfig
import com.udacity.locationreminder.R
import com.udacity.locationreminder.base.BaseFragment
import com.udacity.locationreminder.base.NavigationCommand
import com.udacity.locationreminder.databinding.FragmentRemindersListBinding
import com.udacity.locationreminder.util.hasPermission
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReminderListFragment : BaseFragment(), MenuProvider {

    private lateinit var binding: FragmentRemindersListBinding
    override val viewModel: ReminderListViewModel by viewModel()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (!granted) {
            showOpenPermissionSettingSnackbar()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestNotificationPermission()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val host: MenuHost = requireActivity()
        host.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        binding = FragmentRemindersListBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val adapter = RemindersListAdapter(RemindersListAdapter.OnClickListener {
            viewModel.navigateToReminderDetail(NavigationCommand.To(ReminderListFragmentDirections.actionOpenDetail()))
        })
        binding.remindersRecyclerView.adapter = adapter
        viewModel.reminders.observe(viewLifecycleOwner) {
            it?.let { adapter.submitList(it) }
        }

        binding.addReminderFab.setOnClickListener {
            viewModel.navigateToAddReminder(NavigationCommand.To(ReminderListFragmentDirections.actionSaveReminder()))
        }

        return binding.root
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.main_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == R.id.logout) {
            viewModel.logout(NavigationCommand.To(ReminderListFragmentDirections.actionGlobalLaunchDestination()))
            return true
        }
        return false
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
            && !requireContext().hasPermission(Manifest.permission.POST_NOTIFICATIONS)
        ) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun showOpenPermissionSettingSnackbar() {
        Snackbar.make(
            binding.root,
            R.string.notification_permission_denied_explanation,
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

}