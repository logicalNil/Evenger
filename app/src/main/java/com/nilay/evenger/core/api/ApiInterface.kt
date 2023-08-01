package com.nilay.evenger.core.api

import retrofit2.http.GET

interface ApiInterface {
    @GET("data/data.json")
    suspend fun getSubjects(): SubjectResponse
}