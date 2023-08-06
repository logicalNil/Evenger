package com.nilay.evenger.service

import android.annotation.SuppressLint
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.nilay.evenger.R
import com.nilay.evenger.utils.CHANNEL_ID

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class FCMService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("AAA", "onMessageReceived: called")
        Log.d("AAA", "onMessageReceived: notification received !! $message")
        createNotification(message)
    }

    @SuppressLint("MissingPermission")
    private fun createNotification(message: RemoteMessage) {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(message.notification?.title)
            .setContentText(message.notification?.body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
        val managerCompat = androidx.core.app.NotificationManagerCompat.from(this)
        managerCompat.notify(1, builder.build())
    }
}