package com.emonics.covid19trackertest.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.emonics.covid19trackertest.repository.CovidTrackerRepository

class AdminViewModelFactory(private val covidTrackerRepository: CovidTrackerRepository):
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
     return   if (modelClass.isAssignableFrom(AdminViewModel::class.java)){
            AdminViewModel(this.covidTrackerRepository) as T
        }else{
            throw IllegalArgumentException("ViewModel Not Found")
        }

        return super.create(modelClass)
    }

}