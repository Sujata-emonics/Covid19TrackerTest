package com.emonics.covid19trackertest.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.emonics.covid19trackertest.Room.CovidTrackerDatabase
import com.emonics.covid19trackertest.dataClass.City
import com.emonics.covid19trackertest.dataClass.Country
import com.emonics.covid19trackertest.dataClass.Global
import com.emonics.covid19trackertest.dataClass.User
import com.emonics.covid19trackertest.helpers.retrofit.RetroServiceDbUpdateInterface
import com.emonics.covid19trackertest.util.Covid19TrackerUtility
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class CovidTrackerRepository(
    private val apiInterFace: RetroServiceDbUpdateInterface,
    private val covidTrackerDatabase: CovidTrackerDatabase,
    private val applicationContext: Context
) {

    private val covidTrackerLiveData = MutableLiveData<List<Country>>()

    val countryData:LiveData<List<Country>>
    get() = covidTrackerLiveData

    //Store Country DEtails from API to country table
    suspend fun getCountryDatas(){
        if(Covid19TrackerUtility.checkForInternet(applicationContext)){
            val countryResult = apiInterFace.getCountryDetails()
            if(countryResult.body() != null){
               covidTrackerDatabase.CountryDao().insertCountry(countryResult!!.body())
                covidTrackerLiveData.postValue(countryResult.body())
                //covidTrackerDatabase.CountryDao().deleteAllCountries()

            } else {
                val countries = covidTrackerDatabase.CountryDao().getAllCountry()
                covidTrackerLiveData.postValue(countries)
            }

        }
    }

    //
   private val cityLiveData = MutableLiveData<List<City>>()
    val cityData:LiveData<List<City>>
    get() = cityLiveData
    //Code is for inserting record in City table
    suspend fun storeCityDetail(){
         if(Covid19TrackerUtility.checkForInternet(applicationContext)){
            val cityListFromAPI = apiInterFace.getCityDetail()

            if(cityListFromAPI.body()!=null){
                covidTrackerDatabase.CityDao().insert(cityListFromAPI!!.body())
                cityLiveData.postValue(cityListFromAPI!!.body())
                //covidTrackerDatabase.CityDao().deleteAllCities()

            }
        } else{
             val cities = covidTrackerDatabase.CityDao().getAllCities()
            cityLiveData.postValue(cities)

        }
    }

    private val globalLiveData = MutableLiveData<Global>()
    val globalData:LiveData<Global>
        get() = globalLiveData
    //Code is for inserting record in Global table
    suspend fun storeGlobalDetail(){
        //Log.i("tag","-inside global function -")
        if(Covid19TrackerUtility.checkForInternet(applicationContext)) {
            val globalDataFromApi = apiInterFace.getGlobalData()
             if (globalDataFromApi.body() != null) {
                var globalRecord = covidTrackerDatabase.GlobalDao().getAllGlobalData()
                if(globalRecord!=null){
                    covidTrackerDatabase.GlobalDao().UpdateGlobalRecord(globalDataFromApi.body()!!)

                } else {
                     covidTrackerDatabase.GlobalDao().insertGlobalData(globalDataFromApi.body()!!)
                }
                globalLiveData.postValue(globalDataFromApi.body())

                // covidTrackerDatabase.GlobalDao().deleteGlobalRecord()
            }
        }else{
            val globalRecord = covidTrackerDatabase.GlobalDao().getAllGlobalData()
            globalLiveData.postValue(globalRecord)
        }
    }

    private val userLiveData = MutableLiveData<List<User>>()
    val userDetail: LiveData<List<User>>
    get() = userLiveData

    //Code is for inserting record in user table
    suspend fun storeUserDetail(){
        Log.i("log_u","-userDataFromApi-")
        if(Covid19TrackerUtility.checkForInternet(applicationContext)) {
            val userDataFromApi = apiInterFace.getUserData()
            Log.i("log_u","-userDataFromApi-"+userDataFromApi.body())
            if(userDataFromApi.body()!= null){
                covidTrackerDatabase.UserDao().insertUser(userDataFromApi!!.body()!!)
                userLiveData.postValue(userDataFromApi.body())
            }

        } else {
            val userRecord = covidTrackerDatabase.UserDao().getAllUsers()
            userLiveData.postValue(userRecord)
        }
    }


//Code is for getting record from user table
    val userRecords = MutableLiveData<List<User>>()
    val userRecordFromDb:LiveData<List<User>>
    get() = userRecords
     fun getUserDetailFromDB(){
        Log.i("tag_u","-getUserDetailFromDB-")
        var userDetails = covidTrackerDatabase.UserDao().getAllUsers()
        userRecords.postValue(userDetails)
    }


    //Code to send mail for forgot password
    val isMailSent= MutableLiveData<String>("")
    val emailLiveData:LiveData<String>
    get() =isMailSent
    suspend fun sendMailForForgotPassword(auth: FirebaseAuth, emailEntered: String) {
        Log.d("tag_", "Email sent"+emailEntered)
        auth!!.sendPasswordResetEmail(emailEntered.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("tag_", "Email sent.$emailEntered")
                    isMailSent.postValue("success")
                } else {  isMailSent.postValue("") }

                }
            }




}

