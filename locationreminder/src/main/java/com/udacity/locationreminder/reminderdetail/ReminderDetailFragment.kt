package com.udacity.locationreminder.reminderdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.udacity.locationreminder.base.BaseFragment
import com.udacity.locationreminder.databinding.FragmentReminderDetailBinding

class ReminderDetailFragment : BaseFragment() {

    private lateinit var binding: FragmentReminderDetailBinding
    override val viewModel: ReminderDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReminderDetailBinding.inflate(inflater)
        return binding.root
    }
}