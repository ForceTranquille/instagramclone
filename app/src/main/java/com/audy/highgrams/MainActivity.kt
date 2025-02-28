package com.audy.highgrams

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.audy.highgrams.fragments.HomeFragment
import com.audy.highgrams.fragments.NotificationsFragment
import com.audy.highgrams.fragments.ProfileFragment
import com.audy.highgrams.fragments.SearchFragment
import com.audy.highgrams.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Listener for bottom navigation item selections
    private val onNavigationItemSelectedListener = NavigationBarView.OnItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_home -> {
                moveToFragment(HomeFragment())
                return@OnItemSelectedListener true
            }
            R.id.nav_search -> {
                moveToFragment(SearchFragment())
                return@OnItemSelectedListener true
            }
            R.id.nav_add_post -> {
                // Handle the add post action here, for example, open a dialog or a new activity
                item.isChecked = false
                return@OnItemSelectedListener true
            }
            R.id.nav_notifications -> {
                moveToFragment(NotificationsFragment())
                return@OnItemSelectedListener true
            }
            R.id.nav_profile -> {
                moveToFragment(ProfileFragment())
                return@OnItemSelectedListener true
            }
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
