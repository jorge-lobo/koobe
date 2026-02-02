package com.jorgelobo.koobe.ui.screen.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.amount.reduceAmountInput
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.transaction.DescriptionResolution
import com.jorgelobo.koobe.domain.model.transaction.DescriptionSource
import com.jorgelobo.koobe.domain.repository.CategoryRepository
import com.jorgelobo.koobe.domain.repository.ShortcutRepository
import com.jorgelobo.koobe.domain.repository.SubcategoryRepository
import com.jorgelobo.koobe.domain.usecase.transaction.ResolveTransactionDescriptionUseCase
import com.jorgelobo.koobe.domain.usecase.transaction.SaveTransactionUseCase
import com.jorgelobo.koobe.ui.components.base.numericKeypad.KeypadKey
import com.jorgelobo.koobe.ui.components.model.icons.IconGeneral
import com.jorgelobo.koobe.ui.mappers.toAmountAction
import com.jorgelobo.koobe.ui.mappers.toTransaction
import com.jorgelobo.koobe.ui.screen.common.bottomSheet.selector.SelectorSheetAction
import com.jorgelobo.koobe.ui.screen.common.bottomSheet.selector.reduceSelectorSheet
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogAction
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogEffect
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.reduceConfirmationDialog
import com.jorgelobo.koobe.ui.screen.common.dialog.datePicker.DatePickerDialogAction
import com.jorgelobo.koobe.ui.screen.common.dialog.datePicker.DatePickerDialogEffect
import com.jorgelobo.koobe.ui.screen.common.dialog.datePicker.reduceDatePickerDialog
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogAction
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogEffect
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.reduceSelectorDialog
import com.jorgelobo.koobe.utils.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionEditorViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val subcategoryRepository: SubcategoryRepository,
    private val shortcutRepository: ShortcutRepository,
    private val saveTransactionUseCase: SaveTransactionUseCase,
    private val resolveTransactionDescriptionUseCase: ResolveTransactionDescriptionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransactionEditorUiState.initialEmpty())
    val uiState: StateFlow<TransactionEditorUiState> = _uiState

    private val _events = MutableSharedFlow<TransactionEditorEvent>()
    val events = _events.asSharedFlow()

    private lateinit var config: TransactionEditorConfig

    fun init(config: TransactionEditorConfig) {
        if (this::config.isInitialized) return
        this.config = config

        viewModelScope.launch {
            val category = categoryRepository.getCategoryById(config.categoryId) ?: Category.empty()
            val subcategory =
                config.subcategoryId?.let { subcategoryRepository.getSubcategoryById(it) }
            val shortcut = config.shortcutId?.let { shortcutRepository.getShortcutById(it) }

            _uiState.value = TransactionEditorUiState.initial(
                config = config,
                category = category,
                subcategory = subcategory,
                shortcut = shortcut
            )
        }
    }

    // ─────────────────────────────
    // User actions – Date
    // ─────────────────────────────

    fun onTodayClick() {
        updateState { state ->
            state.copy(date = DateUtils.currentDate)
        }
    }

    fun onDatePickerDialogAction(action: DatePickerDialogAction) {
        val (dialogState, effect) = reduceDatePickerDialog(
            state = uiState.value.datePickerDialog,
            action = action
        )

        updateState { state ->
            state.copy(datePickerDialog = dialogState)
        }

        when (effect) {
            is DatePickerDialogEffect.Confirmed ->
                updateState {
                    it.copy(date = effect.date)
                }

            null -> Unit
        }
    }

    // ─────────────────────────────
    // User actions – Description
    // ─────────────────────────────

    fun onDescriptionChanged(text: String) {
        updateState { state ->
            state.copy(descriptionSource = DescriptionSource.TextDescription(text))
        }
    }

    fun onResetDescription() {
        updateState { state ->
            state.copy(descriptionSource = DescriptionSource.Empty)
        }
    }

    fun onSnackBarActionClick(resolvedDescription: String) {
        autoFillDescription(resolvedDescription)
    }

    // ─────────────────────────────
    // User actions – Amount
    // ─────────────────────────────

    fun onAmountKeyPressed(key: KeypadKey) {
        val action = key.toAmountAction()

        updateState { state ->
            val newInput = reduceAmountInput(
                current = state.amountInput,
                action = action
            )

            state.copy(
                amountInput = newInput,
                amount = newInput.toDoubleOrNull() ?: 0.0
            )
        }
    }

    fun onResetAmount() {
        updateState { state ->
            state.copy(
                amountInput = "0",
                amount = 0.0
            )
        }
    }

    // ─────────────────────────────
    // User actions – Save
    // ─────────────────────────────

    fun onSaveClick() {
        val state = _uiState.value

        when (val result = resolveTransactionDescriptionUseCase.resolve(
            state.descriptionSource,
            state.subcategory,
            state.shortcut
        )) {
            is DescriptionResolution.Resolved -> saveTransaction(result.text)

            is DescriptionResolution.RequireUserChoice -> sendSnackBar()

            DescriptionResolution.Missing -> Unit
        }
    }

    // ─────────────────────────────
    // User actions – Dialogs / Sheets
    // ─────────────────────────────

    fun onDialogAction(action: ConfirmationDialogAction) {
        val (dialogState, effect) = reduceConfirmationDialog(
            state = uiState.value.discardDialog,
            action = action
        )

        updateState { state ->
            state.copy(discardDialog = dialogState)
        }

        when (effect) {
            ConfirmationDialogEffect.Confirmed -> sendNavigateBack()
            null -> Unit
        }
    }

    fun onCurrencySelectorDialogAction(action: SelectorDialogAction<CurrencyType>) {
        val (dialogState, effect) = reduceSelectorDialog(
            state = uiState.value.currencyDialog,
            action = action
        )

        updateState { state ->
            state.copy(currencyDialog = dialogState)
        }

        when (effect) {
            is SelectorDialogEffect.Applied ->
                updateState {
                    it.copy(currencyType = effect.value)
                }

            null -> Unit
        }
    }

    fun onPaymentSelectorAction(
        action: SelectorSheetAction<PaymentMethodType>
    ) {
        val newState = reduceSelectorSheet(
            state = uiState.value.paymentMethodSelector,
            action = action
        )

        updateState { state ->
            state.copy(
                paymentMethodSelector = newState,
                paymentMethodType = newState.selected
            )
        }
    }

    // ─────────────────────────────
    // Internal business logic
    // ─────────────────────────────

    private fun autoFillDescription(text: String) {
        updateState { state ->
            state.copy(descriptionSource = DescriptionSource.TextDescription(text))
        }

        saveTransaction(text)
    }

    private fun saveTransaction(description: String) {
        val state = _uiState.value

        viewModelScope.launch {
            saveTransactionUseCase(
                transaction = state.toTransaction(
                    config = config,
                    resolvedDescription = description
                ),
                isEditorMode = config.isEditMode
            )
            sendNavigateBack()
        }
    }

    // ─────────────────────────────
    // Side effects / Events
    // ─────────────────────────────

    private fun sendSnackBar() {
        emitEvent(
            TransactionEditorEvent.ShowSnackBar(
                messageRes = R.string.snackBar_message,
                actionLabelRes = R.string.snackBar_action,
                icon = IconGeneral.EDIT
            )
        )
    }

    private fun sendNavigateBack() {
        emitEvent(TransactionEditorEvent.ExitToOrigin)
    }

    private fun emitEvent(event: TransactionEditorEvent) {
        viewModelScope.launch { _events.emit(event) }
    }

    // ─────────────────────────────
    // State reducer
    // ─────────────────────────────

    private fun updateState(reducer: (TransactionEditorUiState) -> TransactionEditorUiState) {
        _uiState.update { state ->
            val newState = reducer(state)

            val enabled = if (config.isEditMode) {
                newState.hasUnsavedChanges
            } else {
                newState.amount > 0.0
            }

            newState.copy(isSaveButtonEnabled = enabled)
        }
    }
}