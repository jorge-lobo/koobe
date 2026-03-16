package com.jorgelobo.koobe.ui.components.composed.date

import androidx.compose.runtime.Stable
import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek

@Stable
data class SelectableListConfig(
    val startOfWeek: StartOfWeek,
    val items: List<String>,
    val selectedIndex: Int,
    val onItemSelected: (Int) -> Unit
)