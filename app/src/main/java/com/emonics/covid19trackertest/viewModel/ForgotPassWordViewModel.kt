package com.emonics.covid19trackertest.viewModel

import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emonics.covid19trackertest.dataClass.City
import com.emonics.covid19trackertest.dataClass.Country
import com.emonics.covid19trackertest.dataClass.Global
import com.emonics.covid19trackertest.helpers.validation.RegistrationFormEvent
import com.emonics.covid19trackertest.repository.CovidTrackerRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgotpassword.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class ForgotPassWordViewModel(private var repository: CovidTrackerRepository):ViewModel() {
 private var mAuth: FirebaseAuth? = null
    private val _email = MutableStateFlow("")
    private val stateEmail = MutableLiveData(false)
    init {
            viewModelScope.launch(Dispatchers.IO) {
              //  mAuth = FirebaseAuth.getInstance();
            //repository.sendMailForForgotPassword(mAuth!!,emailEntered.toString())
        }
    }

    val isPasswordChanged:LiveData<String>
    get() = repository.emailLiveData
    val showErrorMessage: LiveData<Boolean>
    get() = stateEmail

     fun onEvent(email:String) {
          mAuth = FirebaseAuth.getInstance();
          val isEmailCorrect = (email!!.isBlank()) || (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
          stateEmail.value = isEmailCorrect
         if(isEmailCorrect){return}
         else {
             viewModelScope.launch(Dispatchers.IO) {
                 repository.sendMailForForgotPassword(
                     mAuth!!,
                     email.toString()
                 )           //  mAuth = FirebaseAuth.getInstance();
                 //repository.sendMailForForgotPassword(mAuth!!,emailEntered.toString())
             }
         }
    }




}