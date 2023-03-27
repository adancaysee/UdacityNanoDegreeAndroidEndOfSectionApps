package com.udacity.politicalpreparedness.domain.representative

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.udacity.politicalpreparedness.databinding.FragmentRepresentativesBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class RepresentativesFragment : Fragment() {

    private lateinit var binding:FragmentRepresentativesBinding

    private val viewModel: RepresentativesViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRepresentativesBinding.inflate(inflater)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }
}