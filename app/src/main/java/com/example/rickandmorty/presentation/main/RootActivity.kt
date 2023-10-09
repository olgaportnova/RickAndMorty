package com.example.rickandmorty.presentation.main

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.rickandmorty.R
import com.example.rickandmorty.databinding.ActivityRootBinding
import com.example.rickandmorty.presentation.episodes.view.EpisodesListFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class RootActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRootBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.container_view) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView) // или используйте findViewById для фрагмента
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.charactersListFragment -> {
                    navController.navigate(R.id.charactersListFragment)
                    true
                }
                R.id.locationsListFragment -> {
                    navController.navigate(R.id.locationsListFragment)
                    true
                }
                R.id.episodesListFragment -> {
                    navController.navigate(R.id.episodesListFragment)
                    true
                }
                else -> false
            }
        }


    }



    override fun onResume() {
        super.onResume()

        val appOps = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), packageName)
        val granted = mode == AppOpsManager.MODE_ALLOWED

        if (!granted) {
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            startActivity(intent)
        }
    }




}
