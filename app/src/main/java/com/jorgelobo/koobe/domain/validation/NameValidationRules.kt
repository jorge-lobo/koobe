package com.jorgelobo.koobe.domain.validation

data class NameValidationRules(
    val minLength: Int = 3,
    val mustStartWithLetter: Boolean = true
)