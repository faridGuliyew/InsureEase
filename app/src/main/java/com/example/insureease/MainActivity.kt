package com.example.insureease

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.insureease.databinding.ActivityMainBinding
import com.example.insureease.mainFragments.AddFragment
import com.example.insureease.mainFragments.HomeFragment
import com.example.insureease.mainFragments.HomeFragmentDirections
import com.example.insureease.mainFragments.ProfileFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFloatingActionButton(binding.floatingActionBar)
        setBottomNav()
    }

    private fun setFloatingActionButton (fab : FloatingActionButton){
        fab.setOnClickListener {
            findNavController(R.id.fragmentContainerView2).navigate(HomeFragmentDirections.actionHomeFragmentToAddFragment())
        }
    }
    private fun setBottomNav(){
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment
        NavigationUI.setupWithNavController(binding.bottomNav,navHostFragment.navController)
    }

    fun disableFab(){
       binding.floatingActionBar.isClickable = false
    }
    fun enableFab(){
        binding.floatingActionBar.isClickable = true
    }
}