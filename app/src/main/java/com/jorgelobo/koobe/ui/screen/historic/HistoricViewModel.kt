package com.jorgelobo.koobe.ui.screen.historic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorgelobo.koobe.domain.model.category.CategoryHistory
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.model.transaction.Transaction
import com.jorgelobo.koobe.domain.settings.GetUserSettingsUseCase
import com.jorgelobo.koobe.domain.usecase.historic.GetHistoricDataUseCase
import com.jorgelobo.koobe.domain.usecase.historic.GetPeriodTotalsUseCase
import com.jorgelobo.koobe.ui.navigation.Route
import com.jorgelobo.koobe.ui.screen.common.bottomSheet.periodFilter.PeriodFilterAction
import com.jorgelobo.koobe.ui.screen.common.bottomSheet.periodFilter.reducePeriodFilter
import com.jorgelobo.koobe.ui.screen.common.dialog.datePicker.DatePickerDialogAction
import com.jorgelobo.koobe.ui.screen.common.dialog.datePicker.DatePickerDialogEffect
import com.jorgelobo.koobe.ui.screen.common.dialog.datePicker.reduceDatePickerDialog
import com.jorgelobo.koobe.ui.screen.transactions.TransactionEditorConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoricViewModel @Inject constructor(
    private val getHistoricData: GetHistoricDataUseCase,
    private val getPeriodTotals: GetPeriodTotalsUseCase,
    private val getUserSettings: GetUserSettingsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoricUiState())
    val uiState: StateFlow<HistoricUiState> = _uiState

    private val _events = MutableSharedFlow<HistoricEvent>()
    val events = _events.asSharedFlow()

    private val filters =
        _uiState.map {
            Triple(
                it.date,
                it.periodType,
                it.transactionTypeSelected
            )
        }

    init {
        loadUserSettings()
        observeHistoric()
    }

    fun onTransactionTypeChanged(type: TransactionType) {
        if (_uiState.value.transactionTypeSelected == type) return

        _uiState.update { it.copy(transactionTypeSelected = type) }
    }

    fun onBackClick() {
        navigateBack()
    }

    fun onTransactionItemClick(item: Transaction) {
        val route = Route.TransactionEditor.create(
            TransactionEditorConfig(
                transactionId = item.id,
                transactionType = item.type,
                categoryId = item.categoryId,
                subcategoryId = item.subcategoryId,
                originRoute = "historic"
            )
        )
        navigateTo(route)
    }

    fun onCategoryExpandToggle(categoryId: Int) {
        _uiState.update { state ->
            val updated = state.categories.map { categoryUi ->

                if (categoryUi.category.id == categoryId) {
                    val newExpanded = !categoryUi.isExpanded

                    categoryUi.copy(
                        isExpanded = newExpanded,
                        expandedSubcategories = if (newExpanded) {
                            categoryUi.expandedSubcategories
                        } else {
                            emptySet()
                        }
                    )
                } else {
                    categoryUi
                }
            }

            state.copy(categories = updated)
        }
    }

    fun onSubcategoryExpandToggle(
        categoryId: Int,
        subcategoryId: Int
    ) {
        _uiState.update { state ->
            val updated = state.categories.map { categoryUi ->

                if (categoryUi.category.id != categoryId) return@map categoryUi

                val newExpandedSet =
                    if (subcategoryId in categoryUi.expandedSubcategories) {
                        categoryUi.expandedSubcategories - subcategoryId
                    } else {
                        categoryUi.expandedSubcategories + subcategoryId
                    }

                categoryUi.copy(expandedSubcategories = newExpandedSet)
            }

            state.copy(categories = updated)
        }
    }

    fun onPeriodFilterAction(action: PeriodFilterAction) {

        if (action is PeriodFilterAction.OpenDatePicker) {
            _uiState.update {
                it.copy(
                    datePickerDialog = it.datePickerDialog.copy(
                        visible = true,
                        selectedDate = it.periodFilter.tempSelectedDate
                    )
                )
            }

            return
        }

        val currentState = _uiState.value
        val newFilterState = reducePeriodFilter(currentState.periodFilter, action)

        var newState = currentState.copy(periodFilter = newFilterState)

        if (action is PeriodFilterAction.Apply) {
            newState = newState.copy(
                date = newFilterState.selectedDate,
                periodType = newFilterState.selectedType
            )
        }

        _uiState.value = newState
    }

    fun onDatePickerDialogAction(action: DatePickerDialogAction) {
        val (dialogState, effect) = reduceDatePickerDialog(
            state = uiState.value.datePickerDialog,
            action = action
        )

        _uiState.update { it.copy(datePickerDialog = dialogState) }

        when (effect) {
            is DatePickerDialogEffect.Confirmed -> {
                _uiState.update {
                    it.copy(
                        periodFilter = it.periodFilter.copy(
                            tempSelectedDate = effect.date
                        )
                    )
                }
            }

            null -> Unit
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeHistoric() {
        viewModelScope.launch {
            filters
                .distinctUntilChanged()
                .onStart {
                    _uiState.update { it.copy(isLoading = true) }
                }
                .flatMapLatest { (date, periodType, type) ->

                    combine(
                        getHistoricData(date, type, periodType),
                        getPeriodTotals(date, periodType)
                    ) { histories, totals ->

                        histories to totals
                    }
                }
                .collect { (histories, totals) ->

                    _uiState.update { state ->
                        state.copy(
                            categories = histories.map { it.toUi() },
                            income = totals.income,
                            expenses = totals.expenses,
                            balance = totals.balance,
                            isLoading = false
                        )
                    }
                }
        }
    }

    private fun loadUserSettings() {
        viewModelScope.launch {
            getUserSettings().collect { settings ->
                _uiState.update { state ->
                    state.copy(
                        currencyType = settings.currency,
                        startOfWeek = settings.startOfWeek
                    )
                }
            }
        }
    }

    private fun navigateBack() {
        emitEvent(HistoricEvent.NavigateBack)
    }

    private fun navigateTo(route: String) {
        emitEvent(HistoricEvent.NavigateTo(route))
    }

    private fun emitEvent(event: HistoricEvent) {
        viewModelScope.launch { _events.emit(event) }
    }

    private fun CategoryHistory.toUi() =
        CategoryHistoricUi(
            category = category,
            history = this
        )
}