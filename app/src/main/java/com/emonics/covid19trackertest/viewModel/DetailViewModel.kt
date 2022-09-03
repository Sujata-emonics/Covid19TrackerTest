package com.emonics.covid19trackertest.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emonics.covid19trackertest.dataClass.City
import com.emonics.covid19trackertest.dataClass.Country
import com.emonics.covid19trackertest.dataClass.Global
import com.emonics.covid19trackertest.repository.DetailViewRepository
import kotlinx.coroutines.launch

class DetailViewModel(private var detailViewRepository: DetailViewRepository,var countrySelected:String) : ViewModel() {


    init {
        viewModelScope.launch {
            detailViewRepository.getCountryDetail(countrySelected)
            detailViewRepository.getCitiesDetailBasedonCountry(countrySelected)

        }
    }

    val globalRecord: LiveData<Global>
    get() = detailViewRepository.detailGlobalLivedata

    val allCountryRecord: LiveData<List<Country>>
        get() = detailViewRepository.liveAllCountryDetailView
    val errormsg:LiveData<String>
    get() =  detailViewRepository.errorMsgCountryDetailViewLiveDataForAdmin
    fun getGlobalRecord(){
        viewModelScope.launch {
            detailViewRepository.getDetailGlobalRecord()
            detailViewRepository.getAllCountryDetail()
        }

    }

   val countryRecord: LiveData<Country>
    get() = detailViewRepository.liveCountryDetailView

    val cityRecord: LiveData<List<City>>
    get() = detailViewRepository.citiDetailLiveData
     fun getCountryDetail(selectedCountry:String){
         viewModelScope.launch {
             detailViewRepository.getCountryDetail(selectedCountry)
             detailViewRepository.getCitiesDetailBasedonCountry(selectedCountry)

         }

    }


}