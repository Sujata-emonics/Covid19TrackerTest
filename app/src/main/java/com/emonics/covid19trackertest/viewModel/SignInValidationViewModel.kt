package com.emonics.covid19trackertest.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emonics.covid19trackertest.dataClass.RegistrationFormState
import com.emonics.covid19trackertest.helpers.validation.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SignInValidationViewModel(
    private val validateEmail: ValidateEmail = ValidateEmail(),
    private val validatePassword: ValidatePassword = ValidatePassword(),
    private val validateRepeatedPassword: ValidateRepeatePassword = ValidateRepeatePassword(),
    private val validateEmailSignUp: ValidateEmailSignUP = ValidateEmailSignUP(),
    private val validatePasswordSignUp: ValidatePasswordSignUP = ValidatePasswordSignUP(),
    private val validateConfirmedPasswordSignUp:ValidateConfirmedPassword = ValidateConfirmedPassword()

):ViewModel() {
    var state by mutableStateOf(RegistrationFormState())
    var emailErrorLiveData = MutableLiveData<String>()
    var passwordErrorLiveData = MutableLiveData<String>()
    var repeatPasswordErrorLiveData = MutableLiveData<String>()
    var emailErrorLiveDataSignUp = MutableLiveData<String>()
    var passwordErrorLiveDataSignUp = MutableLiveData<String>()
    var confirmedPasswordErrorLiveDataSignUp = MutableLiveData<String>()
    //var errorState = MutableLiveData<RegistrationFormState>(RegistrationFormState())

    //var state by MutableLiveData<RegistrationFormState>()
   var validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()
    fun onEvent(event: RegistrationFormEvent,selectedOption:String="SignIn") {
        when(event) {
            is RegistrationFormEvent.EmailChanged -> {
                Log.i("tag","++++"+event)

                state = state.copy(email = event.email)
            }
            is RegistrationFormEvent.PasswordChanged -> {
                state = state.copy(password = event.password)
            }
           is RegistrationFormEvent.EmailChangedSignUp -> {
                Log.i("tag","++++"+event)

                state = state.copy(emailSignUp = event.emailSignUp)
            }
            is RegistrationFormEvent.PasswordChangedSighUp -> {
                state = state.copy(passwordSignUp = event.passwordSignUp)
            }
           is RegistrationFormEvent.ConfirmedPasswordChangedSighUp -> {
                state = state.copy(confirmedPasswordSignUp = event.confirmedPasswordSignUp)
            }
            is RegistrationFormEvent.RepeatedPasswordChanged -> {
                state = state.copy(repeatPassword = event.repeatedPassword)
            }

            is RegistrationFormEvent.Submit -> {
                submitData(selectedOption)
            }
        }
    }

    private fun submitData(selectedOption:String) {
        if(selectedOption == "SignUp"){
            val emailResultSignUp = validateEmailSignUp.execute(state.emailSignUp)
            val passwordResultSignUp = validatePasswordSignUp.execute(state.passwordSignUp)
            val confirmedPasswordResultSighUp = validateConfirmedPasswordSignUp.execute(
                state.passwordSignUp, state.confirmedPasswordSignUp
            )
            val hasErrorSignUp = listOf(
                emailResultSignUp,
                passwordResultSignUp,
                confirmedPasswordResultSighUp
            ).any { !it.successful }
            if(hasErrorSignUp) {
                state = state.copy(
                    emailErrorSignUp = emailResultSignUp.errorMessage,
                    passwordErrorSignUp = passwordResultSignUp.errorMessage,
                    confirmedPasswordErrorSignUp = confirmedPasswordResultSighUp.errorMessage,
                 )
                emailErrorLiveData.value = ""
                passwordErrorLiveData.value = ""
                repeatPasswordErrorLiveData.value = ""

                emailErrorLiveDataSignUp.value = emailResultSignUp.errorMessage.toString()
                passwordErrorLiveDataSignUp.value = passwordResultSignUp.errorMessage.toString()
                confirmedPasswordErrorLiveDataSignUp.value = confirmedPasswordResultSighUp.errorMessage.toString()

                Log.i("tag","val hasError"+emailErrorLiveDataSignUp.value+" passwordErrorLiveDataSignUp  "+passwordErrorLiveDataSignUp.toString()+" confirmedPasswordErrorLiveDataSignUp "+confirmedPasswordErrorLiveDataSignUp.value)
                //Log.i("tag","hasError"+hasError+"emailError"+state.emailError)
                return
            }

        } else {
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

                emailErrorLiveDataSignUp.value =""
                passwordErrorLiveDataSignUp.value = ""
                confirmedPasswordErrorLiveDataSignUp.value = ""

                Log.i("tag","val hasError"+emailErrorLiveData.value+" passwordResult  "+passwordResult.errorMessage.toString()+" repeatedPasswordResult "+repeatPasswordErrorLiveData.value)
                //Log.i("tag","hasError"+hasError+"emailError"+state.emailError)
                return
            }
        }
        viewModelScope.launch {
           Log.i("tag","inside success123")
            validationEventChannel.send(ValidationEvent.Success)
        }
    }

    sealed class ValidationEvent {
        object Success: ValidationEvent()
    }
}
