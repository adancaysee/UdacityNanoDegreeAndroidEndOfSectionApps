package com.udacity.locationreminder.domain.reminderdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.udacity.locationreminder.base.BaseFragment
import com.udacity.locationreminder.databinding.FragmentReminderDetailBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReminderDetailFragment : BaseFragment() {

    private lateinit var binding: FragmentReminderDetailBinding
    override val viewModel: ReminderDetailViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReminderDetailBinding.inflate(inflater)
        return binding.root
    }
}