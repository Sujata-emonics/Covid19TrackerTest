package com.emonics.covid19trackertest.viewModel

import android.util.Log
import androidx.lifecycle.*
import com.emonics.covid19trackertest.dataClass.City
import com.emonics.covid19trackertest.dataClass.Country
import com.emonics.covid19trackertest.dataClass.Global
import com.emonics.covid19trackertest.repository.CovidTrackerRepository
import kotlinx.coroutines.launch

class CityRecyclerViewModel(private var repository: CovidTrackerRepository,
private var countrySelected:String):ViewModel() {
    init {
        viewModelScope.launch(){
            //cityRecyclerViewModel.getCountry(countryName.toString())
            //cityRecyclerViewModel.getGlobalRecord()
            //cityRecyclerViewModel.getAllCities()
            repository.getGlobalRecord()
            repository.getAllcities(countrySelected)
            repository.getCountryBasedOnName(countrySelected)
        }

    }

    //val errorMessage = MutableLiveData<String>()

    val citiRecordsLiveData : LiveData<List<City>>
    get() = repository.citiListLiveData
    val errorMessage:LiveData<String>
    get() =  repository.errorMessageLiveData

    /*suspend fun getAllCities() {
        val citiRecordsLiveData : LiveData<List<City>>
        get() = repository.citiListLiveData
        val response = repository.getAllcities()

        if (response.body() != null) {
            var citiesForCountry= response!!.body()!!.filter {
                it.country== countrySelected
            }
            citiList.postValue(citiesForCountry)
            Log.i("tag_", "====cities== "+citiesForCountry)
        } else {
            errorMessage.postValue("Error with ApI Connectiom")
        }
    }*/

    val countryList:LiveData<Country>
    get() = repository.countryListLiveData
    val errorMessageCountry:LiveData<String>
    get() = repository.errorMessageCountryLiveData


    /*suspend fun getCountry(countrySelected:String){
        val responseCountry = repository.getAllCountry()
        if(responseCountry.body()!=null){
            var countryResult = responseCountry!!.body()!!.filter {
                it.name == countrySelected
            }
            Log.i("tag_","==country= ="+countryResult)
            countryList.postValue(countryResult)
        }else{
            errorMessageCountry.postValue("Error with ApI Connectiom")
        }
    }*/
    val globalRecord:LiveData<Global>
    get()= repository.globalLiveRecord
    val globalErrorMessage:LiveData<String>
   get() = repository.errorMessageGlobalLiveData

/*    suspend fun getGlobalRecord(){
        val responseGlobal = repository.getGlobalRecord()
        if(responseGlobal.body()!=null){
            globalRecord.postValue(responseGlobal.body())
        } else {
            globalErrorMessage.postValue("Error with Global API")
        }
    }*/
}
