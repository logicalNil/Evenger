package com.nilay.evenger.ui.fragments.login.loading

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import com.nilay.evenger.R
import com.nilay.evenger.core.api.ApiCase
import com.nilay.evenger.databinding.FragmentLoadingBinding
import com.nilay.evenger.utils.Axis
import com.nilay.evenger.utils.SharePrefKeys
import com.nilay.evenger.utils.exitTransition
import com.nilay.evenger.utils.launchWhenCreated
import com.nilay.evenger.utils.navigate
import com.nilay.evenger.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoadingFragment : Fragment(R.layout.fragment_loading) {

    @Inject
    lateinit var apiCase: ApiCase

    @Inject
    lateinit var pref: SharedPreferences


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDataFromApi()
    }

    private fun getDataFromApi() = launchWhenCreated {
        apiCase.getAllData.invoke { (_, ex) ->
            if (ex != null) {
                toast("Something went wrong try again later .. ${ex.message}")
                return@invoke
            }
            toast("Data fetched successfully and saved in database")
        }
        Thread.sleep(1000)
        pref.edit().apply {
            putBoolean(SharePrefKeys.RestoreDone.name, true)
            putBoolean(SharePrefKeys.SetUpDone.name, true)
        }.apply()
        navigateToHome()
    }

    private fun navigateToHome() {
        exitTransition(Axis.Z)
        val action = LoadingFragmentDirections.actionLoadingFragmentToEventFragment()
        navigate(action)
    }
}