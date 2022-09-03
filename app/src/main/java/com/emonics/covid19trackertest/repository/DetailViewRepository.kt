package com.emonics.covid19trackertest.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.emonics.covid19trackertest.Room.CovidTrackerDatabase
import com.emonics.covid19trackertest.dataClass.City
import com.emonics.covid19trackertest.dataClass.Country
import com.emonics.covid19trackertest.dataClass.Global
import com.emonics.covid19trackertest.helpers.retrofit.RetroServiceDbUpdateInterface
import com.emonics.covid19trackertest.util.Covid19TrackerUtility

class DetailViewRepository(
    private val apiInterFace: RetroServiceDbUpdateInterface,
    private val covidTrackerDatabase: CovidTrackerDatabase,
    private val applicationContext: Context
) {

    val detailGlobalMutabledata = MutableLiveData<Global>()
    val detailGlobalLivedata:LiveData<Global>
    get() = detailGlobalMutabledata
    val ErrorMsgDetailGlobalMutableData = MutableLiveData<String>()
    val errorMessageDetailGlobalLiveData: LiveData<String>
    get() = ErrorMsgDetailGlobalMutableData

    suspend fun getDetailGlobalRecord() {
        if(Covid19TrackerUtility.checkForInternet(applicationContext)) {
            if(apiInterFace.getGlobalData().body()!=null){
                detailGlobalMutabledata.postValue(apiInterFace.getGlobalData().body())
            }else{
                ErrorMsgDetailGlobalMutableData.postValue("API connection Error")
            }
        }  else {
            if(covidTrackerDatabase.GlobalDao().getAllGlobalData()!=null){
                detailGlobalMutabledata.postValue(covidTrackerDatabase.GlobalDao().getAllGlobalData())
            }else{
                ErrorMsgDetailGlobalMutableData.postValue("Database connection Error")
            }

        }

    }

    val countryDetailView = MutableLiveData<Country>()
    val liveCountryDetailView:LiveData<Country>
        get() = countryDetailView

    val errorMsgCountryDetailView = MutableLiveData<String>()
    val errorMsgCountryDetailViewLiveDataForAdmin:LiveData<String>
        get() = errorMsgCountryDetailView
    suspend fun getCountryDetail(countrySelected:String) {

        var country: Country
        if (Covid19TrackerUtility.checkForInternet(applicationContext)) {
            if (apiInterFace.getCountryDetails().body() != null) {
                Log.i("tag_", "INSIDE country " + apiInterFace.getCountryDetails().body())
                var country = apiInterFace.getCountryDetails().body()!!.filter { country ->
                    country.country_name == countrySelected
                }
                Log.i("tag_", "country array" + country)
                Log.i("tag_", "countrySelected" + countrySelected)
                if(country.isNotEmpty()) {
                    countryDetailView.postValue(country[0])
                } else {
                    Log.i("tag_", "empty array")
                    errorMsgCountryDetailView.postValue("Data does not Exist")

                }
            } else {
                errorMsgCountryDetailView.postValue("Data does not Exist")
            }

        } else {
            if (covidTrackerDatabase.CountryDao().findByCountryName(countrySelected) != null) {
                countryDetailView.postValue(
                    covidTrackerDatabase.CountryDao().findByCountryName(countrySelected)
                )
            } else {
                errorMsgCountryDetailView.postValue("Data does not Exist")
            }
        }

    }

    val allCountryDetailView = MutableLiveData<List<Country>>()
    val liveAllCountryDetailView:LiveData<List<Country>>
        get() = allCountryDetailView

    val errorMsgAllCountryDetailView = MutableLiveData<String>()
    val errorMsgAllCountryDetailViewLiveDataForAdmin:LiveData<String>
        get() = errorMsgAllCountryDetailView

    suspend fun getAllCountryDetail() {
        var country: Country
        if (Covid19TrackerUtility.checkForInternet(applicationContext)) {
            if (apiInterFace.getCountryDetails().body() != null) {
                Log.i("tag_", "country " + apiInterFace.getCountryDetails().body())
                allCountryDetailView.postValue(apiInterFace.getCountryDetails().body())
            } else {
                Log.i("tag_", "data does not exist ")
                errorMsgAllCountryDetailView.postValue("API connection Error")
            }

        } else {
            if (covidTrackerDatabase.CountryDao().getAllCountry() != null) {
                allCountryDetailView.postValue(
                    covidTrackerDatabase.CountryDao().getAllCountry()
                )
            } else {
                errorMsgAllCountryDetailView.postValue("Database connection Error")
            }
        }

    }

    val citiListDetail = MutableLiveData<List<City>>()
    val citiDetailLiveData:LiveData<List<City>>
        get() = citiListDetail
    val errorMessageMutableDataForCity = MutableLiveData<String>()
    val errorMessageLiveDataForCity: LiveData<String>
        get() = errorMessageMutableDataForCity
    suspend fun getCitiesDetailBasedonCountry(countrySelected:String) {
        if(Covid19TrackerUtility.checkForInternet(applicationContext)) {
            val cityRecords = apiInterFace.getCityDetail()
            Log.i("tag_","INSIDE cityRecords "+cityRecords)
            if(cityRecords.body() != null){
                var citiesForCountry= cityRecords.body()!!.filter {
                    it.country== countrySelected
                }
                citiListDetail.postValue(citiesForCountry)
            } else{
                errorMessageMutableDataForCity.postValue("API Error")
            }
        }  else{
            val cityRecords = covidTrackerDatabase.CityDao().findByCountryName(countrySelected)
            if(cityRecords != null) {
                citiListDetail.postValue(cityRecords)
            }else{
                errorMessageMutableDataForCity.postValue("Database Error")
            }
        }
    }




}