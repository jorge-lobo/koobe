package com.jorgelobo.koobe.ui.screen.transactions

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.amount.reduceAmountInput
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.transaction.DescriptionResolution
import com.jorgelobo.koobe.domain.model.transaction.DescriptionSource
import com.jorgelobo.koobe.domain.model.transaction.Transaction
import com.jorgelobo.koobe.domain.repository.CategoryRepository
import com.jorgelobo.koobe.domain.repository.ShortcutRepository
import com.jorgelobo.koobe.domain.repository.SubcategoryRepository
import com.jorgelobo.koobe.domain.repository.TransactionRepository
import com.jorgelobo.koobe.domain.settings.GetUserSettingsUseCase
import com.jorgelobo.koobe.domain.usecase.transaction.DeleteTransactionUseCase
import com.jorgelobo.koobe.domain.usecase.transaction.ResolveTransactionDescriptionUseCase
import com.jorgelobo.koobe.domain.usecase.transaction.SaveTransactionUseCase
import com.jorgelobo.koobe.ui.components.base.numericKeypad.KeypadKey
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
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
import com.jorgelobo.koobe.utils.date.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

/**
 * ViewModel responsible for managing the state and business logic of the Transaction Editor screen.
 *
 * This ViewModel handles:
 * - Loading initial data for categories, subcategories, shortcuts, and existing transactions.
 * - Managing complex UI state including date selection, amount input via a custom keypad,
 *   currency selection, and payment methods.
 * - Resolving transaction descriptions automatically based on selected subcategories or shortcuts.
 * - Validating form requirements and determining unsaved changes to enable/disable the save action.
 * - Persisting new or edited transactions via [SaveTransactionUseCase].
 * - Deleting existing transactions via [DeleteTransactionUseCase].
 * - Emitting one-off UI events such as navigation and SnackBar notifications via [events].
 *
 * @property events A [SharedFlow] of [TransactionEditorEvent] for handling UI side-effects.
 * @property uiState A [StateFlow] of [TransactionEditorUiState] representing the current screen state.
 */
@HiltViewModel
class TransactionEditorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    categoryRepository: CategoryRepository,
    subcategoryRepository: SubcategoryRepository,
    shortcutRepository: ShortcutRepository,
    transactionRepository: TransactionRepository,
    getUserSettings: GetUserSettingsUseCase,
    private val saveTransaction: SaveTransactionUseCase,
    private val resolveTransactionDescription: ResolveTransactionDescriptionUseCase,
    private val deleteTransaction: DeleteTransactionUseCase
) : ViewModel() {

    private val _events = MutableSharedFlow<TransactionEditorEvent>()
    val events = _events.asSharedFlow()

    /**
     * Configuration settings for the transaction editor, retrieved from the [SavedStateHandle].
     *
     * This object contains the initial parameters required to initialize the editor,
     * such as whether it's in edit mode, the transaction ID, and associated category,
     * subcategory, or shortcut identifiers.
     */
    private val config: TransactionEditorConfig =
        savedStateHandle.get<String>("config")
            ?.let { Uri.decode(it) }
            ?.let { Json.decodeFromString<TransactionEditorConfig>(it) }
            ?: error("Missing TransactionEditorConfig")

    private val transactionFlow: Flow<Transaction?> =
        if (config.isEditMode) {
            transactionRepository.getTransactionByIdFlow(config.transactionId!!)
        } else {
            flowOf(null)
        }

    private val categoryFlow = categoryRepository.getCategoryByIdFlow(config.categoryId)

    private val subcategoryFlow = config.subcategoryId
        ?.let { subcategoryRepository.getSubcategoryByIdFlow(it) }
        ?: flowOf(null)

    private val shortcutFlow = config.shortcutId
        ?.let { shortcutRepository.getShortcutByIdFlow(it) }
        ?: flowOf(null)

    private val userSettingsFlow = getUserSettings()

    private val userInput = MutableStateFlow(TransactionInputState())

    /**
     * A [Flow] that combines external data sources (category, subcategory, shortcut, existing
     * transaction, and user settings) to produce the baseline [TransactionEditorUiState].
     *
     * This flow determines the initial state of the editor, distinguishing between
     * creation mode and edit mode, and serves as the foundation upon which
     * [userInput] changes are applied.
     */
    private val baseStateFlow: Flow<TransactionEditorUiState> =
        combine(
            categoryFlow,
            subcategoryFlow,
            shortcutFlow,
            transactionFlow,
            userSettingsFlow
        ) { category, subcategory, shortcut, transaction, settings ->

            val safeCategory = category ?: Category.empty()

            if (config.isEditMode && transaction != null) {
                TransactionEditorUiState.initialFromTransaction(
                    config = config,
                    transaction = transaction,
                    category = safeCategory,
                    subcategory = subcategory,
                    shortcut = shortcut
                ).copy(
                    language = settings.language
                )
            } else {
                TransactionEditorUiState.initial(
                    config = config,
                    category = safeCategory,
                    subcategory = subcategory,
                    shortcut = shortcut
                ).copy(
                    language = settings.language,
                    currencyType = settings.currency,
                    paymentMethodType = settings.paymentMethod
                )
            }
        }

    /**
     * The combined and observable state of the Transaction Editor screen.
     *
     * This property merges [baseStateFlow] (containing data from repositories and settings)
     * with [userInput] (containing temporary changes made by the user). It calculates
     * the final UI state, including form validation and the enabled status of the save action.
     */
    val uiState: StateFlow<TransactionEditorUiState> =
        combine(
            baseStateFlow,
            userInput
        ) { base: TransactionEditorUiState, input: TransactionInputState ->

            val amountInput = input.amountInput ?: base.amountInput

            val newState = base.copy(
                descriptionSource = input.descriptionSource ?: base.descriptionSource,
                amountInput = amountInput,
                amount = amountInput.toDoubleOrNull() ?: base.amount,
                date = input.date ?: base.date,
                paymentMethodType = input.paymentMethodType ?: base.paymentMethodType,
                currencyType = input.currencyType ?: base.currencyType,
                discardDialog = input.discardDialog ?: base.discardDialog,
                deleteDialog = input.deleteDialog ?: base.deleteDialog,
                currencyDialog = input.currencyDialog ?: base.currencyDialog,
                paymentMethodSelector = input.paymentMethodSelector ?: base.paymentMethodSelector,
                datePickerDialog = input.datePickerDialog ?: base.datePickerDialog,
                isLoading = input.isDeleting ?: base.isLoading
            )

            newState.copy(
                isSaveButtonEnabled = computeSaveEnabled(newState)
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = TransactionEditorUiState.initialEmpty()
            )

    // ─────────────────────────────
    // Save button logic
    // ─────────────────────────────

    /**
     * Determines whether the save action should be enabled based on the current [state].
     *
     * In edit mode, saving is enabled only if there are unsaved changes.
     * In creation mode, saving is enabled as long as the amount is greater than zero.
     *
     * @param state The current UI state of the transaction editor.
     * @return True if the transaction can be saved, false otherwise.
     */
    private fun computeSaveEnabled(state: TransactionEditorUiState): Boolean {
        return if (config.isEditMode) {
            state.hasUnsavedChanges
        } else {
            state.amount > 0
        }
    }

    // ─────────────────────────────
    // User actions – Date
    // ─────────────────────────────

    fun onTodayClick() {
        userInput.update {
            it.copy(date = DateUtils.currentDate)
        }
    }

    fun onDatePickerDialogAction(action: DatePickerDialogAction) {
        val (dialogState, effect) = reduceDatePickerDialog(
            state = uiState.value.datePickerDialog,
            action = action
        )

        userInput.update {
            it.copy(datePickerDialog = dialogState)
        }

        when (effect) {
            is DatePickerDialogEffect.Confirmed ->
                userInput.update {
                    it.copy(date = effect.date)
                }

            null -> Unit
        }
    }

    // ─────────────────────────────
    // User actions – Description
    // ─────────────────────────────

    fun onDescriptionChanged(text: String) {
        userInput.update {
            it.copy(descriptionSource = DescriptionSource.TextDescription(text))
        }
    }

    fun onResetDescription() {
        userInput.update {
            it.copy(descriptionSource = DescriptionSource.Empty)
        }
    }

    fun onSnackBarActionClick(resolvedDescription: String) {
        autoFillDescription(resolvedDescription)
    }

    // ─────────────────────────────
    // User actions – Amount
    // ─────────────────────────────

    fun onAmountKeyPressed(key: KeypadKey) {
        userInput.update { input ->
            val current = input.amountInput ?: uiState.value.amountInput
            val newInput = reduceAmountInput(current, key.toAmountAction())

            input.copy(amountInput = newInput)
        }
    }

    fun onResetAmount() {
        userInput.update {
            it.copy(amountInput = "0")
        }
    }

    // ─────────────────────────────
    // User actions – Save
    // ─────────────────────────────

    /**
     * Handles the click event on the save button.
     *
     * It attempts to resolve the transaction description based on the current user input,
     * selected subcategory, or shortcut. Depending on the [DescriptionResolution] result:
     * - **Resolved**: Saves the transaction immediately using the resolved description.
     * - **RequireUserChoice**: Triggers a SnackBar to let the user choose or confirm a description.
     * - **Missing**: Does nothing (aborts the save process).
     */
    fun onSaveClick() {
        val state = uiState.value

        when (val result = resolveTransactionDescription.resolve(
            state.descriptionSource,
            state.subcategory,
            state.shortcut
        )) {
            is DescriptionResolution.Resolved -> saveTransaction(result.text)

            is DescriptionResolution.RequireUserChoice -> showSnackBar()

            DescriptionResolution.Missing -> Unit
        }
    }

    // ─────────────────────────────
    // User actions – Delete
    // ─────────────────────────────

    /**
     * Deletes the current transaction from the repository and manages the UI state during the process.
     *
     * This function checks for the existence of an original transaction, updates the [userInput]
     * to reflect a loading state, and invokes the [deleteTransaction] use case.
     * - On success: Triggers navigation back to the previous screen.
     * - On failure: Resets the loading state and emits a SnackBar event to notify the user of the error.
     */
    private fun deleteCurrentTransaction() {
        val transaction = uiState.value.originalTransaction ?: return

        viewModelScope.launch {
            runCatching {
                userInput.update { it.copy(isDeleting = true) }
                deleteTransaction(transaction)
            }.onSuccess {
                navigateBack()
            }.onFailure {
                userInput.update { it.copy(isDeleting = false) }
                emitEvent(
                    TransactionEditorEvent.ShowSnackBar(
                        messageRes = R.string.snackBar_delete_transaction_error,
                        actionLabelRes = null,
                        icon = IconPack.WARNING
                    )
                )
            }
        }
    }

    // ─────────────────────────────
    // User actions – Dialogs / Sheets
    // ─────────────────────────────

    fun onDiscardDialogAction(action: ConfirmationDialogAction) {
        val (dialogState, effect) = reduceConfirmationDialog(
            state = uiState.value.discardDialog,
            action = action
        )

        userInput.update {
            it.copy(discardDialog = dialogState)
        }

        when (effect) {
            ConfirmationDialogEffect.Confirmed -> navigateBack()

            null -> Unit
        }
    }

    fun onDeleteDialogAction(action: ConfirmationDialogAction) {
        val (dialogState, effect) = reduceConfirmationDialog(
            state = uiState.value.deleteDialog,
            action = action
        )

        userInput.update {
            it.copy(deleteDialog = dialogState)
        }

        when (effect) {
            ConfirmationDialogEffect.Confirmed -> deleteCurrentTransaction()

            null -> Unit
        }
    }

    fun onCurrencySelectorDialogAction(action: SelectorDialogAction<CurrencyType>) {
        val currentState = uiState.value

        val baseState =
            if (action is SelectorDialogAction.Open) {
                currentState.currencyDialog.copy(
                    initial = currentState.currencyType,
                    selected = currentState.currencyType
                )
            } else currentState.currencyDialog

        val (dialogState, effect) = reduceSelectorDialog(
            state = baseState,
            action = action
        )

        userInput.update {
            it.copy(currencyDialog = dialogState)
        }

        when (effect) {
            is SelectorDialogEffect.Applied ->
                userInput.update {
                    it.copy(currencyType = effect.value)
                }

            null -> Unit
        }
    }

    fun onPaymentSelectorAction(
        action: SelectorSheetAction<PaymentMethodType>
    ) {
        val currentState = uiState.value

        val baseState =
            if (action is SelectorSheetAction.Open) {
                currentState.paymentMethodSelector.copy(
                    selected = currentState.paymentMethodType,
                )
            } else currentState.paymentMethodSelector

        val newState = reduceSelectorSheet(
            state = baseState,
            action = action
        )

        userInput.update {
            it.copy(
                paymentMethodSelector = newState,
                paymentMethodType = newState.selected
            )
        }
    }

    // ─────────────────────────────
    // Internal business logic
    // ─────────────────────────────

    // Automatically fills the description and proceeds with saving the transaction
    private fun autoFillDescription(text: String) {
        userInput.update {
            it.copy(descriptionSource = DescriptionSource.TextDescription(text))
        }

        saveTransaction(text)
    }

    private fun saveTransaction(description: String) {
        val state = uiState.value

        viewModelScope.launch {
            saveTransaction(
                transaction = state.toTransaction(
                    config = config,
                    resolvedDescription = description
                ),
                isEditorMode = config.isEditMode
            )
            navigateBack()
        }
    }

    // ─────────────────────────────
    // Side effects / Events
    // ─────────────────────────────

    private fun showSnackBar() {
        emitEvent(
            TransactionEditorEvent.ShowSnackBar(
                messageRes = R.string.snackBar_message,
                actionLabelRes = R.string.snackBar_action,
                icon = IconPack.EDIT
            )
        )
    }

    private fun navigateBack() {
        emitEvent(TransactionEditorEvent.ExitToOrigin)
    }

    private fun emitEvent(event: TransactionEditorEvent) {
        viewModelScope.launch { _events.emit(event) }
    }
}