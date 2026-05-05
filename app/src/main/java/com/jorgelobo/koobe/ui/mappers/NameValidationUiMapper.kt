package com.jorgelobo.koobe.ui.mappers

import com.jorgelobo.koobe.domain.validation.NameValidationException
import com.jorgelobo.koobe.R

fun NameValidationException.toMessageRes(): Int = when (this) {
    is NameValidationException.Empty -> R.string.snackBar_empty_name
    is NameValidationException.TooShort -> R.string.snackBar_name_too_short
    is NameValidationException.InvalidFirstCharacter -> R.string.snackBar_invalid_first_character
    is NameValidationException.Duplicate -> R.string.snackBar_duplicate_name
}