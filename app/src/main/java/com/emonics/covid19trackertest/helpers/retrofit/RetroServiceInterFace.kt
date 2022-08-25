package com.emonics.covid19trackertest.helpers.retrofit

import android.service.autofill.UserData
import com.emonics.covid19trackertest.dataClass.Country
import retrofit2.Call
import retrofit2.http.GET

interface RetroServiceInterFace {
    @GET("User.json")
    fun getUserDetails(): Call<List<User>>

}