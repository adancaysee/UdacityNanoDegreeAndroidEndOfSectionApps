package com.udacity.locationreminder.savereminder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.udacity.locationreminder.base.BaseFragment
import com.udacity.locationreminder.databinding.FragmentSaveReminderBinding
import org.koin.android.ext.android.inject

class SaveReminderFragment : BaseFragment() {

    private lateinit var binding: FragmentSaveReminderBinding
    override val viewModel: SaveReminderViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSaveReminderBinding.inflate(inflater)
        return binding.root
    }
}