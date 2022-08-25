package com.emonics.covid19trackertest.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.emonics.covid19trackertest.repository.CovidTrackerRepository

class DatabaseViewModelFactory(private val covidTrackerRepository: CovidTrackerRepository) : ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(DatabaseViewModel::class.java)) {
            return DatabaseViewModel(covidTrackerRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}