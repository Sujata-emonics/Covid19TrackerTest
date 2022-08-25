package com.emonics.covid19trackertest.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emonics.covid19trackertest.dataClass.City
import com.emonics.covid19trackertest.dataClass.Country
import com.emonics.covid19trackertest.dataClass.Global
import com.emonics.covid19trackertest.dataClass.User
import com.emonics.covid19trackertest.repository.CovidTrackerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DatabaseViewModel(private val covidTrackerRepository:CovidTrackerRepository):ViewModel() {
    init {
        viewModelScope.launch(Dispatchers.IO) {
            covidTrackerRepository.getCountryDatas()
            covidTrackerRepository.storeCityDetail()
            covidTrackerRepository.storeGlobalDetail()
            covidTrackerRepository.storeUserDetail()
        }
    }

    val countries: LiveData<List<Country>>
    get() =  covidTrackerRepository.countryData

    val cities:LiveData<List<City>>
    get() = covidTrackerRepository.cityData

    val globalData:LiveData<Global>
    get() = covidTrackerRepository.globalData

    val userData:LiveData<List<User>>
    get() = covidTrackerRepository.userDetail
}