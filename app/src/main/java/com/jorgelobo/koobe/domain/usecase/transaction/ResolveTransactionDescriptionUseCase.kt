package com.jorgelobo.koobe.domain.usecase.transaction

import com.jorgelobo.koobe.domain.model.category.Subcategory
import com.jorgelobo.koobe.domain.model.transaction.DescriptionResolution
import com.jorgelobo.koobe.domain.model.transaction.DescriptionSource
import com.jorgelobo.koobe.domain.model.transaction.Shortcut
import javax.inject.Inject

/**
 * Resolves the final description of a transaction based on the available context.
 *
 * This use case determines whether a transaction description can be:
 * - Used directly from user input
 * - Inferred from the selected subcategory or shortcut
 * - Or requires explicit user confirmation when multiple candidates exist
 *
 * The result of this resolution is used by the UI layer to decide whether the transaction can
 * be saved immediately or if user interaction is required.
 */
class ResolveTransactionDescriptionUseCase @Inject constructor() {

    /**
     * Attempts to resolve a transaction description.
     *
     * Resolution rules:
     * - If the user explicitly entered a description, it is used directly.
     * - Otherwise, available names from the selected subcategory and/or shortcut are collected
     * as candidates.
     * - If no candidates exist, the description is considered missing.
     * - If one or more candidates exist, user confirmation is required.
     *
     * @param descriptionSource The source of the description provided by the user
     * @param subcategory The selected subcategory, if any
     * @param shortcut The selected shortcut, if any
     *
     * @return A [DescriptionResolution] indicating how the description should be handled.
     */
    fun resolve(
        descriptionSource: DescriptionSource?,
        subcategory: Subcategory?,
        shortcut: Shortcut?
    ): DescriptionResolution {
        return when (descriptionSource) {
            is DescriptionSource.TextDescription -> DescriptionResolution.Resolved(descriptionSource.text)

            else -> {
                val candidates = listOfNotNull(
                    subcategory?.name,
                    shortcut?.name
                )

                if (candidates.isEmpty()) {
                    DescriptionResolution.Missing
                } else {
                    DescriptionResolution.RequireUserChoice(candidates)
                }
            }
        }
    }
}