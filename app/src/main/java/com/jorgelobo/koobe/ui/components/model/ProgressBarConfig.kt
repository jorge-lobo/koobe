package com.jorgelobo.koobe.ui.components.model

import kotlin.math.roundToInt

data class ProgressBarConfig(
    val progress: Float,
    val projection: Float,
    val percentageLabel: String = progress.toPercentageLabel()
)

private fun Float.toPercentageLabel(): String =
    "${(this * 100).roundToInt()}%"