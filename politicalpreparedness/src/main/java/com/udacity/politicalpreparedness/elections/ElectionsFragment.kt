package com.udacity.politicalpreparedness.elections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.udacity.politicalpreparedness.databinding.FragmentElectionsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ElectionsFragment : Fragment() {

    private lateinit var binding: FragmentElectionsBinding

    private val viewModel: ElectionsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentElectionsBinding.inflate(inflater)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val electionListAdapter = ElectionListAdapter(ElectionListAdapter.ElectionListener {
            viewModel.navigateToVoterInfo(it)
        })
        binding.electionsRecyclerView.adapter = electionListAdapter

        val savedElectionListAdapter = ElectionListAdapter(ElectionListAdapter.ElectionListener {
            viewModel.navigateToVoterInfo(it)
        })
        binding.savedElectionsRecyclerView.adapter = savedElectionListAdapter

        viewModel.upcomingElectionList.observe(viewLifecycleOwner) {
            it?.let {
                electionListAdapter.submitList(it)
            }
        }

        viewModel.savedElectionList.observe(viewLifecycleOwner) {
            it?.let {
                savedElectionListAdapter.submitList(it)
            }
        }

        viewModel.navigateToVoterInfoEvent.observe(viewLifecycleOwner) {
            findNavController().navigate(ElectionsFragmentDirections.actionToVoterInfoDestination(it.id,it.division))
        }

        return binding.root
    }
}