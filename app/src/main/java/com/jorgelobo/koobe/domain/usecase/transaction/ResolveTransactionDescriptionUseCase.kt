package com.jorgelobo.koobe.domain.usecase.transaction

import com.jorgelobo.koobe.domain.model.category.Subcategory
import com.jorgelobo.koobe.domain.model.transaction.DescriptionResolution
import com.jorgelobo.koobe.domain.model.transaction.DescriptionSource
import com.jorgelobo.koobe.domain.model.transaction.Shortcut
import javax.inject.Inject

class ResolveTransactionDescriptionUseCase @Inject constructor() {

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