package com.jorgelobo.koobe.ui.components.model

data class AppBarConfig(
    val headline: String,
    val leadingIcon: String,
    val primaryTrailingIcon: String? = null,
    val secondaryTrailingIcon: String? = null
)