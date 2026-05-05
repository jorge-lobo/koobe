package com.jorgelobo.koobe.domain.validation

import javax.inject.Inject

class NameValidator @Inject constructor() {

    fun validate(name: String, rules: NameValidationRules) {
        val trimmed = name.trim()

        if (trimmed.isBlank()) throw NameValidationException.Empty()
        if (trimmed.length < rules.minLength) throw NameValidationException.TooShort()
        if (rules.mustStartWithLetter && !trimmed.first()
                .isLetter()
        ) throw NameValidationException.InvalidFirstCharacter()
    }
}