package com.nilay.evenger.module

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {


    @Provides
    @Singleton
    fun provideFirebase() = Firebase.firestore


    @Singleton
    @Provides
    fun providerFirebaseAuth() = Firebase.auth

    @Singleton
    @Provides
    fun provideFCM() : FirebaseMessaging = Firebase.messaging

}