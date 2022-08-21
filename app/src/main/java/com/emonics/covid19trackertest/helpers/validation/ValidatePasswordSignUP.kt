package com.emonics.covid19trackertest.helpers.validation

import com.emonics.covid19trackertest.dataClass.ValidationResult

class ValidatePasswordSignUP {
    fun execute(passwordSignUP: String): ValidationResult {
        if(passwordSignUP.length < 8) {
            return ValidationResult(
                successful = false,
                errorMessage = "The password needs to consist of at least 8 characters"
            )
        }
        val containsLettersAndDigits = passwordSignUP.any { it.isDigit() } &&
                passwordSignUP.any { it.isLetter() }
        if(!containsLettersAndDigits) {
            return ValidationResult(
                successful = false,
                errorMessage = "The password needs to contain at least one letter and digit"
            )
        }
        return ValidationResult(
            successful = true,
            errorMessage = ""
        )
    }
}