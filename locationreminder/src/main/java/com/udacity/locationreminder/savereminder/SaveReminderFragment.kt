package com.udacity.locationreminder.savereminder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.udacity.locationreminder.base.BaseFragment
import com.udacity.locationreminder.databinding.FragmentSaveReminderBinding

class SaveReminderFragment : BaseFragment() {

    private lateinit var binding: FragmentSaveReminderBinding
    override val viewModel: SaveReminderViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSaveReminderBinding.inflate(inflater)
        return binding.root
    }
}