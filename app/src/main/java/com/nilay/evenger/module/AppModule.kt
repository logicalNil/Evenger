package com.nilay.evenger.module

import android.content.Context
import android.content.SharedPreferences
import com.nilay.evenger.utils.SharePrefKeys
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

//    @Singleton
//    @Provides
//    fun provideAppUpdateManager(@ApplicationContext context: Context): AppUpdateManager =
//        AppUpdateManagerFactory.create(context)

    @Singleton
    @Provides
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(SharePrefKeys.SharedPreferenceName.name, Context.MODE_PRIVATE)


    @Singleton
    @Provides
    fun getCalender(): Calendar = Calendar.getInstance()


}