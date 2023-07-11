package com.civil31dot5.fitnessdiary.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.net.toUri
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import com.civil31dot5.fitnessdiary.data.IntentHandler
import com.civil31dot5.fitnessdiary.data.StravaAccountManagerImpl
import com.civil31dot5.fitnessdiary.ui.theme.FitnessDiaryTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity(), IntentHandler {

    @Inject
    lateinit var stravaAccountManager: StravaAccountManagerImpl
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        setContent {
            FitnessDiaryTheme {
                FitnessDiaryAPP()
            }
        }

        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(getIntent())
    }

    private fun handleIntent(intent: Intent) {
        val toRecord = intent.getStringExtra("to_record")
        if (toRecord == "diet") {
            val deepLinkIntent = Intent(
                Intent.ACTION_VIEW,
                "fitnessdiary://add_diet_record".toUri(),
                this,
                MainActivity::class.java
            )
            startActivity(deepLinkIntent)
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
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            intentHandlerDataCallback = dataCallback
            intentHandlerRequestCode = requestCode
            startActivityForResult(intent, requestCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == intentHandlerRequestCode && data != null) {
            intentHandlerDataCallback?.invoke(data)
            intentHandlerDataCallback = null
            intentHandlerRequestCode = null
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        intentHandlerDataCallback = null
        intentHandlerRequestCode = null
    }
}
