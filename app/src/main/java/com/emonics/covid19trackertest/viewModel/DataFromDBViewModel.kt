package com.emonics.covid19trackertest.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emonics.covid19trackertest.dataClass.Country
import com.emonics.covid19trackertest.dataClass.User
import com.emonics.covid19trackertest.helpers.retrofit.RetroInstance
import com.emonics.covid19trackertest.helpers.retrofit.RetroServiceInterFace
import com.emonics.covid19trackertest.repository.CovidTrackerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DataFromDBViewModel(private var repository: CovidTrackerRepository): ViewModel() {
    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getUserDetailFromDB()
        }
    }

     val userRecordFromDb:LiveData<List<User>>
        get() = repository.userRecordFromDb

 }