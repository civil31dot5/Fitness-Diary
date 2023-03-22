package com.civil31dot5.fitnessdiary.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.civil31dot5.fitnessdiary.NavGraphDirections
import com.civil31dot5.fitnessdiary.R
import com.civil31dot5.fitnessdiary.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fl_container) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(
                R.id.homeFragment,
                R.id.dietRecordHistoryFragment
            ),
            binding.drawerLayout
        )

//        val appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawerLayout)

        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(getIntent())
    }

    private fun handleIntent(intent: Intent){
        val toRecord = intent.getStringExtra("to_record")
        if (toRecord == "diet"){
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.fl_container) as NavHostFragment
            val navController = navHostFragment.navController
            navController.navigate(NavGraphDirections.actionGlobalAddDietRecordFragment())
        }
    }

}