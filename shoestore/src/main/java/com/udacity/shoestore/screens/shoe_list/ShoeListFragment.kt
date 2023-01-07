package com.udacity.shoestore.screens.shoe_list

import android.os.Bundle
import android.view.*
import android.widget.ScrollView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.udacity.shoestore.R
import com.udacity.shoestore.databinding.FragmentShoeListBinding
import com.udacity.shoestore.databinding.ItemShoeBinding
import com.udacity.shoestore.model.Shoe
import com.udacity.shoestore.screens.login.LoginFragmentDirections
import com.udacity.shoestore.viewmodel.MainActivityViewModel


class ShoeListFragment : Fragment(), MenuProvider {

    private lateinit var binding: FragmentShoeListBinding

    private val mainViewModel by activityViewModels<MainActivityViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val host: MenuHost = requireActivity()
        host.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shoe_list, container, false)

        binding.fbAddNewItem.setOnClickListener {
            findNavController().navigate(ShoeListFragmentDirections.actionShoeListDestToShoeDetailDest())
        }

        mainViewModel.shoeList.observe(viewLifecycleOwner) { shoeList ->
            displayShoeList(shoeList)
        }

        return binding.root
    }

    private fun displayShoeList(shoeList: List<Shoe>) {
        binding.invalidateAll()
        binding.layoutShoeList.removeAllViews()
        for (shoe in shoeList) {
            displayShoeItem(shoe)
        }
        binding.scrollView.post { binding.scrollView.fullScroll(ScrollView.FOCUS_DOWN) }
    }

    private fun displayShoeItem(shoe: Shoe) {
        val itemViewBinding = ItemShoeBinding.inflate(LayoutInflater.from(requireActivity()))
        itemViewBinding.shoe = shoe
        binding.layoutShoeList.addView(itemViewBinding.root)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.logout_menu, menu)
    }

    /**
     * I used global action here. Because when I used onNavDestinationSelected shoe_list_dest was kept in back stack
     */
    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        findNavController().navigate(LoginFragmentDirections.actionGlobalOnboardingGraph())
        return true
        //return NavigationUI.onNavDestinationSelected(menuItem,requireView().findNavController())
    }
}