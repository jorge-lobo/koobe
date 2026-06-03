package com.jorgelobo.koobe.ui.screen.shortcuts.editor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorgelobo.koobe.core.model.resolve
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.transaction.Shortcut
import com.jorgelobo.koobe.domain.repository.CategoryRepository
import com.jorgelobo.koobe.domain.repository.ShortcutRepository
import com.jorgelobo.koobe.ui.screen.shortcuts.editor.state.ShortcutFormState
import com.jorgelobo.koobe.ui.screen.shortcuts.editor.state.ShortcutUiStateInternal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import javax.inject.Inject

@HiltViewModel
class ShortcutEditorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    shortcutRepository: ShortcutRepository,
    categoryRepository: CategoryRepository
): ViewModel() {

    private val _events = MutableSharedFlow<ShortcutEditorEvent>()
    val events = _events.asSharedFlow()

    private val config: ShortcutEditorConfig =
        savedStateHandle.get<String>("config")
            ?.let { URLDecoder.decode(it, "UTF-8") }
            ?.let { Json.decodeFromString<ShortcutEditorConfig>(it) }
            ?: error("Missing ShortcutEditorConfig")

    // State

    private val formState = MutableStateFlow(ShortcutFormState())
    private val uiInternalState = MutableStateFlow(ShortcutUiStateInternal())

    // Data Sources
    private val shortcutFlow: Flow<Shortcut?> =
        if (config.isEditMode) {
            shortcutRepository.getShortcutByIdFlow(config.shortcutId!!)
        } else {
            flowOf(null)
        }

    private val categoryFlow = categoryRepository.getCategoryByIdFlow(config.categoryId)

    // Base State

    private val baseStateFlow: Flow<ShortcutEditorUiState> =
        combine(
            shortcutFlow,
            categoryFlow
        ) { shortcut, category ->
            val safeCategory = category ?: Category.empty()

            if (config.isEditMode && shortcut != null) {
                ShortcutEditorUiState.initialFromShortcut(
                    config = config,
                    shortcut = shortcut,
                    category = safeCategory,
                )
            } else {
                ShortcutEditorUiState.initial(
                    config = config,
                    category = safeCategory
                )
            }
        }

    // Public UI State

    val uiState: StateFlow<ShortcutEditorUiState> =
        combine(
            baseStateFlow,
            formState,
            uiInternalState
        ) { base, form, uiInternal ->
            val updatedName = form.name.resolve(base.name)
            val updatedIcon = form.icon.resolve(base.icon)
            val updatedAmountInput = form.amountInput.resolve(base.amountInput)
            val updatedAmount = updatedAmountInput.toDoubleOrNull() ?: base.amount
            val updatedPaymentMethod = form.paymentMethod.resolve(base.paymentMethodType)
            val updatedCurrency = form.currency.resolve(base.currencyType)
            val updatedRepeat = form.repeat.resolve(base.repeat)
            val updatedRepeatFrequency = form.repeatFrequency.resolve(base.repeatFrequency)

            base.copy(
                name = updatedName,
                icon = updatedIcon,
                amountInput = updatedAmountInput,
                amount = updatedAmount,
                paymentMethodType = updatedPaymentMethod,
                currencyType = updatedCurrency,
                repeat = updatedRepeat,
                repeatFrequency = updatedRepeatFrequency,
                discardDialog = uiInternal.discardDialog,
                deleteDialog = uiInternal.deleteDialog,
                iconSelectDialog = uiInternal.iconSelectDialog,
                currencyDialog = uiInternal.currencyDialog,
                paymentMethodSelector = uiInternal.paymentMethodSelector,
                periodSelector = uiInternal.periodSelector,
                isSaving = uiInternal.isSaving,
                isDeleting = uiInternal.isDeleting,
                isLoading = uiInternal.isSaving || uiInternal.isDeleting
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = ShortcutEditorUiState.initialEmpty()
            )
}