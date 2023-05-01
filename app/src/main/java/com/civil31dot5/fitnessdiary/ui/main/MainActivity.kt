package com.civil31dot5.fitnessdiary.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.civil31dot5.fitnessdiary.NavGraphDirections
import com.civil31dot5.fitnessdiary.R
import com.civil31dot5.fitnessdiary.data.IntentHandler
import com.civil31dot5.fitnessdiary.data.StravaAccountManagerImpl
import com.civil31dot5.fitnessdiary.databinding.ActivityMainBinding
import com.civil31dot5.fitnessdiary.domain.usecase.sport.StravaAccountManager
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), IntentHandler {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var stravaAccountManager: StravaAccountManagerImpl

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
                R.id.dietRecordHistoryFragment,
                R.id.sportHistoryFragment,
                R.id.bodyShapeRecordFragment,
                R.id.reportFragment
            ),
            binding.drawerLayout
        )

        setSupportActionBar(binding.toolbar)

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

    override fun onStart() {
        super.onStart()
        stravaAccountManager.registerIntentHandler(this)
    }

    override fun onStop() {
        super.onStop()
        stravaAccountManager.unregisterIntentHandler()
    }

    private var intentHandlerRequestCode: Int? = null
    private var intentHandlerDataCallback: ((Intent) -> Unit)? = null
    override fun handleIntentForResult(
        intent: Intent,
        requestCode: Int,
        dataCallback: (Intent) -> Unit
    ) {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)){
            intentHandlerDataCallback = dataCallback
            intentHandlerRequestCode = requestCode
            startActivityForResult(intent, requestCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == intentHandlerRequestCode && data != null){
            intentHandlerDataCallback?.invoke(data)
            intentHandlerDataCallback = null
            intentHandlerRequestCode = null
        }

    }

    fun setLoading(isVisible: Boolean){
        binding.ilLoading.root.isVisible = isVisible
    }

    override fun onDestroy() {
        super.onDestroy()
        intentHandlerDataCallback = null
        intentHandlerRequestCode = null
    }
}