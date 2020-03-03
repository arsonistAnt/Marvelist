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
import com.example.marvelist.injection.modules.ContextModule
import timber.log.Timber

/**
 * The Main activity that hosts all relevant fragments in the application. Contains
 * a singleton [ActivityComponent] that provides any needed dependencies to the
 * fragments that are hosted.
 */
class NavigationHostActivity : AppCompatActivity(), InjectionProvider {
    private lateinit var viewBinding: NavigationMainLayoutBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    // Create base dagger component that will provide dependencies to fragments hosted in this activity.
    override val component: ActivityComponent by lazy {
        val contextModule = ContextModule(this)
        DaggerActivityComponent.builder()
            .contextModule(contextModule).build()
    }

    /**
     * Initialize any base view components.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize timber logs
        Timber.plant(Timber.DebugTree())
        // Initialize the view binding and inflate the layout.
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

    /**
     * Allow the Navigation component to handle the callback otherwise
     * the super method will handle it.
     */
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    /**
     * Have the back button close the navigation drawer if it is open.
     */
    override fun onBackPressed() {
        val drawerLayout = viewBinding.mainDrawerLayout

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
