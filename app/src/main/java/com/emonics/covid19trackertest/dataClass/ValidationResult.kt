package com.emonics.covid19trackertest.dataClass

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null
)
