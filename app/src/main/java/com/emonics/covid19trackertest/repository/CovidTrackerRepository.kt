package com.emonics.covid19trackertest.repository

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.sourceInformationMarkerStart
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.emonics.covid19trackertest.Room.CovidTrackerDatabase
import com.emonics.covid19trackertest.activities.MainActivity
import com.emonics.covid19trackertest.activities.UpdateDBActivity
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


    val citiList = MutableLiveData<List<City>>()
    val citiListLiveData:LiveData<List<City>>
    get() = citiList
    val errorMessageMutableData = MutableLiveData<String>()
    val errorMessageLiveData: LiveData<String>
    get() = errorMessageMutableData

    suspend fun getAllcitiesBasedonCountry(countrySelected:String) {
        if(!Covid19TrackerUtility.checkForInternet(applicationContext)) {
            val cityRecords = apiInterFace.getCityDetail()
            Log.i("tag_","INSIDE cityRecords "+cityRecords)
            if(cityRecords.body() != null){
                var citiesForCountry= cityRecords.body()!!.filter {
                    it.country== countrySelected
                }
                citiList.postValue(citiesForCountry)
            } else{
                errorMessageMutableData.postValue("API Error")
            }
        }  else{
            val cityRecords = covidTrackerDatabase.CityDao().findByCountryName(countrySelected)
            if(cityRecords != null) {
                citiList.postValue(cityRecords)
            }else{
                errorMessageMutableData.postValue("Database Error")
            }
        }
    }

    val countryListMutableData = MutableLiveData<Country>()
    val countryListLiveData:LiveData<Country>
    get() = countryListMutableData

    val ErrorMsgMutableData = MutableLiveData<String>()
    val errorMessageCountryLiveData:LiveData<String>
    get() = ErrorMsgMutableData

    suspend fun getCountryBasedOnName(countrySelected:String) {
        if(Covid19TrackerUtility.checkForInternet(applicationContext)) {
            if(apiInterFace.getCountryDetails().body()!= null){
                Log.i("tag_","INSIDE country "+apiInterFace.getCountryDetails().body())
                Log.i("tag_","countrySelected country "+countrySelected)
                var countryResult = apiInterFace.getCountryDetails()!!.body()!!.filter {
                    it.country_name == countrySelected
                }
                Log.i("tag_","countryResult "+countryResult)
                countryListMutableData.postValue(countryResult[0])
            }else{
                ErrorMsgMutableData.postValue("API connection Error")
            }

        }  else{
            if(covidTrackerDatabase.CountryDao().findByCountryName(countrySelected)!=null){
                countryListMutableData.postValue(covidTrackerDatabase.CountryDao().findByCountryName(countrySelected))
            }else{
                ErrorMsgMutableData.postValue("API connection Error")
            }
        }
    }
    val globalMutableData = MutableLiveData<Global>()
    val globalLiveRecord:LiveData<Global>
    get() =globalMutableData

    val ErrorMsgGlobalMutableData = MutableLiveData<String>()
    val errorMessageGlobalLiveData:LiveData<String>
        get() = ErrorMsgGlobalMutableData

    suspend fun getGlobalRecord() {
        if(Covid19TrackerUtility.checkForInternet(applicationContext)) {
           if(apiInterFace.getGlobalData().body()!=null){
               globalMutableData.postValue(apiInterFace.getGlobalData().body())
           }else{
               ErrorMsgGlobalMutableData.postValue("API connection Error")
           }
         }  else {
             if(covidTrackerDatabase.GlobalDao().getAllGlobalData()!=null){
                 globalMutableData.postValue(covidTrackerDatabase.GlobalDao().getAllGlobalData())
             }else{
                 ErrorMsgGlobalMutableData.postValue("Database connection Error")
             }

        }

        }

    val countryListForAdmin = MutableLiveData<List<Country>>()
    val liveCountryListForAdmin:LiveData<List<Country>>
        get() =countryListForAdmin

    val errorMsgCountryListForAdmin = MutableLiveData<String>()
    val errorMsgCountryListLiveDataForAdmin:LiveData<String>
        get() = errorMsgCountryListForAdmin
    suspend fun getAllCountry(){
        if(Covid19TrackerUtility.checkForInternet(applicationContext)) {
            if(apiInterFace.getCountryDetails().body()!= null){
                Log.i("tag_","INSIDE country "+apiInterFace.getCountryDetails().body())
                countryListForAdmin.postValue(apiInterFace.getCountryDetails().body())
            }else{
                errorMsgCountryListForAdmin.postValue("API connection Error")
            }

        }  else{
            if(covidTrackerDatabase.CountryDao().getAllCountry()!=null){
                countryListForAdmin.postValue(covidTrackerDatabase.CountryDao().getAllCountry())
            }else{
                errorMsgCountryListForAdmin.postValue("API connection Error")
            }
        }

    }




    val countryListSpinnerForAdmin = MutableLiveData<List<Country>>()
    val liveCountryListSpinnerForAdmin:LiveData<List<Country>>
        get() =countryListSpinnerForAdmin

    var cityListSpinnerForAdmin = MutableLiveData<List<City>>()
    val liveCityListSpinnerForAdmin:LiveData<List<City>>
        get() =cityListSpinnerForAdmin

    val errorMsgCountryListSpinnerForAdmin = MutableLiveData<String>()
    val errorMsgCountryListLiveDataSpinnerForAdmin:LiveData<String>
        get() = errorMsgCountryListSpinnerForAdmin
    suspend fun getAllCountryCityForSpinner(countrySelected: String = ""){
        Log.i("tag_","getAllCountryCityForSpinner "+Covid19TrackerUtility.checkForInternet(applicationContext))
        if(Covid19TrackerUtility.checkForInternet(applicationContext)) {
            if(apiInterFace.getCountryDetails().body()!= null){
                Log.i("tag_","INSIDE country "+apiInterFace.getCountryDetails().body())
                countryListSpinnerForAdmin.postValue(apiInterFace.getCountryDetails().body())
                //var cityForSelectedCountry = getAllcitiesBasedonCountry(apiInterFace.getCountryDetails().body()!![0].country_name.toString())
                if(countrySelected!="") {
                    val cityRecords = apiInterFace.getCityDetail()
                    if (cityRecords.body() != null) {
                        var citiesForCountry = cityRecords.body()!!.filter {
                            if (countrySelected != "") {
                                it.country == countrySelected
                            } else {
                                it.country == apiInterFace.getCountryDetails()
                                    .body()!![0].country_name.toString()
                            }

                        }
                        cityListSpinnerForAdmin.postValue(citiesForCountry)
                        Log.i("tag_", "cityListSpinnerForAdmin" + citiesForCountry)
                    }
                }

                //Log.i("tag_","cityListSpinnerForAdmin" +apiInterFace.getCountryDetails().body()!![0].country_name.toString())

            }else{
                errorMsgCountryListSpinnerForAdmin.postValue("API connection Error")
            }
        }  else{
            if(covidTrackerDatabase.CountryDao().getAllCountry()!=null){
                countryListSpinnerForAdmin.postValue(covidTrackerDatabase.CountryDao().getAllCountry())
                Log.i("tag_","countrySelected"+countrySelected)
                var cityForSelectedCountry = if(countrySelected!=""){
                    covidTrackerDatabase.CityDao().findByCountryName(countrySelected)
                }else{covidTrackerDatabase.CityDao().findByCountryName(covidTrackerDatabase.CountryDao().getAllCountry()!![0].country_name.toString())}
                Log.i("tag_","cityForSelectedCountry"+cityForSelectedCountry)
                cityListSpinnerForAdmin.postValue(cityForSelectedCountry)

            }else{
                errorMsgCountryListSpinnerForAdmin.postValue("API connection Error")
            }
        }

    }


    val cityListSpinnerForAdminBasedonCountry = MutableLiveData<List<String>>()
    val liveCityListSpinnerForAdminBasedonCountry:LiveData<List<String>>
        get() =cityListSpinnerForAdminBasedonCountry


    suspend fun getCitiBasedOnCountrySpinner(countrySelected:String){
        Log.i("tag_","getCitiBasedOnCountrySpinner" +countrySelected)
        var cityList = ArrayList<String>()
        var citiesForCountry = ArrayList<String>()
        val cityRecords = apiInterFace.getCityDetail()
        if(cityRecords.body() != null){
           cityRecords.body()!!.forEach {
               if(it.country == countrySelected){
                   citiesForCountry.add(it.name.toString())
               }
           }
            cityListSpinnerForAdminBasedonCountry.postValue(citiesForCountry)
            //cityListSpinnerForAdmin.postValue(citiesForCountry)
            Log.i("tag_","cityListSpinnerForAdmin1233" +citiesForCountry)
        }

}

//===========
suspend fun getAllCountrySpinner(): ArrayList<String> {
    var countryList = ArrayList<String>()
    if(Covid19TrackerUtility.checkForInternet(applicationContext)) {
        if(apiInterFace.getCountryDetails().body()!= null){

             apiInterFace.getCountryDetails().body()!!.forEach { country->
                countryList.add(country.country_name.toString()) }
            Log.i("tag_","countryList1 "+countryList)
            return countryList

          }else{
            Log.i("tag_","countryList2 "+countryList)
            return countryList
        }
    }  else{
        if(covidTrackerDatabase.CountryDao().getAllCountry()!=null){
            covidTrackerDatabase.CountryDao().getAllCountry().
            forEach { country->countryList.add(country.country_name.toString()) }
            Log.i("tag_","countryList3"+countryList)
            return countryList
            }else{
            Log.i("tag_","countryList41 "+countryList)
            return countryList
            }
        }
    }


    suspend fun getCitySpinner(countrySelected: String): ArrayList<String> {
        var cityList = ArrayList<String>()
        Log.i("tag_","getCitySpinner "+countrySelected)
        if(Covid19TrackerUtility.checkForInternet(applicationContext)) {
            if(apiInterFace.getCityDetail().body()!= null){
                Log.i("tag_","getCitySpinner "+apiInterFace.getCityDetail().body())

                apiInterFace.getCityDetail().body()!!.forEach { city->
                    if(city.country == countrySelected){ cityList.add(city.name.toString())}
                    }
                Log.i("tag_","citylist 1 "+cityList)
                return cityList

            }else{
                Log.i("tag_","cityList2 "+cityList)
                return cityList
            }
        }  else{
            if(covidTrackerDatabase.CityDao().findByCountryName(countrySelected)!=null){
                covidTrackerDatabase.CityDao().findByCountryName(countrySelected).
                forEach { city->cityList.add(city.name.toString()) }
                Log.i("tag_","cityList3"+cityList)
                return cityList
            }else{
                Log.i("tag_","cityList4 "+cityList)
                return cityList
            }
        }
    }



    //
    suspend fun insertCityRecordFromAdmin(city:City){
        Log.i("tag_","insertCityRecordFromAdmin"+city)
        covidTrackerDatabase.CityDao().insertsingleRecord(city)
    }


    //
    suspend fun getCountryFRomCountryCitySpinner(): ArrayList<String> {
        var countryList = ArrayList<String>()
        if(Covid19TrackerUtility.checkForInternet(applicationContext)) {
            Log.i("tag_","countryList1 "+apiInterFace.getCountryCity().body())
            if(apiInterFace.getCountryCity().body()!= null){

                apiInterFace.getCountryCity().body()!!.forEach { country->
                    countryList.add(country.country.toString()) }
                Log.i("tag_","countryList1 "+countryList)
                return countryList

            }else{
                Log.i("tag_","countryList2 "+countryList)
                return countryList
            }
        }  else{
            if(covidTrackerDatabase.CountryCityDao().findAllCountry()!=null){
                covidTrackerDatabase.CountryCityDao().findAllCountry().
                forEach { country->countryList.add(country.toString()) }
                Log.i("tag_","countryList3"+countryList)
                return countryList
            }else{
                Log.i("tag_","countryList41 "+countryList)
                return countryList
            }
        }
    }

    //====
    suspend fun getCityFromCountryCitySpinner(countrySelected: String): ArrayList<String> {
        Log.i("tag_","getCityFromCountryCitySpinner 1 "+apiInterFace.getCountryCity().body())
        var cityList = ArrayList<String>()
        if(Covid19TrackerUtility.checkForInternet(applicationContext)) {
            Log.i("tag_","getCityFromCountryCitySpinner 1 "+apiInterFace.getCountryCity().body())
            if(apiInterFace.getCountryCity().body()!= null){

                apiInterFace.getCountryCity().body()!!.forEach { countryCity->
                    if(countryCity.country.toString() == countrySelected){ cityList.add(countryCity.city.toString())}
                }
                Log.i("tag_","citylist 1 "+cityList)
                return cityList

            }else{
                Log.i("tag_","cityList2 "+cityList)
                return cityList
            }
        }  else{
            if(covidTrackerDatabase.CountryCityDao().findByCountry(countrySelected)!=null){
                Log.i("tag_","cityList3"+covidTrackerDatabase.CountryCityDao().findByCountry(countrySelected))
                return cityList
              //  covidTrackerDatabase.CountryCityDao().findByCountry(countrySelected).
               // forEach { cityCountry->cityList.add(cityCountry!!.city!!.toString()) }
                //Log.i("tag_","cityList3"+cityList)
                return cityList
            }else{
                Log.i("tag_","cityList4 "+cityList)
                return cityList
            }
        }
    }

}

