package com.emonics.covid19trackertest.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emonics.covid19trackertest.dataClass.RegistrationFormState
import com.emonics.covid19trackertest.helpers.validation.RegistrationFormEvent
import com.emonics.covid19trackertest.helpers.validation.ValidateEmail
import com.emonics.covid19trackertest.helpers.validation.ValidatePassword
import com.emonics.covid19trackertest.helpers.validation.ValidateRepeatePassword
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SignInValidationViewModel(
    private val validateEmail: ValidateEmail = ValidateEmail(),
    private val validatePassword: ValidatePassword = ValidatePassword(),
    private val validateRepeatedPassword: ValidateRepeatePassword = ValidateRepeatePassword()
):ViewModel() {
    var state by mutableStateOf(RegistrationFormState())
    var emailErrorLiveData = MutableLiveData<String>()
    var passwordErrorLiveData = MutableLiveData<String>()
    var repeatPasswordErrorLiveData = MutableLiveData<String>()
    //var errorState = MutableLiveData<RegistrationFormState>(RegistrationFormState())

    //var state by MutableLiveData<RegistrationFormState>()
   var validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()
    fun onEvent(event: RegistrationFormEvent) {
        Log.i("tag","=="+event)
        when(event) {
            is RegistrationFormEvent.EmailChanged -> {
                Log.i("tag","++++"+event)

                state = state.copy(email = event.email)
            }
            is RegistrationFormEvent.PasswordChanged -> {
                state = state.copy(password = event.password)
            }
            is RegistrationFormEvent.RepeatedPasswordChanged -> {
                state = state.copy(repeatPassword = event.repeatedPassword)
            }

            is RegistrationFormEvent.Submit -> {
                submitData()
            }
        }
    }

    private fun submitData() {

        val emailResult = validateEmail.execute(state.email)
        val passwordResult = validatePassword.execute(state.password)
        val repeatedPasswordResult = validateRepeatedPassword.execute(
            state.password, state.repeatPassword
        )
       // val hasError = listOf<Array>()
        val hasError = listOf(
            emailResult,
            passwordResult,
            repeatedPasswordResult
        ).any { !it.successful }
        Log.i("tag","emailResult "+emailResult+" passwordResult  "+state.email+"--repeatedPasswordResult--"+repeatedPasswordResult)
        Log.i("tag","inside success"+hasError)

        if(hasError) {
            state = state.copy(
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage,
                repeatedPasswordError = repeatedPasswordResult.errorMessage,
                //termsError = termsResult.errorMessage
                //termsError = termsResult.errorMessage
            )
            emailErrorLiveData.value = emailResult.errorMessage.toString()
            passwordErrorLiveData.value = passwordResult.errorMessage.toString()
            repeatPasswordErrorLiveData.value = repeatedPasswordResult.errorMessage.toString()

            Log.i("tag","val hasError"+emailErrorLiveData.value+" passwordResult  "+passwordResult.errorMessage.toString()+" repeatedPasswordResult "+repeatPasswordErrorLiveData.value)
            //Log.i("tag","hasError"+hasError+"emailError"+state.emailError)
            return
        }
        Log.i("tag","inside success")
        viewModelScope.launch {
           Log.i("tag","inside success123")
            validationEventChannel.send(ValidationEvent.Success)
        }
    }

    sealed class ValidationEvent {
        object Success: ValidationEvent()
    }
}
