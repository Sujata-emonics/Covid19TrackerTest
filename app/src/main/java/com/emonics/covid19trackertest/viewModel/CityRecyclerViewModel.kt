package com.emonics.covid19trackertest.viewModel

import androidx.lifecycle.MutableLiveData
import com.emonics.covid19trackertest.dataClass.City
import com.emonics.covid19trackertest.repository.CovidTrackerRepository

class CitiRecyclerViewModel(private var repository: CovidTrackerRepository) {
    val citiList = MutableLiveData<List<City>>()
    val errorMessage = MutableLiveData<String>()

    fun getAllCities() {
        val response = repository.getAllcities()

        if (response.body() != null) {
            citiList.postValue(response.body())
        } else {
            errorMessage.postValue("Error with ApI Connectiom")
        }
    }

}
