package com.emonics.covid19trackertest.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.emonics.covid19trackertest.repository.DetailViewRepository

class DetailViewFactory(private val detailViewRepository: DetailViewRepository,private val countrySelected: String):ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)){
            return DetailViewModel(this.detailViewRepository,this.countrySelected) as T
        }
        return throw IllegalArgumentException("ViewModel Not Found")

    }
}
