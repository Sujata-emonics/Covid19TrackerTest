package com.emonics.covid19trackertest.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.emonics.covid19trackertest.Room.CovidTrackerDatabase
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
                countryDetailView.postValue(country[0])
            } else {
                errorMsgCountryDetailView.postValue("API connection Error")
            }

        } else {
            if (covidTrackerDatabase.CountryDao().findByCountryName(countrySelected) != null) {
                countryDetailView.postValue(
                    covidTrackerDatabase.CountryDao().findByCountryName(countrySelected)
                )
            } else {
                errorMsgCountryDetailView.postValue("Database connection Error")
            }
        }

    }
}