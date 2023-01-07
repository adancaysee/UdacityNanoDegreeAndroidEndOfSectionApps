package com.udacity.shoestore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.udacity.shoestore.databinding.ActivityMainBinding
import timber.log.Timber

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        val navController = getNavController()

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.login_destination,
                R.id.shoe_list_dest
            )
        )
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController,appBarConfiguration)
        //binding.toolbar.setupWithNavController(navController,appBarConfiguration)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            Timber.tag("navigation").i(destination.label.toString())
        }


    }

    private fun getNavController(): NavController {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        return navHostFragment.navController
    }

    override fun onSupportNavigateUp(): Boolean {
        return getNavController().navigateUp()
    }
}