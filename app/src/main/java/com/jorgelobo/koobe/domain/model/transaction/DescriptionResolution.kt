package com.jorgelobo.koobe.domain.model.transaction

/**
 * Represents the result of attempting to resolve a transaction description.
 *
 * This sealed interface is used by the domain layer to indicate whether a transaction description
 * could be resolved automatically, requires user confirmation, or is missing altogether.
 *
 * The UI layer reacts to each case by either saving immediately, prompting the user with a choice,
 * or preventing the save action.
 */
sealed interface DescriptionResolution{
    data class Resolved(
        val text: String
    ): DescriptionResolution

    data class RequireUserChoice(
        val candidates: List<String>
    ): DescriptionResolution

    object Missing: DescriptionResolution
}