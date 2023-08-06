package com.nilay.evenger

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.messaging.FirebaseMessaging
import com.nilay.evenger.utils.AppTheme
import com.nilay.evenger.utils.CHANNEL_DESCRIPTION
import com.nilay.evenger.utils.CHANNEL_ID
import com.nilay.evenger.utils.CHANNEL_NAME
import com.nilay.evenger.utils.SharePrefKeys
import com.nilay.evenger.utils.setAppTheme
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class Evenger : Application() {
    @Inject
    lateinit var pref: SharedPreferences

    @Inject
    lateinit var fcm: FirebaseMessaging
    override fun onCreate() {
        super.onCreate()
        when (pref.getString(SharePrefKeys.AppTheme.name, AppTheme.Sys.name)) {
            AppTheme.Sys.name -> setAppTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            AppTheme.Light.name -> setAppTheme(AppCompatDelegate.MODE_NIGHT_NO)
            AppTheme.Dark.name -> setAppTheme(AppCompatDelegate.MODE_NIGHT_YES)
        }
        fcm.subscribeToTopic("Evenger")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createAppNotificationChannel()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createAppNotificationChannel() {
        val noticeChannel = NotificationChannel(
            CHANNEL_ID, CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        noticeChannel.description = CHANNEL_DESCRIPTION
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(noticeChannel)
    }

}