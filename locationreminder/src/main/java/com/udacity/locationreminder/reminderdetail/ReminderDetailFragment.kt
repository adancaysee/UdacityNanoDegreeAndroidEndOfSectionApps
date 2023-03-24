package com.udacity.locationreminder.reminderdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.udacity.locationreminder.databinding.FragmentReminderDetailBinding

class ReminderDetailFragment : Fragment() {

    private lateinit var binding: FragmentReminderDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReminderDetailBinding.inflate(inflater)
        return binding.root
    }
}