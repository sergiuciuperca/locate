package ro.sc.test.locate.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import ro.sc.test.locate.R
import ro.sc.test.locate.databinding.ActivityMainBinding
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ro.sc.test.locate.domain.Home
import ro.sc.test.locate.domain.Login
import ro.sc.test.locate.domain.MainViewState
import ro.sc.test.locate.domain.Onboarding


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private val destinationChangeListener = object : NavController.OnDestinationChangedListener {
        override fun onDestinationChanged(
            controller: NavController,
            destination: NavDestination,
            arguments: Bundle?
        ) {
            when (destination.id) {
                R.id.navigation_onboarding, R.id.navigation_login,
                R.id.navigation_success,  R.id.navigation_splash -> {
                    binding.appBar.visibility = View.GONE
                }
                R.id.navigation_home -> {
                    binding.appBar.visibility = View.GONE
                }
                else -> {
                    binding.appBar.visibility = View.VISIBLE
                    binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_toolbar)
                }
            }

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val metrics = resources.displayMetrics

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        val config = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, config)



        navController.addOnDestinationChangedListener(destinationChangeListener)

        viewModel.stateFlow.onEach { handle(it) }
            .launchIn(lifecycleScope)
    }


    private fun handle(state: MainViewState) {
        when (state.mainNavigation) {
            Home -> {
                navController.navigate(
                    "app:locate://home".toUri(), NavOptions.Builder()
                        .setLaunchSingleTop(true)
                        .setPopUpTo(R.id.nav_graph, true)
                        .build()
                )
            }

            Onboarding -> {
                navController.navigate(
                    "app:locate://onboarding".toUri(), NavOptions.Builder()
                        .setLaunchSingleTop(true)
                        .setPopUpTo(R.id.nav_graph, true)
                        .build()
                )
            }

            Login -> {
                navController.navigate(
                    "app:locate://login".toUri(), NavOptions.Builder()
                        .setLaunchSingleTop(true)
                        .setPopUpTo(R.id.nav_graph, true)
                        .build()
                )
            }
        }
    }

    override fun onNavigateUp(): Boolean {
        return navController.navigateUp()
    }
}