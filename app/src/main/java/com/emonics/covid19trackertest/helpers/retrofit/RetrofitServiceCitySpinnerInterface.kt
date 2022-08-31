package com.emonics.covid19trackertest.helpers.retrofit

import com.emonics.covid19trackertest.dataClass.City
import com.emonics.covid19trackertest.dataClass.CountryCity
import retrofit2.Response
import retrofit2.http.GET

interface RetrofitServiceCitySpinnerInterface {
    @GET("CountryCity.json")
     fun getCountryCity(): Response<List<CountryCity>>
    @GET("City.json")
     fun getCityDetail(): Response<List<City>>
}