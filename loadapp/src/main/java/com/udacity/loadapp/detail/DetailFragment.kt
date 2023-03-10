package com.udacity.loadapp.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.udacity.loadapp.databinding.FragmentDetailBinding
import com.udacity.loadapp.util.Constant
import com.udacity.loadapp.util.cancelAllNotifications
import com.udacity.loadapp.util.getNotificationManager

class DetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentDetailBinding.inflate(inflater)

        getNotificationManager(requireContext()).cancelAllNotifications()
        if (arguments != null) {
            val fileName = requireArguments().getString(Constant.FILE_NAME)
            val status = requireArguments().getString(Constant.DOWNLOAD_STATUS)
            binding.filenameText.text = fileName
            binding.statusText.text = status
        }

        binding.btnOk.setOnClickListener {
            findNavController().navigate(DetailFragmentDirections.actionDetailFragmentToMainFragment())
        }

        return binding.root
    }
}