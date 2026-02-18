package com.jorgelobo.koobe.core.localization

import com.jorgelobo.koobe.domain.model.constants.enums.AppLanguage
import java.util.Locale

/**
 * Converts the [AppLanguage] enum constant to its corresponding [Locale] object.
 *
 * @return The [Locale] associated with the application language, specifically mapping
 * Portuguese to the "pt-PT" language tag.
 */
fun AppLanguage.toLocale(): Locale = when (this) {
    AppLanguage.ENGLISH -> Locale.ENGLISH
    AppLanguage.PORTUGUESE -> Locale.forLanguageTag("pt-PT")
}