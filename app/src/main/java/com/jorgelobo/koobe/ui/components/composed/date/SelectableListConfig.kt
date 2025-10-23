package com.jorgelobo.koobe.ui.components.composed.date

import androidx.compose.runtime.Stable

@Stable
data class SelectableListConfig(
    val items: List<String>,
    val selectedIndex: Int,
    val onItemSelected: (Int) -> Unit
)