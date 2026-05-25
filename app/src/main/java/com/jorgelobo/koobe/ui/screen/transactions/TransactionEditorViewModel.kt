package com.jorgelobo.koobe.ui.screen.transactions

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.core.model.resolve
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.transaction.DescriptionResolution
import com.jorgelobo.koobe.domain.model.transaction.Transaction
import com.jorgelobo.koobe.domain.repository.CategoryRepository
import com.jorgelobo.koobe.domain.repository.ShortcutRepository
import com.jorgelobo.koobe.domain.repository.SubcategoryRepository
import com.jorgelobo.koobe.domain.repository.TransactionRepository
import com.jorgelobo.koobe.domain.settings.GetUserSettingsUseCase
import com.jorgelobo.koobe.domain.usecase.transaction.DeleteTransactionUseCase
import com.jorgelobo.koobe.domain.usecase.transaction.ResolveTransactionDescriptionUseCase
import com.jorgelobo.koobe.domain.usecase.transaction.SaveTransactionUseCase
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.mappers.toTransaction
import com.jorgelobo.koobe.ui.navigation.Route
import com.jorgelobo.koobe.ui.screen.categories.selector.CategorySelectorConfig
import com.jorgelobo.koobe.ui.screen.categories.selector.CategorySelectorMode
import com.jorgelobo.koobe.ui.screen.categories.selector.CategorySelectorTarget
import com.jorgelobo.koobe.ui.screen.common.bottomSheet.selector.SelectorSheetAction
import com.jorgelobo.koobe.ui.screen.common.bottomSheet.selector.handleSelectorSheet
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogAction
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.handleConfirmationDialog
import com.jorgelobo.koobe.ui.screen.common.dialog.datePicker.DatePickerDialogAction
import com.jorgelobo.koobe.ui.screen.common.dialog.datePicker.DatePickerDialogEffect
import com.jorgelobo.koobe.ui.screen.common.dialog.datePicker.reduceDatePickerDialog
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogAction
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.handleSelectorDialog
import com.jorgelobo.koobe.ui.screen.transactions.state.TransactionFormState
import com.jorgelobo.koobe.ui.screen.transactions.state.TransactionUiStateInternal
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import javax.inject.Inject

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

    private val config: TransactionEditorConfig =
        savedStateHandle.get<String>("config")
            ?.let { URLDecoder.decode(it, "UTF-8") }
            ?.let { Json.decodeFromString<TransactionEditorConfig>(it) }
            ?: error("Missing TransactionEditorConfig")

    private val formState = MutableStateFlow(TransactionFormState())
    private val uiInternalState = MutableStateFlow(TransactionUiStateInternal())

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

    val uiState: StateFlow<TransactionEditorUiState> =
        combine(
            baseStateFlow,
            formState,
            uiInternalState
        ) { base, form, uiInternal ->

            val updatedDescription = form.description.resolve(base.descriptionSource)
            val updatedAmountInput = form.amountInput.resolve(base.amountInput)
            val updatedDate = form.date.resolve(base.date)
            val updatedPaymentMethod = form.paymentMethod.resolve(base.paymentMethodType)
            val updatedCurrency = form.currency.resolve(base.currencyType)
            val updatedAmount = updatedAmountInput.toDoubleOrNull() ?: base.amount

            base.copy(
                descriptionSource = updatedDescription,
                amountInput = updatedAmountInput,
                amount = updatedAmount,
                date = updatedDate,
                paymentMethodType = updatedPaymentMethod,
                currencyType = updatedCurrency,
                discardDialog = uiInternal.discardDialog,
                deleteDialog = uiInternal.deleteDialog,
                currencyDialog = uiInternal.currencyDialog,
                paymentMethodSelector = uiInternal.paymentMethodSelector,
                datePickerDialog = uiInternal.datePickerDialog,
                isLoading = uiInternal.isDeleting
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = TransactionEditorUiState.initialEmpty()
            )

    fun onIntent(intent: TransactionEditorIntent) {
        when (intent) {

            is TransactionEditorIntent.Action -> handleAction(intent)

            is TransactionEditorIntent.State -> {

                val result = TransactionEditorReducer.reduce(
                    intent = intent,
                    currentForm = formState.value,
                    currentInternal = uiInternalState.value,
                    baseState = uiState.value
                )

                formState.value = result.form
                uiInternalState.value = result.internal
            }
        }
    }

    private fun handleAction(intent: TransactionEditorIntent.Action) {
        when (intent) {
            is TransactionEditorIntent.Action.DiscardDialogUpdated -> handleDiscardDialog(intent.action)

            is TransactionEditorIntent.Action.DeleteDialogUpdated -> handleDeleteDialog(intent.action)

            is TransactionEditorIntent.Action.DatePickerDialogUpdated ->
                handleDatePickerDialog(intent.action)

            is TransactionEditorIntent.Action.CurrencySelectorDialogUpdated ->
                handleCurrencySelectorDialog(intent.action)

            is TransactionEditorIntent.Action.PaymentMethodSelectorUpdated ->
                handlePaymentMethodSelectorSheet(intent.action)

            TransactionEditorIntent.Action.SaveClicked -> handleSave()
            TransactionEditorIntent.Action.CloseClicked -> handleClose()
            TransactionEditorIntent.Action.ChangeCategoryClicked -> handleChangeCategory()
            TransactionEditorIntent.Action.RequestDeleteTransaction ->
                handleDeleteDialog(ConfirmationDialogAction.Open)
        }
    }

    private fun handleSave() {
        val state = uiState.value

        when (val result = resolveTransactionDescription.resolve(
            state.descriptionSource,
            state.subcategory,
            state.shortcut
        )) {
            is DescriptionResolution.Resolved -> saveTransaction(result.text)

            is DescriptionResolution.RequireUserChoice ->
                showSnackBar(
                    messageRes = R.string.snackBar_empty_description,
                    actionLabelRes = R.string.snackBar_action,
                    icon = IconPack.EDIT
                )

            DescriptionResolution.Missing -> Unit
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
        val mode = if (config.isEditMode) {
            CategorySelectorMode.EDIT_TRANSACTION
        } else {
            CategorySelectorMode.CREATE_TRANSACTION
        }

        val route = Route.CategorySelector.create(
            CategorySelectorConfig(
                mode = mode,
                target = CategorySelectorTarget.TRANSACTION_EDITOR,
                initialTransactionType = state.category.type,
                initialCategoryId = state.category.id,
                initialSubcategoryId = state.subcategory?.id
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
            onConfirmed = { deleteCurrentTransaction() }
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
            onApplied = { onIntent(TransactionEditorIntent.State.CurrencyChanged(it)) }
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
                onIntent(TransactionEditorIntent.State.PaymentMethodChanged(it))
            }
        )
    }

    private fun handleDatePickerDialog(action: DatePickerDialogAction) {
        val (newState, effect) = reduceDatePickerDialog(
            state = uiInternalState.value.datePickerDialog,
            action = action
        )

        uiInternalState.update {
            it.copy(datePickerDialog = newState)
        }

        when (effect) {
            is DatePickerDialogEffect.Confirmed -> {
                onIntent(TransactionEditorIntent.State.DateChanged(effect.date))
            }

            null -> Unit
        }
    }

    private fun deleteCurrentTransaction() {
        val transaction = uiState.value.originalTransaction ?: return

        viewModelScope.launch {
            uiInternalState.update { it.copy(isDeleting = true) }

            runCatching {
                deleteTransaction(transaction)
            }.onSuccess {
                uiInternalState.update { it.copy(isDeleting = false) }
                navigateBack()
            }.onFailure {

                uiInternalState.update { it.copy(isDeleting = false) }
                showSnackBar(
                    messageRes = R.string.snackBar_delete_transaction_error,
                    actionLabelRes = null,
                    icon = IconPack.WARNING
                )
            }
        }
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

    private fun showSnackBar(messageRes: Int, actionLabelRes: Int? = null, icon: IconPack) {
        emitEvent(
            TransactionEditorEvent.ShowSnackBar(
                messageRes,
                actionLabelRes,
                icon
            )
        )
    }

    private fun navigateBack() {
        emitEvent(TransactionEditorEvent.ExitToOrigin)
    }

    private fun navigateTo(route: String) {
        emitEvent(TransactionEditorEvent.NavigateTo(route))
    }

    private fun emitEvent(event: TransactionEditorEvent) {
        viewModelScope.launch { _events.emit(event) }
    }
}