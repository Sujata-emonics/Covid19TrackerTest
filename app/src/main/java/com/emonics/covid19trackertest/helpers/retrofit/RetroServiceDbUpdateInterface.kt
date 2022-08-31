package com.emonics.covid19trackertest.helpers.retrofit

import com.emonics.covid19trackertest.dataClass.City
import com.emonics.covid19trackertest.dataClass.Country
import com.emonics.covid19trackertest.dataClass.CountryCity
import com.emonics.covid19trackertest.dataClass.Global
import retrofit2.Response
import retrofit2.http.GET

interface RetroServiceDbUpdateInterface {
    @GET("Country.json")
   suspend fun getCountryDetails(): Response<List<Country>>
    @GET("City.json")
   suspend fun getCityDetail(): Response<List<City>>
   @GET("Global.json")
   suspend fun getGlobalData():Response<Global>
   @GET("User.json")
   suspend fun getUserData():Response<List<com.emonics.covid19trackertest.dataClass.User>>
    @GET("CountryCity.json")
  suspend  fun getCountryCity(): Response<List<CountryCity>>


}