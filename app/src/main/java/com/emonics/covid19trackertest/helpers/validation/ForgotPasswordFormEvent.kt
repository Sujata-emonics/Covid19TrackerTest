package com.emonics.covid19trackertest.helpers.validation

sealed class ForgotPasswordFormEvent {

    data class PasswordChangedForgotPassword(val passwordForgotPassword: String) : ForgotPasswordFormEvent()
    data class ConfirmedPasswordChangedForgotPassword(val confirmedPasswordForgotPassword: String) : ForgotPasswordFormEvent()
    object Submit: ForgotPasswordFormEvent()
}