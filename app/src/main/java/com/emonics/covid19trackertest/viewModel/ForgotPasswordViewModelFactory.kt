package com.emonics.covid19trackertest.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.emonics.covid19trackertest.repository.CovidTrackerRepository

class ForgotPasswordViewModelFactory(private val covidTrackerRepository: CovidTrackerRepository) :ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(ForgotPassWordViewModel::class.java)) {
            return ForgotPassWordViewModel(covidTrackerRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }

}