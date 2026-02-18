package com.jorgelobo.koobe.core.localization

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.jorgelobo.koobe.domain.model.constants.enums.AppLanguage

/**
 * A Composable wrapper that provides a localized [LocalContext] to its [content].
 *
 * This function creates a new configuration context based on the provided [language], allowing the
 * [content] to access resources (strings, plurals, etc.) for that specific locale regardless of
 * the system's current language settings.
 *
 * @param language The [AppLanguage] to be applied to the localized context.
 * @param content The Composable content that will consume the localized context.
 */
@SuppressLint("LocalContextConfigurationRead")
@Composable
fun LocalizedContent(
    language: AppLanguage,
    content: @Composable () -> Unit
) {
    val baseContext = LocalContext.current

    val localizedContext = remember(language) {
        val locale = language.toLocale()
        val config = Configuration(baseContext.resources.configuration)
        config.setLocale(locale)
        baseContext.createConfigurationContext(config)
    }

    CompositionLocalProvider(
        value = LocalContext provides localizedContext,
        content = content
    )
}