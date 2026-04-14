package com.jorgelobo.koobe.ui.components.base.snackbar

import androidx.annotation.StringRes
import com.jorgelobo.koobe.ui.components.model.icons.IconPack

data class SnackBarConfig(
    @field:StringRes val messageRes: Int,
    @field:StringRes val actionLabelRes: Int? = null,
    val icon: IconPack? = null,
    val onActionClick: (() -> Unit)? = null,
    val onIconClick: (() -> Unit)? = null
)