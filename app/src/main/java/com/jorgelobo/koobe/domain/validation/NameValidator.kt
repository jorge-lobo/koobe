package com.jorgelobo.koobe.domain.validation

import javax.inject.Inject

class NameValidator @Inject constructor() {

    /**
     * Validates a name against the given rules, throwing on the first violation.
     *
     * The name is trimmed before validation.
     *
     * @throws NameValidationException.Empty if the trimmed name is blank.
     * @throws NameValidationException.TooShort if below [NameValidationRules.minLength].
     * @throws NameValidationException.InvalidFirstCharacter if [NameValidationRules.mustStartWithLetter] is true and the first character is not a letter.
     */
    fun validate(name: String, rules: NameValidationRules) {
        val trimmed = name.trim()

        if (trimmed.isBlank()) throw NameValidationException.Empty()
        if (trimmed.length < rules.minLength) throw NameValidationException.TooShort()
        if (rules.mustStartWithLetter && !trimmed.first()
                .isLetter()
        ) throw NameValidationException.InvalidFirstCharacter()
    }
}