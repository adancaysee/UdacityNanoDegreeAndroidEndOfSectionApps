package com.udacity.loadapp.main

import android.app.DownloadManager
import android.content.IntentFilter
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.udacity.loadapp.R
import com.udacity.loadapp.databinding.FragmentMainBinding
import com.udacity.loadapp.util.DownloadCompleteReceiver

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }
    private lateinit var binding: FragmentMainBinding

    private val receiver = DownloadCompleteReceiver()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater)

        binding.viewModel = viewModel

        requireActivity().registerReceiver(
            receiver,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )

        viewModel.radioGroupList.observe(viewLifecycleOwner) {
            it?.let {
                createRadioGroupList(it)
            }
        }

        viewModel.emptySelectionEvent.observe(viewLifecycleOwner) {
            if (it == true) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.empty_selection_warning),
                    Toast.LENGTH_LONG
                ).show()
                viewModel.doneEmptySelectionEvent()
            }
        }

        return binding.root
    }

    private fun createRadioGroupList(list: List<DownloadInfo>) {
        for ((index, info) in list.withIndex()) {
            val button = RadioButton(requireContext())
            button.text = info.title
            button.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                resources.getDimension(R.dimen.default_text)
            )
            button.id = index
            button.setPadding(0, 0, 0, resources.getDimension(R.dimen.spacing_16dp).toInt())
            binding.radioGroup.addView(button)
        }
    }
}