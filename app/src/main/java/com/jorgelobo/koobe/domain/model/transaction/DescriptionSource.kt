package com.jorgelobo.koobe.domain.model.transaction

/**
 * Represents the source of a transaction description.
 *
 * This sealed class is used to distinguish between a description explicitly provided by the user
 * and the absence of user input, allowing business logic to apply different resolution strategies.
 */
sealed class DescriptionSource {

    /** Indicates that no description was provided by the user. */
    data object Empty : DescriptionSource()

    data class TextDescription(val text: String) : DescriptionSource()
}