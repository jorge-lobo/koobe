package com.jorgelobo.koobe.ui.components.model

import androidx.annotation.StringRes

data class SnackBarConfig(
    @field:StringRes val messageRes: Int,
    @field:StringRes val actionLabelRes: Int? = null,
    val icon: IconName? = null,
    val onActionClick: (() -> Unit)? = null,
    val onIconClick: (() -> Unit)? = null
)
