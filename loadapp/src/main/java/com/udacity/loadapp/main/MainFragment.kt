package com.udacity.loadapp.main

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.udacity.loadapp.R
import com.udacity.loadapp.databinding.FragmentMainBinding


class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }
    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater)

        binding.viewModel = viewModel

        viewModel.radioGroupList.observe(viewLifecycleOwner) {
            it?.let {
                createRadioGroupList(it)
            }
        }

        return binding.root
    }

    private fun createRadioGroupList(list: List<DownloadInfo>) {
        for (info in list) {
            val button = RadioButton(requireContext())
            button.text = info.title
            button.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                resources.getDimension(R.dimen.default_text)
            )
            button.setPadding(0, 0, 0, resources.getDimension(R.dimen.spacing_16dp).toInt())
            binding.radioGroup.addView(button)
        }
    }
}