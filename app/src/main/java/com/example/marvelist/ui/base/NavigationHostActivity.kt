package com.example.marvelist.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.marvelist.R
import com.example.marvelist.databinding.NavigationMainLayoutBinding
import com.example.marvelist.injection.InjectionProvider
import com.example.marvelist.injection.components.ActivityComponent
import com.example.marvelist.injection.components.DaggerActivityComponent

class NavigationHostActivity : AppCompatActivity(), InjectionProvider {
    private lateinit var viewBinding: NavigationMainLayoutBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    // Create base dagger component that will provide dependencies to fragments hosted in this activity.
    override val component: ActivityComponent by lazy {
        DaggerActivityComponent.builder().build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize the view binding for this activity.
        viewBinding = NavigationMainLayoutBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        setupDrawerNavigation(viewBinding)
    }

    /**
     * Setup the navigation drawer and link it to the Navigation component library.
     *
     * @param binding view binding to access any relevant children Views.
     */
    private fun setupDrawerNavigation(binding: NavigationMainLayoutBinding) {
        // Retrieve the navigation controller.
        navController =
            NavHostFragment.findNavController(supportFragmentManager.findFragmentById(R.id.main_nav_host_frag)!!)

        // Link the navigation graph to the DrawerLayout and assign the navController to the NavigationView.
        appBarConfiguration = AppBarConfiguration(navController.graph, viewBinding.mainDrawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.mainNavView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        val drawerLayout = viewBinding.mainDrawerLayout

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
