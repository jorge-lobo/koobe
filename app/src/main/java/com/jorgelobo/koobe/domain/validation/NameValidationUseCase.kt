package com.jorgelobo.koobe.domain.validation

import javax.inject.Inject

class NameValidationUseCase @Inject constructor(
    private val nameValidator: NameValidator
) {

    fun <T> validate(
        name: String,
        currentId: Int,
        existingItems: List<T>,
        extractId: (T) -> Int,
        extractName: (T) -> String,
        normalize: (String) -> String,
        rules: NameValidationRules = NameValidationRules()
    ) {
        nameValidator.validate(name, rules)

        val normalizedInput = normalize(name)

        val exists = existingItems.any { item ->
            extractId(item) != currentId && normalize(extractName(item)) == normalizedInput
        }

        if (exists) throw NameValidationException.Duplicate()
    }
}