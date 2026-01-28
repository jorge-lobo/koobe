package com.jorgelobo.koobe.domain.model.transaction

sealed class DescriptionSource {
    data object Empty : DescriptionSource()
    data class TextDescription(val text: String) : DescriptionSource()
}