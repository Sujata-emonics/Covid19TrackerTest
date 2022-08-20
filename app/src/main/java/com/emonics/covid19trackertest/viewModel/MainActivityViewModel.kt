package com.emonics.covid19trackertest.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel:ViewModel(){
    /* Implementation for switch menu on LogIn Page*/
    var toggle = MutableLiveData<String>()
    init {
        toggle.value = "SignIn"
    }
    fun changeToggle(toggleValue:String) {
        //when(toggle.value){
            toggle.value = toggleValue
            //else -> toggle.value = "SignUp"
       // }
    }
    /*-----End-----*/





}