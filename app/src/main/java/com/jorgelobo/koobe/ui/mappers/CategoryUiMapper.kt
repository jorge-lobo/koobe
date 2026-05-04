package com.jorgelobo.koobe.ui.mappers

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.category.Category
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

private val categoryNameMap = mapOf(
    "category_home" to R.string.category_home,
    "category_grocery" to R.string.category_grocery,
    "category_dining" to R.string.category_dining,
    "category_transportation" to R.string.category_transportation,
    "category_health" to R.string.category_health,
    "category_apparel" to R.string.category_apparel,
    "category_technology" to R.string.category_technology,
    "category_entertainment" to R.string.category_entertainment,
    "category_education" to R.string.category_education,
    "category_travel" to R.string.category_travel,
    "category_essentials" to R.string.category_essentials,
    "category_pets" to R.string.category_pets,
    "category_body_care" to R.string.category_body_care,
    "category_sports" to R.string.category_sports,
    "category_family" to R.string.category_family,
    "category_miscellaneous" to R.string.category_miscellaneous,
    "category_work" to R.string.category_work,
    "category_passive" to R.string.category_passive,
    "category_support" to R.string.category_support,
    "category_investments" to R.string.category_investments,
    "category_sales" to R.string.category_sales,
    "category_extra" to R.string.category_extra
)

@Composable
fun Category.localizedName(): String {
    val resId = categoryNameMap[this.name]
    return resId?.let { stringResource(it) } ?: this.name
}

class CategoryNameResolver @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    fun resolve(name: String): String {
        val resId = categoryNameMap[name]
        return resId?.let { context.getString(it) } ?: name
    }
}