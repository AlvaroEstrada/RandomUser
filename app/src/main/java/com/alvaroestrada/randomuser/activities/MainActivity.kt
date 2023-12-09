package com.alvaroestrada.randomuser.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.alvaroestrada.randomuser.R
import com.alvaroestrada.randomuser.databinding.ActivityMainBinding
import com.alvaroestrada.randomuser.extensions.setStartDestination
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initNavigation()
    }

    override fun onResume() {
        super.onResume()
        navController.setStartDestination()
    }

    private fun initNavigation() {
        (supportFragmentManager.findFragmentById(R.id.navHostFragment) as? NavHostFragment)?.let {
            navController = it.navController
        }
    }
}