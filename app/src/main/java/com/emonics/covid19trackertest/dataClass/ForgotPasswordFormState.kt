package com.emonics.covid19trackertest.dataClass

data class ForgotPasswordFormState(
    val passwordForgotPassword:String="",
    val passwordErrorForgotPassword:String?="",
    val confirmedPasswordForgotPassword:String="",
    val confirmedPasswordErrorForgotPassword:String?="",
)
