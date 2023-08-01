package com.nilay.evenger.ui.fragments.login.util

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.nilay.evenger.R
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private const val TAG = "GoogleSignUIClient"

class GoogleSignUIClient(
    private val context: Context, private val oneTimeClient: SignInClient
) {
    suspend fun intentSender(): Pair<IntentSender?, Exception?> = suspendCoroutine { continuation ->
        val signRequest = buildSignRequest()
        val signInTask = oneTimeClient.beginSignIn(signRequest)

        signInTask.addOnSuccessListener { result ->
            val intentSender = result.pendingIntent.intentSender
            continuation.resume(intentSender to null)
        }

        signInTask.addOnFailureListener { exception ->
            Log.e(TAG, "sign: $exception")
            continuation.resume(null to exception)
        }
    }

    private fun buildSignRequest(): BeginSignInRequest =
        BeginSignInRequest.builder().setGoogleIdTokenRequestOptions(
            GoogleIdTokenRequestOptions.builder().setSupported(true)
                .setServerClientId(context.getString(R.string.default_web_client_id))
                .setFilterByAuthorizedAccounts(false).build()
        ).setAutoSelectEnabled(true).build()

    fun getSignUserFromIntend(intend: Intent, callback: (String, Exception?) -> Unit) {
        try {
            val task = oneTimeClient.getSignInCredentialFromIntent(intend)
            val googleIdToken = task.googleIdToken
            googleIdToken?.let {
                callback(it, null)
            }
        } catch (e: Exception) {
            Log.e(TAG, "getSignUserFromIntend: $e")
            callback("", e)
        }
    }

}