package com.emonics.covid19trackertest.helpers.retrofit

import android.service.autofill.UserData
import retrofit2.Call
import retrofit2.http.GET

interface RetroServiceInterFace {
    @GET("users")
    fun getUserDetails(): Call<List<User>>
}