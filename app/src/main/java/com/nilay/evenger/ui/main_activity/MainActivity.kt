package com.nilay.evenger.ui.main_activity

import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.nilay.evenger.R
import com.nilay.evenger.databinding.ActivityMainBinding
import com.nilay.evenger.utils.DrawerState
import com.nilay.evenger.utils.onDestinationChange
import com.nilay.evenger.utils.openCustomChromeTab
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), DrawerState {
    private val binding: ActivityMainBinding by viewBinding()
    private val navHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
    }
    private val navController by lazy {
        navHostFragment.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.apply {
            bottomNavigation.setupWithNavController(navController)
            setUpDrawer()
        }
        onDestinationChange()
    }

    private fun ActivityMainBinding.setUpDrawer() = this.navigationView.apply {
        setupWithNavController(navController)
        setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_vTop -> openCustomChromeTab("https://vtopcc.vit.ac.in/vtop/login")
                R.id.nav_lms -> openCustomChromeTab("https://lms.vit.ac.in/login/index.php")

                // TODO : Change the links and then remove the comments
                R.id.nav_github -> openCustomChromeTab("https://github.com/logicalNil/Evenger_Android")
                R.id.nav_whats_new -> openCustomChromeTab("https://github.com/logicalNil/Evenger_Android/releases")
                R.id.nav_issue -> openCustomChromeTab("https://github.com/logicalNil/Evenger_Android/issues")
                else -> NavigationUI.onNavDestinationSelected(item, navController)
            }
            changeState()
            true
        }
    }

    private fun onDestinationChange() {
        navController.onDestinationChange {
            when (it.id) {
                R.id.eventFragment -> binding.bottomNavigation.isVisible = true
                else -> binding.bottomNavigation.isVisible = false
            }
        }
    }

    override fun changeState() {
        binding.root.apply {
            if (isDrawerOpen(GravityCompat.START)) {
                closeDrawer(GravityCompat.START)
            } else {
                openDrawer(GravityCompat.START)
            }
        }
    }

    override fun setBottomViewVisibility(boolean: Boolean) {
        binding.bottomNavigation.isVisible = boolean
    }
}