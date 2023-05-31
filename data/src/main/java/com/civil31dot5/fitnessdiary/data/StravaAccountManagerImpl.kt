package com.civil31dot5.fitnessdiary.data

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.core.content.edit
import com.civil31dot5.fitnessdiary.data.di.SecretSharedPrefs
import com.civil31dot5.fitnessdiary.domain.model.StravaConnectStatus
import com.civil31dot5.fitnessdiary.domain.usecase.sport.StravaAccountManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import net.openid.appauth.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

private const val CLIENT_ID = "103919"
private const val RESPONSE_TYPE = "code"
private const val REDIRECT_URI_STRING = "fitnessdiary://fitnessdiary.civil31dot5.com"
private const val REQUEST_CODE = 2023
private const val KEY_AUTH_STATE = "KEY_AUTH_STATE"

@Singleton
class StravaAccountManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    @SecretSharedPrefs private val sharedPreferences: SharedPreferences
) : StravaAccountManager {

    init {
        readAuthStateFromLocal()
    }

    private val authService = AuthorizationService(context)

    private val serverConfig: AuthorizationServiceConfiguration = AuthorizationServiceConfiguration(
        Uri.parse("https://www.strava.com/oauth/mobile/authorize"),
        Uri.parse("https://us-central1-fitnessdiary-f20c8.cloudfunctions.net/stravaOAuth")
    )
    private var authState: AuthState? = null

    override suspend fun connectStrava(): Boolean {
        authState = AuthState(serverConfig)
        val dataIntent = suspendCancellableCoroutine<Intent?> { continuation ->
            val handler = intentHandler
            if (handler == null) {
                continuation.resume(null)
            } else {
                handler.handleIntentForResult(createAuthIntent(), REQUEST_CODE) {
                    continuation.resume(it)
                }
            }
        } ?: return false

        return parseAuthResult(dataIntent)
    }

    override fun connectStatus(): StravaConnectStatus {
        return if (authState?.isAuthorized == true)
            StravaConnectStatus.Connected
        else
            StravaConnectStatus.NotConnected
    }

    override fun disconnectStrava() {
        authState = null
    }

    private fun createAuthIntent(): Intent {
        val authRequest = AuthorizationRequest.Builder(
            serverConfig,
            CLIENT_ID,
            RESPONSE_TYPE,
            Uri.parse(REDIRECT_URI_STRING)
        ).apply {
            setScopes("read,read_all,profile:read_all,activity:read_all")
            setState(UUID.randomUUID().toString())
        }.build()


        return authService.getAuthorizationRequestIntent(authRequest)
    }

    private suspend fun parseAuthResult(data: Intent): Boolean {
        val authResp = AuthorizationResponse.fromIntent(data)
        val authExp = AuthorizationException.fromIntent(data)
        return if (authResp != null) {
            updateAuthState(authResp, authExp)
            requireAccessToken(authResp)
        } else {
            Timber.d("auth fail: ${authExp?.error}")
            false
        }
    }

    private suspend fun requireAccessToken(authResp: AuthorizationResponse) =
        suspendCancellableCoroutine<Boolean> {
            authService.performTokenRequest(authResp.createTokenExchangeRequest()) { resp, ex ->
                if (resp != null) {
                    updateAuthState(resp, ex)
                    it.resume(true)
                } else {
                    Timber.d("TokenExchange fail: $ex")
                    it.resume(false)
                }
            }
        }

    private var intentHandler: IntentHandler? = null

    fun registerIntentHandler(handler: IntentHandler) {
        intentHandler = handler
    }

    fun unregisterIntentHandler() {
        intentHandler = null
    }

    fun getAccessToken(): String {
        return authState?.accessToken ?: ""
    }

    fun getRefreshToken(): String {
        return authState?.refreshToken ?: ""
    }

    suspend fun refreshToken() = suspendCancellableCoroutine<Unit> {
        authService.performTokenRequest(authState!!.createTokenRefreshRequest()) { resp, ex ->
            if (resp != null) {
                updateAuthState(resp, ex)
                it.resume(Unit)
            } else {
                it.resumeWithException(ex!!)
            }

        }
    }

    private fun updateAuthState(resp: AuthorizationResponse, ex: AuthorizationException?) {
        authState?.update(resp, ex)
        sharedPreferences.edit(commit = true) {
            putString(KEY_AUTH_STATE, authState?.jsonSerializeString())
        }
    }

    private fun updateAuthState(resp: TokenResponse, ex: AuthorizationException?) {
        authState?.update(resp, ex)
        sharedPreferences.edit(commit = true) {
            putString(KEY_AUTH_STATE, authState?.jsonSerializeString())
        }
    }

    private fun readAuthStateFromLocal() {
        val json = sharedPreferences.getString(KEY_AUTH_STATE, "")
        if (json != null && json.isNotEmpty()) {
            authState = AuthState.jsonDeserialize(json)
        }
    }
}

interface IntentHandler {
    fun handleIntentForResult(intent: Intent, requestCode: Int, dataCallback: (Intent) -> Unit)
}