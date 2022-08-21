package com.emonics.covid19trackertest.helpers.validation

sealed class RegistrationFormEvent{
    data class EmailChanged(val email: String) : RegistrationFormEvent()
    data class PasswordChanged(val password: String) : RegistrationFormEvent()
    data class EmailChangedSignUp(val emailSignUp: String) : RegistrationFormEvent()
    data class PasswordChangedSighUp(val passwordSignUp: String) : RegistrationFormEvent()
    data class ConfirmedPasswordChangedSighUp(val confirmedPasswordSignUp: String) : RegistrationFormEvent()
    data class RepeatedPasswordChanged(
        val repeatedPassword: String
    ) : RegistrationFormEvent()
    object Submit: RegistrationFormEvent()
}
