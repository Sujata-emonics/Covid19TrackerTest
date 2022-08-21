package com.emonics.covid19trackertest.helpers.validation

import android.util.Patterns
import com.emonics.covid19trackertest.dataClass.ValidationResult

class ValidateEmailSignUP {
    fun execute(emailSignUP: String): ValidationResult {
        if(emailSignUP.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "The email can't be blank"
            )
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(emailSignUP).matches()) {
            return ValidationResult(
                successful = false,
                errorMessage = "That's not a valid email"
            )
        }
        return ValidationResult(
            successful = true,
            errorMessage =""
        )
    }
}