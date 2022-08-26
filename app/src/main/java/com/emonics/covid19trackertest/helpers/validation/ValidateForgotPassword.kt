package com.emonics.covid19trackertest.helpers.validation

import android.util.Patterns
import com.emonics.covid19trackertest.dataClass.ValidationResult

class ValidateForgotPassword {
        fun execute(email: String): ValidationResult {
            if (email.isBlank()) {
                return ValidationResult(
                    successful = false,
                    errorMessage = "The email can't be blank"
                )
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                return ValidationResult(
                    successful = false,
                    errorMessage = "That's not a valid email"
                )
            }
            return ValidationResult(
                successful = true,
                errorMessage = ""
            )
        }
    }
