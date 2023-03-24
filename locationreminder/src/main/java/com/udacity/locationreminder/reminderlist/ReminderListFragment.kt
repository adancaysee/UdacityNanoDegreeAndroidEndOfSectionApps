package com.udacity.locationreminder.reminderlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.udacity.locationreminder.databinding.FragmentRemindersListBinding

class ReminderListFragment : Fragment() {

    private lateinit var binding: FragmentRemindersListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRemindersListBinding.inflate(inflater)

        return binding.root
    }
}