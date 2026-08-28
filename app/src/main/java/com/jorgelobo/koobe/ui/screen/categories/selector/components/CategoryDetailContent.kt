package com.jorgelobo.koobe.ui.screen.categories.selector.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.jorgelobo.koobe.ui.components.base.buttons.types.ButtonText
import com.jorgelobo.koobe.ui.components.composed.emptyState.EmptyStateContent
import com.jorgelobo.koobe.ui.components.composed.emptyState.EmptyStateContentConfig
import com.jorgelobo.koobe.ui.components.model.enums.EmptyStateIconType
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.color.LightThemeGrey2
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

/**
 * Displays the detailed content for a category, either subcategories or shortcuts.
 *
 * This composable handles:
 * - Rendering an empty state UI when no items are present.
 * - Displaying a create button to add new items when the list is not empty.
 * - Providing a slot for the main content list.
 *
 * @param isEmpty Whether the detail list is empty.
 * @param emptyHeadlineRes String resource for the empty state headline.
 * @param emptyHintRes String resource for the empty state hint message.
 * @param createButtonRes String resource for the create button text.
 * @param onCreateClick Callback for when the create button is clicked.
 * @param content Slot for the main content list.
 */
@Composable
fun CategoryDetailContent(
    isEmpty: Boolean,
    emptyHeadlineRes: Int,
    emptyHintRes: Int,
    createButtonRes: Int,
    onCreateClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isEmpty) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(Spacing.Giant))

                EmptyStateContent(
                    config = EmptyStateContentConfig(
                        message = stringResource(emptyHeadlineRes),
                        icon = IconPack.EMPTY,
                        iconTint = LightThemeGrey2,
                        iconType = EmptyStateIconType.BACKGROUND
                    )
                )
            }
        } else {
            content()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                ButtonText(
                    onClick = onCreateClick,
                    enabled = true,
                    text = stringResource(createButtonRes),
                    textColor = AppTheme.colors.buttonColors.buttonTextDefault,
                    icon = IconPack.ADD
                )
            }
        }
    }
}