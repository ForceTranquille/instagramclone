package com.audy.highgrams

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import com.audy.highgrams.databinding.ActivityMainBinding
import com.audy.highgrams.fragments.HomeFragment
import com.audy.highgrams.fragments.NotificationsFragment
import com.audy.highgrams.fragments.ProfileFragment
import com.audy.highgrams.fragments.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    internal var selectedFragment: Fragment? = null

    // Listener for bottom navigation item selections
    private val onNavigationItemSelectedListener =
        NavigationBarView.OnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    selectedFragment = HomeFragment()

                }

                R.id.nav_search -> {
                    selectedFragment = SearchFragment()

                }

                R.id.nav_add_post -> {
                    return@OnItemSelectedListener true
                }

                R.id.nav_notifications -> {
                    selectedFragment = NotificationsFragment()
                }

                R.id.nav_profile -> {
                    selectedFragment = ProfileFragment()
                }
            }
            if (selectedFragment != null)
            {
                supportFragmentManager.beginTransaction().replace(
                    R.id.fragment_container,
                    selectedFragment!!
                ).commit()
            }
            false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate layout using view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get reference to the BottomNavigationView
        val navView: BottomNavigationView = binding.navView

        // Set the navigation item selected listener
        navView.setOnItemSelectedListener(onNavigationItemSelectedListener)

        // Initially, load the HomeFragment
        if (savedInstanceState == null) {
            moveToFragment(HomeFragment())
        }
    }

    // Method to move to the specified fragment
    private fun moveToFragment(fragment: Fragment) {
        // Start fragment transaction
        val fragmentTrans = supportFragmentManager.beginTransaction()

        // Replace the current fragment with the selected one
        fragmentTrans.replace(R.id.fragment_container, fragment)

        // Add the transaction to the back stack so the user can navigate back
        fragmentTrans.addToBackStack(null)

        // Commit the transaction
        fragmentTrans.commit()
    }
}
