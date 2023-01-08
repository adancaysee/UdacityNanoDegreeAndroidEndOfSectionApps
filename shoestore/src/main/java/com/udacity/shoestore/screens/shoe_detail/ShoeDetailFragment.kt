package com.udacity.shoestore.screens.shoe_detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.udacity.shoestore.R
import com.udacity.shoestore.databinding.FragmentShoeDetailBinding
import com.udacity.shoestore.viewmodel.MainActivityViewModel

class ShoeDetailFragment : Fragment() {

    private lateinit var binding:FragmentShoeDetailBinding

    private val mainViewModel by activityViewModels<MainActivityViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_shoe_detail,container,false)

        binding.mainViewModel = mainViewModel

        binding.lifecycleOwner = this

        binding.btnCancel.setOnClickListener {
            findNavController().navigateUp()
        }


        mainViewModel.eventAddNewShoeFinish.observe(viewLifecycleOwner) {uiState->
            if (uiState == null)  return@observe
            if (uiState.isSuccess) {
                findNavController().navigate(ShoeDetailFragmentDirections.actionShoeDetailDestToShoeListDest())
            }else{
                Toast.makeText(requireActivity(),getString(R.string.error_msg_input_handling),Toast.LENGTH_LONG).show()
            }
            mainViewModel.onEventAddNewShoeFinishCompleted()
        }
        return binding.root
    }

}