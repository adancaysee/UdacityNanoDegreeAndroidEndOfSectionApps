package com.udacity.locationreminder.domain.reminderlist

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.udacity.locationreminder.R
import com.udacity.locationreminder.base.BaseFragment
import com.udacity.locationreminder.databinding.FragmentRemindersListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReminderListFragment : BaseFragment() ,MenuProvider{

    private lateinit var binding: FragmentRemindersListBinding
    override val viewModel: ReminderListViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val host: MenuHost = requireActivity()
        host.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        binding = FragmentRemindersListBinding.inflate(inflater)

        return binding.root
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.main_menu,menu)
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


}