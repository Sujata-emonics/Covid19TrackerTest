package com.emonics.covid19trackertest.viewModel

import android.content.Intent
import android.util.Log
import android.util.Patterns
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emonics.covid19trackertest.activities.MainActivity
import com.emonics.covid19trackertest.dataClass.City
import com.emonics.covid19trackertest.dataClass.Country
import com.emonics.covid19trackertest.helpers.eventClass.Event
import com.emonics.covid19trackertest.repository.CovidTrackerRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class AdminViewModel(private val covidTrackerRepository: CovidTrackerRepository):ViewModel() {
    var countryList=ArrayList<String>()
    var cityList=ArrayList<String>()
    val inputCountrySelected = MutableLiveData<String>()
    val inputCitySelected = MutableLiveData<String>()
    val inputActiveCases = MutableLiveData<String>()
    val inputConfirmedCases = MutableLiveData<String>()
    val inputRecoveredCases = MutableLiveData<String>()
    val inputDeathCases = MutableLiveData<String>()
    val inputTestCases = MutableLiveData<String>()
    val saveOrUpdateButtonText = MutableLiveData<String>()
    val clearAllButtonOrDeleteText = MutableLiveData<String>()
    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
        get() = statusMessage

   init {
       saveOrUpdateButtonText.value = "Save"
       clearAllButtonOrDeleteText.value = "Clear All"
        viewModelScope.launch {
            countryList =  covidTrackerRepository.getAllCountrySpinner()
            countryListMutable.postValue(countryList)
            Log.i("tag_"," AdminViewModel "+countryList)
        }

    }

    fun insertCityRecord(city: City) =  viewModelScope.launch {
        val newRowId = covidTrackerRepository.insertCityRecordFromAdmin(city)
        statusMessage.value = Event("Record Inserted Successfully")
        Log.i("tag_", "newRowId"+newRowId)
       /* if (newRowId) {
            statusMessage.value = Event("Record Inserted Successfully $newRowId")
        } else {
            statusMessage.value = Event("Error Occurred")
        }*/
    }


    fun saveOrUpdate() {
        Log.i("tag_"," saveOrUpdate ")

        if (inputActiveCases.value == null) {
            statusMessage.value = Event("Please enter active cases")
        } else if (inputConfirmedCases.value == null) {
            statusMessage.value = Event("Please enter confirmed cases")
        }else if (inputRecoveredCases.value == null) {
            statusMessage.value = Event("Please enter recovered cases")
        }else if (inputDeathCases.value == null) {
            statusMessage.value = Event("Please enter death cases")
        }
        else if (inputTestCases.value == null) {
                    statusMessage.value = Event("Please enter test cases")
        } else {

            val countrySelected = inputCountrySelected.value!!
            val citySelected = inputCitySelected.value!!
            // val countrySelected = "India"
            //val citySelected = "Mumbai"
            val activeCases = inputActiveCases.value!!
            val confirmedCases = inputConfirmedCases.value!!
            val recovredCases = inputRecoveredCases.value!!
            val deathCases = inputDeathCases.value!!
            val testCases = inputTestCases.value!!
            Log.i("tag_", " activeCases " + activeCases)
            Log.i("tag_", " countrySelected " + countrySelected)
            Log.i("tag_", " citySelected " + citySelected)
            Log.i("tag_", " confirmedCases " + confirmedCases)
            Log.i("tag_", " recovredCases " + recovredCases)
            Log.i("tag_", " deathCases " + deathCases)
            Log.i("tag_", " testCases " + testCases)


            insertCityRecord(
                City(
                    0, citySelected,
                    0, countrySelected, activeCases,
                    confirmedCases, recovredCases, deathCases, testCases
                )
            )

            /*  inputCountrySelected.value = ""
        inputCitySelected.value = ""
        inputRecoveredCases.value = ""
        inputActiveCases.value = ""
        inputConfirmedCases.value = ""
        inputDeathCases.value = ""
        inputDeathCases.value = ""
        inputTestCases.value = ""

       */
        }
    }



val countryListMutable = MutableLiveData<ArrayList<String>>()
    val countryLiveData:LiveData<ArrayList<String>>
    get() = countryListMutable

val cityListMutable = MutableLiveData<List<String>>()
    val cityLiveData:LiveData<List<String>>
    get() = covidTrackerRepository.liveCityListSpinnerForAdminBasedonCountry



    fun getCityForselectedCountry(selectedCountry:String) {
        viewModelScope.launch {
            Log.i("tag_", "getCityForselectedCountry" + selectedCountry)
            //covidTrackerRepository.submitRecord()
             covidTrackerRepository.getCitiBasedOnCountrySpinner(selectedCountry)
            //cityList = covidTrackerRepository.etCitiBasedOnCountrySpinner(selectedCountry)
           // cityListMutable.postValue(cityList)
       }
    }




       val countryArrayLivedata: LiveData<List<Country>>
       get() = covidTrackerRepository.liveCountryListSpinnerForAdmin

       val errorMsgCountryListLiveData: LiveData<String>
       get() = covidTrackerRepository.errorMsgCountryListLiveDataSpinnerForAdmin

       val cityArrayLiveData:LiveData<List<City>>
       get() = covidTrackerRepository.liveCityListSpinnerForAdmin



/*       fun getCityForselectedCountry(selectedCountry:String){
           viewModelScope.launch {
              /* Log.i("tag_", "getCityForselectedCountry" + selectedCountry)
               covidTrackerRepository.getCitiBasedOnCountrySpinner(selectedCountry)*/
               covidTrackerRepository.getAllCountryCityForSpinner(selectedCountry)
           }

       }*/


}

