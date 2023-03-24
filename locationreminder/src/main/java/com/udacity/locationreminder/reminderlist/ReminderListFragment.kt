package com.udacity.locationreminder.reminderlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.udacity.locationreminder.base.BaseFragment
import com.udacity.locationreminder.databinding.FragmentRemindersListBinding

class ReminderListFragment : BaseFragment() {

    private lateinit var binding: FragmentRemindersListBinding
    override val viewModel: ReminderListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRemindersListBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
    }
}