package com.jorgelobo.koobe.ui.screen.categories.selector

/**
 * Represents the different steps in the category and subcategory selection process.
 * This sealed class is used to control the flow and state of the category selector UI.
 */
sealed class SelectorStep {
    object SelectCategory : SelectorStep()
    object SelectSubcategory : SelectorStep()
}