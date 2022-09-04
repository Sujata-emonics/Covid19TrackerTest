package com.emonics.covid19trackertest.helpers.retrofit

import com.emonics.covid19trackertest.dataClass.SparkView
import retrofit2.Call
import retrofit2.http.GET

interface RetrofitSparkViewService {
    @GET("us/daily.json")
    fun getNationalData(): Call<List<SparkView>>

    @GET("states/daily.json")
    fun getStatesData(): Call<List<SparkView>>
}