package com.nilay.evenger.module

import com.nilay.evenger.core.api.ApiInterface
import com.nilay.evenger.utils.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {


    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    @Singleton
    fun provideRetrofit(
        converter: GsonConverterFactory
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(converter)
        .build()

    @Provides
    @Singleton
    fun provideApiInterface(retrofit: Retrofit): ApiInterface =
        retrofit.create(ApiInterface::class.java)
}