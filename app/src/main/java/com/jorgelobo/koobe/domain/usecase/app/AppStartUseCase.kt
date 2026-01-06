package com.jorgelobo.koobe.domain.usecase.app

import com.jorgelobo.koobe.data.local.defaults.CategoryDefaults
import com.jorgelobo.koobe.data.local.defaults.SubcategoryDefaults
import com.jorgelobo.koobe.data.local.preferences.AppPreferences
import com.jorgelobo.koobe.domain.repository.CategoryRepository
import com.jorgelobo.koobe.domain.repository.SubcategoryRepository
import kotlinx.coroutines.flow.first

class AppStartUseCase(
    private val preferences: AppPreferences,
    private val categoryRepository: CategoryRepository,
    private val subcategoryRepository: SubcategoryRepository
) {

    suspend operator fun invoke() {
        if (preferences.defaultsAlreadyInserted()) return

        val categoriesExist = categoryRepository.getAllCategories().first().isNotEmpty()
        val subcategoriesExist = subcategoryRepository.getAllSubcategories().first().isNotEmpty()

        if (!categoriesExist) {
            categoryRepository.insertCategoryEntities(CategoryDefaults.categories)
        }

        if (!subcategoriesExist) {
            subcategoryRepository.insertSubcategoryEntities(SubcategoryDefaults.subcategories)
        }

        preferences.setDefaultsInserted()
    }
}