package com.jorgelobo.koobe.domain.validation

import javax.inject.Inject

// Extends [NameValidator] with a duplicate check across a generic collection.
class NameValidationUseCase @Inject constructor(
    private val nameValidator: NameValidator
) {

    /**
     * Validates a name for uniqueness within a collection, in addition to structural rules.
     *
     * @param name The candidate name to validate.
     * @param currentId The ID of the item being edited, excluded from the duplicate check.
     * @param existingItems The collection to check for duplicates.
     * @param extractId Extracts the ID from an item.
     * @param extractName Extracts the raw name from an item.
     * @param normalize Normalises both the input and existing names before comparison (e.g. lowercase, trim).
     * @param rules Structural validation rules applied before the duplicate check.
     *
     * @throws NameValidationException.Duplicate if a different item with the same normalised name exists.
     * @throws NameValidationException see [NameValidator.validate] for structural violations.
     */
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