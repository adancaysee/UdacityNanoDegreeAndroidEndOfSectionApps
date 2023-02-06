package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.repository.NEoWsAsteroidFilter

class MainFragment : Fragment(), MenuProvider {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this, MainViewModel.Factory)[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val host: MenuHost = requireActivity()
        host.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        val binding = FragmentMainBinding.inflate(inflater, container, false)

        val adapter = AsteroidListAdapter(AsteroidListAdapter.ItemClickListener {
            viewModel.onNavigateToDetail(it)
        })

        viewModel.asteroids.observe(viewLifecycleOwner) {
            it?.let {
                adapter.submitList(it)
            }
        }

        binding.asteroidRecycler.adapter = adapter

        viewModel.navigateToDetailEvent.observe(viewLifecycleOwner) {
            it?.let {
                findNavController().navigate(MainFragmentDirections.actionToDetailDestination(it))
                viewModel.doneNavigatingToDetail()
            }
        }

        binding.mainViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.main_overflow_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.show_week_asteroids -> {
                viewModel.filterAsteroids(NEoWsAsteroidFilter.SHOW_WEEKLY)
            }
            R.id.show_today_asteroids -> {
                viewModel.filterAsteroids(NEoWsAsteroidFilter.SHOW_TODAY)
            }
            R.id.show_saved_asteroids -> {
                viewModel.filterAsteroids(NEoWsAsteroidFilter.SHOW_ALL)
            }
        }
        return true
    }
}