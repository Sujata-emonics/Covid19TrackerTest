package com.emonics.covid19trackertest.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emonics.covid19trackertest.dataClass.Country
import com.emonics.covid19trackertest.dataClass.Global
import com.emonics.covid19trackertest.repository.DetailViewRepository
import kotlinx.coroutines.launch

class DetailViewModel(private var detailViewRepository: DetailViewRepository,var countrySelected:String) : ViewModel() {


    init {
        viewModelScope.launch {
            detailViewRepository.getCountryDetail(countrySelected)

        }
    }

    val globalRecord: LiveData<Global>
    get() = detailViewRepository.detailGlobalLivedata
    fun getGlobalRecord(){
        viewModelScope.launch {
            detailViewRepository.getDetailGlobalRecord()
        }

    }




    val countryRecord: LiveData<Country>
    get() = detailViewRepository.liveCountryDetailView
     fun getCountryDetail(selectedCountry:String){
         viewModelScope.launch {
             detailViewRepository.getCountryDetail(selectedCountry)
         }

    }
}