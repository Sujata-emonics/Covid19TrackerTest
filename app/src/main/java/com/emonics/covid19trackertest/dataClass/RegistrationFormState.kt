package com.emonics.covid19trackertest.dataClass

data class RegistrationFormState(
    val email:String="",
    val emailError:String?=null,
    val password:String="",
    val passwordError:String?="",
    val emailSignUp:String="",
    val emailErrorSignUp:String?=null,
     val passwordSignUp:String="",
    val passwordErrorSignUp:String?="",
    val confirmedPasswordSignUp:String="",
    val confirmedPasswordErrorSignUp:String?="",
    val repeatPassword:String="",
    val repeatedPasswordError:String?=null,

)
