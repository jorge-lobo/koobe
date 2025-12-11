package com.jorgelobo.koobe.ui.screen.categories.selector

sealed class SelectorStep {
    object SelectCategory : SelectorStep()
    object SelectSubcategory : SelectorStep()
}