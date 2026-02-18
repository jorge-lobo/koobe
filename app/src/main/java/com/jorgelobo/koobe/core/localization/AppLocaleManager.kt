package com.jorgelobo.koobe.core.localization

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.jorgelobo.koobe.domain.model.constants.enums.AppLanguage

/**
 * Updates the application's language settings across the entire app.
 *
 * This function converts the provided [AppLanguage] into a [LocaleListCompat] and applies it
 * using [AppCompatDelegate.setApplicationLocales]. This ensures that the UI components are
 * refreshed with the correct localized resources.
 *
 * @param language The [AppLanguage] enum representing the target language to be set.
 */
fun setAppLanguage(language: AppLanguage) {
    val locale = language.toLocale()
    val appLocale = LocaleListCompat.create(locale)

    AppCompatDelegate.setApplicationLocales(appLocale)
}