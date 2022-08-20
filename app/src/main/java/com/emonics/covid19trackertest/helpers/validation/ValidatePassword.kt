package com.emonics.covid19trackertest.helpers.validation

import android.util.Patterns
import com.emonics.covid19trackertest.dataClass.ValidationResult

class ValidatePassword {
    /*fun execute(password:String): Boolean {
        val containsLettersAndDigits = password.any { it.isDigit() } &&
                password.any { it.isLetter() }

        return (password.length < 8) && (!containsLettersAndDigits)

    }*/

    fun execute(password: String): ValidationResult {
        if(password.length < 8) {
            return ValidationResult(
                successful = false,
                errorMessage = "The password needs to consist of at least 8 characters"
            )
        }
        val containsLettersAndDigits = password.any { it.isDigit() } &&
                password.any { it.isLetter() }
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

