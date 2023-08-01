package com.nilay.evenger.ui.fragments.login

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.SignInButton
import com.nilay.evenger.R
import com.nilay.evenger.core.firebase.AuthCases
import com.nilay.evenger.databinding.FragmentLoginBinding
import com.nilay.evenger.ui.fragments.login.util.GoogleSignUIClient
import com.nilay.evenger.utils.Axis
import com.nilay.evenger.utils.SharePrefKeys
import com.nilay.evenger.utils.enterTransition
import com.nilay.evenger.utils.exitTransition
import com.nilay.evenger.utils.getLastDestination
import com.nilay.evenger.utils.isDark
import com.nilay.evenger.utils.launchWhenStarted
import com.nilay.evenger.utils.navigate
import com.nilay.evenger.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LogInFragment : Fragment(R.layout.fragment_login) {

    private val binding: FragmentLoginBinding by viewBinding()

    private val googleSignClient: GoogleSignUIClient by lazy {
        GoogleSignUIClient(
            requireActivity(), Identity.getSignInClient(requireActivity())
        )
    }

    @Inject
    lateinit var authUseCases: AuthCases


    @Inject
    lateinit var pref: SharedPreferences


    private val activityResult =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                it.data?.let { intent ->
                    googleSignClient.getSignUserFromIntend(intent) { token, error ->
                        if (error != null) {
                            toast(error.toString())
                            return@getSignUserFromIntend
                        }
                        authUseCases.login.invoke(token) { (ui, exception) ->
                            if (exception != null) {
                                toast(exception.toString())
                                return@invoke
                            }
                            ui?.let { _ ->
                                checkUserData()
                            }
                        }
                    }
                }
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition(Axis.Z)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        screenLogic()
        binding.apply {
            signInButton()
            skipButton()
        }
    }

    private fun screenLogic() {
        val isLogIn = authUseCases.hasLogIn.invoke()
        val isRestoreDone = pref.getBoolean(SharePrefKeys.RestoreDone.name, false)
        val isPermanentSkipLogin = pref.getBoolean(SharePrefKeys.PermanentSkipLogin.name, false)
        val isSetUpDone = pref.getBoolean(SharePrefKeys.SetUpDone.name, false)
        val fromHome =
            getLastDestination() == R.id.eventFragment || getLastDestination() == R.id.attendanceFragment
        if (!isLogIn && isPermanentSkipLogin && !fromHome) {
            if (isSetUpDone) navigateToHome()
            else return
        }

        if (isLogIn) {
            if (isRestoreDone && isSetUpDone) navigateToHome()
            else navigateToLoadingScreen()
        }
    }

    private fun navigateToHome() {
        val directions = LogInFragmentDirections.actionLogInFragmentToEventFragment()
        navigate(directions)
    }

    private fun FragmentLoginBinding.skipButton() = this.buttonSkip.apply {
        setOnClickListener {
            pref.edit().apply {
                putBoolean(SharePrefKeys.PermanentSkipLogin.name, true)
            }.apply()
            navigateToHome()
        }
    }

    private fun checkUserData() {
        navigateToLoadingScreen()
    }

    private fun navigateToLoadingScreen() {
        exitTransition(Axis.X)
        val action = LogInFragmentDirections.actionLogInFragmentToLoadingFragment()
        navigate(action)
    }


    private fun FragmentLoginBinding.signInButton() = this.signInButton.apply {
        setSize(SignInButton.SIZE_WIDE)
        setColorScheme(if (activity?.isDark() == true) SignInButton.COLOR_DARK else SignInButton.COLOR_LIGHT)
        setOnClickListener {
            signIn()
        }
    }

    private fun signIn() = launchWhenStarted {
        googleSignClient.intentSender().let { (intentSender, error) ->
            if (error != null) {
                toast(error.toString())
                return@let
            }
            activityResult.launch(
                IntentSenderRequest.Builder(
                    intentSender ?: return@let
                ).build()
            )
        }
    }
}