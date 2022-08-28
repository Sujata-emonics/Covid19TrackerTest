package com.emonics.covid19trackertest.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.emonics.covid19trackertest.repository.CovidTrackerRepository

class CityRecyclerViewModelFactory(private val covidTrackerRepository: CovidTrackerRepository,
private val countrySelected:String):
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CityRecyclerViewModel::class.java)) {
            CityRecyclerViewModel(this.covidTrackerRepository, this.countrySelected) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }

}