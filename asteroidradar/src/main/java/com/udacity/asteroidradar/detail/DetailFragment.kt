package com.udacity.asteroidradar.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.udacity.asteroidradar.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentDetailBinding.inflate(inflater, container, false)

        val args = DetailFragmentArgs.fromBundle(requireArguments())
        val selectedAsteroid = args.selectedAsteroid

        val detailViewModel = ViewModelProvider(
            this,
            DetailViewModel.createFactory(selectedAsteroid)
        )[DetailViewModel::class.java]

        binding.viewModel = detailViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }
}