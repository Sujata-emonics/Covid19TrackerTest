package com.emonics.covid19trackertest.helpers.validation

import com.emonics.covid19trackertest.dataClass.ValidationResult

class ValidateRepeatePassword {
    /*fun execute(password: String, repeatedPassword: String): Boolean {
        return (password != repeatedPassword)
    }*/
    fun execute(password: String, repeatedPassword: String): ValidationResult {
        if(password != repeatedPassword) {
            return ValidationResult(
                //successful = false,
                successful = true,
                errorMessage = "The passwords don't match"
            )
        }
        return ValidationResult(
            successful = true,
            errorMessage = ""

        )
    }
}