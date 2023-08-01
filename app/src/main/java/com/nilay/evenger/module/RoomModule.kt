package com.nilay.evenger.module

import android.content.Context
import androidx.room.Room
import com.nilay.evenger.core.db.EvengerDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
        callback: EvengerDatabase.EventCallback
    ): EvengerDatabase = Room.databaseBuilder(
        context,
        EvengerDatabase::class.java,
        EvengerDatabase.DATABASE_NAME
    ).fallbackToDestructiveMigration()
        .addCallback(callback)
        .build()


    @Provides
    @Singleton
    fun provideEventDao(
        database: EvengerDatabase
    ) = database.eventDao()

    @Provides
    @Singleton
    fun provideAttendanceDao(
        database: EvengerDatabase
    ) = database.attendanceDao()

}