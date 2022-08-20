package com.emonics.covid19trackertest.dataClass

data class RegistrationFormState(
    val email:String="",
    val emailError:String?=null,
    val password:String="",
    val passwordError:String?="",
    val repeatPassword:String="",
    val repeatedPasswordError:String?=null,
    val acceptTerms:Boolean = false,
    val termsError:String?= null



)
