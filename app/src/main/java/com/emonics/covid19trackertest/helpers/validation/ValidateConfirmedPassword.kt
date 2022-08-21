package com.emonics.covid19trackertest.helpers.validation

import com.emonics.covid19trackertest.dataClass.ValidationResult

class ValidateConfirmedPassword {
    fun execute(passwordSignUP: String, confirmedPasswordSignUP: String): ValidationResult {
        if(passwordSignUP != confirmedPasswordSignUP) {
            return ValidationResult(
                successful = false,
                errorMessage = "The passwords don't match"
            )
        }
        return ValidationResult(
            successful = true,
            errorMessage = ""

        )
    }
}