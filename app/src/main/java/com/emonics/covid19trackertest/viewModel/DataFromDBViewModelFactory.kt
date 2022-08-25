package com.emonics.covid19trackertest.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.emonics.covid19trackertest.repository.CovidTrackerRepository

class DataFromDBViewModelFactory( private val covidTrackerRepository: CovidTrackerRepository):ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(DataFromDBViewModel::class.java)) {
            return DataFromDBViewModel(covidTrackerRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}