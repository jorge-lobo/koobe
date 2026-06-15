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
import com.jorgelobo.koobe.domain.usecase.shortcut.SaveShortcutUseCase
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.navigation.Route
import com.jorgelobo.koobe.ui.screen.categories.selector.CategorySelectorConfig
import com.jorgelobo.koobe.ui.screen.categories.selector.CategorySelectorMode
import com.jorgelobo.koobe.ui.screen.categories.selector.CategorySelectorTarget
import com.jorgelobo.koobe.ui.screen.common.bottomSheet.selector.SelectorSheetAction
import com.jorgelobo.koobe.ui.screen.common.bottomSheet.selector.handleSelectorSheet
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogAction
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.handleConfirmationDialog
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogAction
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.handleSelectorDialog
import com.jorgelobo.koobe.ui.screen.shortcuts.editor.state.ShortcutFormState
import com.jorgelobo.koobe.ui.screen.shortcuts.editor.state.ShortcutUiStateInternal
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.usecase.shortcut.DeleteShortcutUseCase
import com.jorgelobo.koobe.domain.validation.NameValidationException
import com.jorgelobo.koobe.ui.mappers.toShortcut
import com.jorgelobo.koobe.ui.mappers.toSnackBarMessageRes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import javax.inject.Inject

@HiltViewModel
class ShortcutEditorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    shortcutRepository: ShortcutRepository,
    categoryRepository: CategoryRepository,
    private val saveShortcut: SaveShortcutUseCase,
    private val deleteShortcut: DeleteShortcutUseCase
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

    @OptIn(ExperimentalCoroutinesApi::class)
    private val categoryFlow: Flow<Category?> =
        combine(
            shortcutFlow,
            formState
        ) { shortcut, form ->

            val baseCategoryId = when (config) {
                is ShortcutEditorConfig.Create -> config.categoryId
                is ShortcutEditorConfig.Edit -> shortcut?.categoryId
            }

            baseCategoryId?.let {
                form.categoryId.resolve(it)
            }
        }
            .filterNotNull()
            .flatMapLatest { categoryId ->
                categoryRepository.getCategoryByIdFlow(categoryId)
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
                iconSelectorDialog = uiInternal.iconSelectDialog,
                currencySelectorDialog = uiInternal.currencyDialog,
                paymentMethodSelectorSheet = uiInternal.paymentMethodSelector,
                periodSelectorSheet = uiInternal.periodSelector,
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
                handleIconSelectorDialog(intent.action)

            is ShortcutEditorIntent.Action.CurrencyDialogUpdated ->
                handleCurrencySelectorDialog(intent.action)

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

    private fun handleSave() {
        val state = uiState.value

        if (!state.isValid) {
            showSnackBar(R.string.snackBar_save_shortcut_error)
            return
        }

        viewModelScope.launch {
            uiInternalState.update { it.copy(isSaving = true) }

            runCatching {
                saveShortcut(
                    shortcut = state.toShortcut(config),
                    config = config
                )
            }.onSuccess {
                uiInternalState.update { it.copy(isSaving = false) }
                navigateBack()
            }.onFailure { error ->

                val validationError = error as? NameValidationException
                onIntent(ShortcutEditorIntent.State.NameChanged(""))

                uiInternalState.update {
                    it.copy(
                        isSaving = false,
                        nameError = validationError,
                        hasTriedToSave = true
                    )
                }

                val messageRes = validationError?.toSnackBarMessageRes()
                    ?: R.string.snackBar_save_shortcut_error

                showSnackBar(messageRes)
            }
        }
    }

    private fun handleClose() {
        if (formState.value.hasChanges) {
            handleDiscardDialog(ConfirmationDialogAction.Open)
        } else {
            navigateBack()
        }
    }

    private fun handleChangeCategory() {
        val state = uiState.value
        val mode = when (config) {
            is ShortcutEditorConfig.Create -> CategorySelectorMode.CREATE_SHORTCUT
            is ShortcutEditorConfig.Edit -> CategorySelectorMode.EDIT_SHORTCUT
        }

        val route = Route.CategorySelector.create(
            CategorySelectorConfig(
                mode = mode,
                target = CategorySelectorTarget.SHORTCUT_EDITOR,
                initialTransactionType = state.category.type
            )
        )
        navigateTo(route)
    }

    private fun handleDiscardDialog(action: ConfirmationDialogAction) {
        handleConfirmationDialog(
            current = uiInternalState.value.discardDialog,
            action = action,
            updateState = { newDialogState ->
                uiInternalState.update { currentState ->
                    currentState.copy(discardDialog = newDialogState)
                }
            },
            onConfirmed = { navigateBack() }
        )
    }

    private fun handleDeleteDialog(action: ConfirmationDialogAction) {
        handleConfirmationDialog(
            current = uiInternalState.value.deleteDialog,
            action = action,
            updateState = { newDialogState ->
                uiInternalState.update { currentState ->
                    currentState.copy(deleteDialog = newDialogState)
                }
            },
            onConfirmed = { deleteCurrentShortcut() }
        )
    }

    private fun handleIconSelectorDialog(action: SelectorDialogAction<IconPack>) {
        handleSelectorDialog(
            current = uiInternalState.value.iconSelectDialog,
            action = action,
            updateState = { newDialogState ->
                uiInternalState.update { currentState ->
                    currentState.copy(iconSelectDialog = newDialogState)
                }
            },
            onApplied = { onIntent(ShortcutEditorIntent.State.IconChanged(it)) }
        )
    }

    private fun handleCurrencySelectorDialog(action: SelectorDialogAction<CurrencyType>) {
        handleSelectorDialog(
            current = uiInternalState.value.currencyDialog,
            action = action,
            updateState = { newDialogState ->
                uiInternalState.update { currentState ->
                    currentState.copy(currencyDialog = newDialogState)
                }
            },
            onApplied = { onIntent(ShortcutEditorIntent.State.CurrencyChanged(it)) }
        )
    }

    private fun handlePaymentMethodSelectorSheet(action: SelectorSheetAction<PaymentMethodType>) {
        handleSelectorSheet(
            current = uiInternalState.value.paymentMethodSelector,
            action = action,
            updateState = { newState ->
                uiInternalState.update { currentState ->
                    currentState.copy(paymentMethodSelector = newState)
                }
            },
            onApplied = {
                onIntent(ShortcutEditorIntent.State.PaymentMethodChanged(it))
            }
        )
    }

    private fun handlePeriodSelectorSheet(action: SelectorSheetAction<PeriodType>) {
        handleSelectorSheet(
            current = uiInternalState.value.periodSelector,
            action = action,
            updateState = { newState ->
                uiInternalState.update { currentState ->
                    currentState.copy(periodSelector = newState)
                }
            },
            onApplied = {
                onIntent(ShortcutEditorIntent.State.RepeatPeriodChanged(it))
            }
        )
    }

    private fun deleteCurrentShortcut() {
        val shortcut = uiState.value.toShortcut(config)

        viewModelScope.launch {
            uiInternalState.update { it.copy(isDeleting = true) }

            runCatching {
                deleteShortcut(shortcut)
            }.onSuccess {
                uiInternalState.update { it.copy(isDeleting = false) }
                navigateBack()
            }.onFailure {
                uiInternalState.update { it.copy(isDeleting = false) }
                showSnackBar(messageRes = R.string.snackBar_delete_shortcut_error)
            }
        }
    }

    private fun showSnackBar(messageRes: Int) {
        emitEvent(ShortcutEditorEvent.ShowSnackbar(messageRes, null, IconPack.WARNING))
    }

    private fun navigateBack() {
        emitEvent(ShortcutEditorEvent.NavigateBack)
    }

    private fun navigateTo(route: String) {
        emitEvent(ShortcutEditorEvent.NavigateTo(route))
    }

    private fun emitEvent(event: ShortcutEditorEvent) {
        viewModelScope.launch { _events.emit(event) }
    }
}