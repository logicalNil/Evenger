package com.nilay.evenger

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.nilay.evenger.utils.AppTheme
import com.nilay.evenger.utils.SharePrefKeys
import com.nilay.evenger.utils.setAppTheme
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class Evenger : Application() {
    @Inject
    lateinit var pref: SharedPreferences
    override fun onCreate() {
        super.onCreate()
        when (pref.getString(SharePrefKeys.AppTheme.name, AppTheme.Sys.name)) {
            AppTheme.Sys.name -> setAppTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            AppTheme.Light.name -> setAppTheme(AppCompatDelegate.MODE_NIGHT_NO)
            AppTheme.Dark.name -> setAppTheme(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

}