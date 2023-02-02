package com.udacity.asteroidradar.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.udacity.asteroidradar.R
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

        detailViewModel.displayAstronomicalUnitExplanationEvent.observe(viewLifecycleOwner) {
            if (it == true) {
                displayAstronomicalUnitExplanationDialog()
                detailViewModel.doneDisplayAstronomicalUnitExplanation()
            }
        }

        binding.viewModel = detailViewModel
        binding.lifecycleOwner = viewLifecycleOwner


        return binding.root
    }

    private fun displayAstronomicalUnitExplanationDialog() {
        val builder = AlertDialog.Builder(requireActivity())
            .setMessage(getString(R.string.astronomica_unit_explanation))
            .setPositiveButton(android.R.string.ok, null)
        builder.create().show()
    }
}

