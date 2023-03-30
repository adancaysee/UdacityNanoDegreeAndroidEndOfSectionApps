package com.udacity.project4.base

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.udacity.project4.util.showSnackbar

abstract class BaseFragment : Fragment() {

    abstract val viewModel: BaseViewModel

    override fun onStart() {
        super.onStart()

        viewModel.showSnackBarEvent.observe(this) { text ->
            requireView().showSnackbar(text)
        }

        viewModel.showSnackBarIntEvent.observe(this) { resourceId ->
            requireView().showSnackbar(getString(resourceId))
        }

        viewModel.showToastEvent.observe(this) { text ->
            Toast.makeText(activity, text, Toast.LENGTH_LONG).show()
        }

        viewModel.showErrorMessageEvent.observe(this) { message ->
            Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
        }

        viewModel.navigationCommandEvent.observe(this) { command ->
            when (command) {
                is NavigationCommand.To -> findNavController().navigate(command.directions)
                is NavigationCommand.Back -> findNavController().popBackStack()
                is NavigationCommand.BackTo -> findNavController().popBackStack(
                    command.destinationId,
                    false
                )
            }
        }


    }


}