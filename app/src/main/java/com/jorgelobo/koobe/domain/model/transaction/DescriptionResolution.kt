package com.jorgelobo.koobe.domain.model.transaction

sealed interface DescriptionResolution{
    data class Resolved(
        val text: String
    ): DescriptionResolution

    data class RequireUserChoice(
        val candidates: List<String>
    ): DescriptionResolution

    object Missing: DescriptionResolution
}