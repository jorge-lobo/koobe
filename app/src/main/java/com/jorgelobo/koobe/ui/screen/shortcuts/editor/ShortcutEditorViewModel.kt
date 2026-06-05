package com.jorgelobo.koobe.ui.screen.shortcuts.editor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorgelobo.koobe.core.model.resolve
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.model.transaction.Shortcut
import com.jorgelobo.koobe.domain.repository.CategoryRepository
import com.jorgelobo.koobe.domain.repository.ShortcutRepository
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.screen.common.bottomSheet.selector.SelectorSheetAction
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogAction
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogAction
import com.jorgelobo.koobe.ui.screen.shortcuts.editor.state.ShortcutFormState
import com.jorgelobo.koobe.ui.screen.shortcuts.editor.state.ShortcutUiStateInternal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import javax.inject.Inject

@HiltViewModel
class ShortcutEditorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    shortcutRepository: ShortcutRepository,
    categoryRepository: CategoryRepository
) : ViewModel() {

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
        when (config) {
            is ShortcutEditorConfig.Create -> flowOf(null)
            is ShortcutEditorConfig.Edit -> shortcutRepository.getShortcutByIdFlow(config.shortcutId)
        }

    private val categoryIdFlow: Flow<Int?> =
        shortcutFlow.map { shortcut ->
            when (config) {
                is ShortcutEditorConfig.Create -> config.categoryId
                is ShortcutEditorConfig.Edit -> shortcut?.categoryId
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val categoryFlow: Flow<Category?> =
        categoryIdFlow.flatMapLatest { categoryId ->
            if (categoryId != null) {
                categoryRepository.getCategoryByIdFlow(categoryId)
            } else {
                flowOf(null)
            }
        }


    // Base State

    private val baseStateFlow: Flow<ShortcutEditorUiState> =
        combine(
            shortcutFlow,
            categoryFlow
        ) { shortcut, category ->

            if (category == null) {
                return@combine ShortcutEditorUiState.initialEmpty()
            }

            when {
                config is ShortcutEditorConfig.Edit && shortcut != null -> {
                    ShortcutEditorUiState.initialFromShortcut(
                        config = config,
                        shortcut = shortcut,
                        category = category,
                    )
                }

                else -> {
                    ShortcutEditorUiState.initial(
                        config = config,
                        category = category
                    )
                }
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

    fun onIntent(intent: ShortcutEditorIntent) {
        when (intent) {
            is ShortcutEditorIntent.Action -> handleAction(intent)
            is ShortcutEditorIntent.State -> reduceState(intent)
        }
    }

    private fun reduceState(intent: ShortcutEditorIntent.State) {
        val result = ShortcutEditorReducer.reduce(
            intent = intent,
            currentForm = formState.value,
            currentInternal = uiInternalState.value,
            baseState = uiState.value
        )

        formState.value = result.form
        uiInternalState.value = result.internal
    }

    private fun handleAction(intent: ShortcutEditorIntent.Action) {
        when (intent) {
            is ShortcutEditorIntent.Action.DiscardDialogUpdated ->
                handleDiscardDialog(intent.action)

            is ShortcutEditorIntent.Action.DeleteDialogUpdated ->
                handleDeleteDialog(intent.action)

            is ShortcutEditorIntent.Action.IconSelectDialogUpdated ->
                handleIconSelectDialog(intent.action)

            is ShortcutEditorIntent.Action.CurrencyDialogUpdated ->
                handleCurrencyDialog(intent.action)

            is ShortcutEditorIntent.Action.PeriodSelectorUpdated ->
                handlePeriodSelectorSheet(intent.action)

            is ShortcutEditorIntent.Action.PaymentMethodSelectorUpdated ->
                handlePaymentMethodSelectorSheet(intent.action)

            ShortcutEditorIntent.Action.SaveClicked -> handleSave()
            ShortcutEditorIntent.Action.CloseClicked -> handleClose()
            ShortcutEditorIntent.Action.ChangeCategoryClicked -> handleChangeCategory()
            ShortcutEditorIntent.Action.RequestDeleteShortcut ->
                handleDeleteDialog(ConfirmationDialogAction.Open)
        }
    }

    private fun handleSave() {}
    private fun handleClose() {}
    private fun handleChangeCategory() {}
    private fun handleDiscardDialog(action: ConfirmationDialogAction) {}
    private fun handleDeleteDialog(action: ConfirmationDialogAction) {}
    private fun handleIconSelectDialog(action: SelectorDialogAction<IconPack>) {}
    private fun handleCurrencyDialog(action: SelectorDialogAction<CurrencyType>) {}
    private fun handlePaymentMethodSelectorSheet(action: SelectorSheetAction<PaymentMethodType>) {}
    private fun handlePeriodSelectorSheet(action: SelectorSheetAction<PeriodType>) {}
}