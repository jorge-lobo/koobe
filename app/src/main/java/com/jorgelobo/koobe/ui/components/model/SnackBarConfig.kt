package com.jorgelobo.koobe.ui.components.model

import androidx.annotation.StringRes
import com.jorgelobo.koobe.ui.components.model.icons.IconGeneral

data class SnackBarConfig(
    @field:StringRes val messageRes: Int,
    @field:StringRes val actionLabelRes: Int? = null,
    val icon: IconGeneral? = null,
    val onActionClick: (() -> Unit)? = null,
    val onIconClick: (() -> Unit)? = null
)
