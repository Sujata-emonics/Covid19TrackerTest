package com.emonics.covid19trackertest.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emonics.covid19trackertest.dataClass.ForgotPasswordFormState
import com.emonics.covid19trackertest.dataClass.RegistrationFormState
import com.emonics.covid19trackertest.helpers.validation.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ForgotPassWordViewModel(
    private val ValidateForgotPassword: ValidateForgotPassword = ValidateForgotPassword(),
    private val validateConfirmedPasswordForgotPassword: ValidateConfirmedPassword = ValidateConfirmedPassword()

):ViewModel() {
    var stateForgotPassword by mutableStateOf(ForgotPasswordFormState())

    var passwordErrorLiveDataForgotPassword = MutableLiveData<String>()
    var repeatPasswordErrorLiveDataForgotPassword = MutableLiveData<String>()

    var validationEventChannelForgotPassword = Channel<ForgotPassWordViewModel.ValidationEventForgotPassword>()
    val validationEvents = validationEventChannelForgotPassword.receiveAsFlow()

    fun onEventForgotPassword(event: ForgotPasswordFormEvent) {
        when(event) {
            is ForgotPasswordFormEvent.PasswordChangedForgotPassword -> {
                stateForgotPassword = stateForgotPassword.copy(passwordForgotPassword = event.passwordForgotPassword)
            }
            is ForgotPasswordFormEvent.ConfirmedPasswordChangedForgotPassword -> {
                stateForgotPassword = stateForgotPassword.copy(confirmedPasswordForgotPassword = event.confirmedPasswordForgotPassword)
            }
            is ForgotPasswordFormEvent.Submit -> {
                submitData()
            }
        }
    }

    fun submitData(){
        val passwordResultForgotPassword = ValidateForgotPassword.execute(stateForgotPassword.passwordForgotPassword)
        val confirmedPasswordResultForgotPassword = ValidateForgotPassword.executeNew(
            stateForgotPassword.passwordForgotPassword, stateForgotPassword.confirmedPasswordForgotPassword
        )
        Log.i("tag","passwordResultForgotPassword "+passwordResultForgotPassword)
        Log.i("tag","confirmedPasswordResultForgotPassword "+confirmedPasswordResultForgotPassword)

        val hasError = listOf(
            passwordResultForgotPassword,
            confirmedPasswordResultForgotPassword
        ).any { !it.successful }
        Log.i("tag","hasError "+hasError)
        if(hasError) {
            stateForgotPassword = stateForgotPassword.copy(
                passwordErrorForgotPassword = passwordResultForgotPassword.errorMessage,
                confirmedPasswordErrorForgotPassword = confirmedPasswordResultForgotPassword.errorMessage,
            )
            passwordErrorLiveDataForgotPassword.value = passwordResultForgotPassword.errorMessage.toString()
            repeatPasswordErrorLiveDataForgotPassword.value = confirmedPasswordResultForgotPassword.errorMessage.toString()

             return
        }

        viewModelScope.launch {
            Log.i("tag","inside success123")
            validationEventChannelForgotPassword.send(ForgotPassWordViewModel.ValidationEventForgotPassword.Success)
        }
    }
    sealed class ValidationEventForgotPassword {
        object Success: ValidationEventForgotPassword()
    }
}