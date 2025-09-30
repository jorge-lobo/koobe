package com.jorgelobo.koobe.ui.components.model

import com.jorgelobo.koobe.domain.model.constants.UiLabel

data class TabConfig<T>(
    val options: List<T>,
    val selectedOption: T,
    val onSelectionChanged: (T) -> Unit
) where T : Enum<T>, T : UiLabel