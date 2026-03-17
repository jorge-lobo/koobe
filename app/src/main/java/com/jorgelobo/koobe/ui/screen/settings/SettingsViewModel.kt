package com.jorgelobo.koobe.ui.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorgelobo.koobe.domain.model.constants.enums.AppLanguage
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek
import com.jorgelobo.koobe.domain.settings.GetUserSettingsUseCase
import com.jorgelobo.koobe.domain.settings.SetCurrencyUseCase
import com.jorgelobo.koobe.domain.settings.SetLanguageUseCase
import com.jorgelobo.koobe.domain.settings.SetPaymentMethodUseCase
import com.jorgelobo.koobe.domain.settings.SetStartOfWeekUseCase
import com.jorgelobo.koobe.ui.navigation.Route
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogAction
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogEffect
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogState
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.reduceSelectorDialog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for managing the user settings screen logic and state.
 *
 * This ViewModel interacts with use cases to retrieve and update user preferences such as
 * language, currency, the start day of the week, and the default payment method.
 * It manages the state for various selection dialogs and ensures that preference changes are
 * persisted through the corresponding use cases.
 *
 * @property getUserSettings Use case to retrieve the current user settings as a flow.
 * @property setLanguage Use case to update the application language preference.
 * @property setCurrency Use case to update the preferred currency.
 * @property setStartOfWeek Use case to update the preferred first day of the week.
 * @property setPaymentMethod Use case to update the preferred default payment method.
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getUserSettings: GetUserSettingsUseCase,
    private val setLanguage: SetLanguageUseCase,
    private val setCurrency: SetCurrencyUseCase,
    private val setStartOfWeek: SetStartOfWeekUseCase,
    private val setPaymentMethod: SetPaymentMethodUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState

    private val _events = MutableSharedFlow<SettingsEvent>()
    val events = _events.asSharedFlow()

    init {
        viewModelScope.launch {
            getUserSettings().collect { settings ->
                _uiState.update {
                    it.copy(
                        languageSelected = settings.language,
                        currencySelected = settings.currency,
                        startOfWeekSelected = settings.startOfWeek,
                        paymentMethodSelected = settings.paymentMethod,
                        languageDialog = it.languageDialog.copy(initial = settings.language),
                        currencyDialog = it.currencyDialog.copy(initial = settings.currency),
                        startOfWeekDialog = it.startOfWeekDialog.copy(initial = settings.startOfWeek),
                        paymentMethodDialog = it.paymentMethodDialog.copy(initial = settings.paymentMethod)
                    )
                }
            }
        }
    }

    /**
     * Handles actions dispatched from various settings selector dialogs.
     *
     * This function updates the UI state for the specific [SettingsSelector] based on the
     * provided [SelectorDialogAction]. When a value is successfully applied, it triggers the
     * corresponding use case to persist the updated setting.
     *
     * @param selector The specific settings category being interacted with (e.g., Language, Currency).
     * @param action The action performed within the selector dialog (e.g., selection, dismissal,
     * application).
     */
    @Suppress("UNCHECKED_CAST")
    fun onSelectorAction(
        selector: SettingsSelector,
        action: SelectorDialogAction<*>
    ) {
        when (selector) {
            SettingsSelector.LANGUAGE -> handleSelector(
                action = action as SelectorDialogAction<AppLanguage>,
                dialogState = _uiState.value.languageDialog,
                onDialogUpdate = { dialog ->
                    _uiState.update { it.copy(languageDialog = dialog) }
                },
                onApplied = { value ->
                    viewModelScope.launch { setLanguage(value) }
                }
            )

            SettingsSelector.CURRENCY -> handleSelector(
                action = action as SelectorDialogAction<CurrencyType>,
                dialogState = _uiState.value.currencyDialog,
                onDialogUpdate = { dialog ->
                    _uiState.update { it.copy(currencyDialog = dialog) }
                },
                onApplied = { value ->
                    viewModelScope.launch { setCurrency(value) }
                }
            )

            SettingsSelector.START_OF_WEEK -> handleSelector(
                action = action as SelectorDialogAction<StartOfWeek>,
                dialogState = _uiState.value.startOfWeekDialog,
                onDialogUpdate = { dialog ->
                    _uiState.update { it.copy(startOfWeekDialog = dialog) }
                },
                onApplied = { value ->
                    viewModelScope.launch { setStartOfWeek(value) }
                }
            )

            SettingsSelector.PAYMENT_METHOD -> handleSelector(
                action = action as SelectorDialogAction<PaymentMethodType>,
                dialogState = _uiState.value.paymentMethodDialog,
                onDialogUpdate = { dialog ->
                    _uiState.update { it.copy(paymentMethodDialog = dialog) }
                },
                onApplied = { value ->
                    viewModelScope.launch { setPaymentMethod(value) }
                }
            )
        }
    }

    fun onManagerCategoriesClick() {
        navigateTo(Route.CategoryManager.route)
    }

    fun onManagerShortcutsClick() {
        navigateTo(Route.ShortcutManager.route)
    }

    fun onBackClick() {
        navigateBack()
    }

    private fun navigateTo(route: String) {
        emitEvent(SettingsEvent.NavigateTo(route))
    }

    private fun navigateBack() {
        emitEvent(SettingsEvent.NavigateBack)
    }

    private fun emitEvent(event: SettingsEvent) {
        viewModelScope.launch { _events.emit(event) }
    }

    /**
     * Generic handler for managing selector dialog logic and state transitions.
     *
     * Processes actions through the [reduceSelectorDialog] function, updates the UI state via the
     * provided callback, and triggers the application of a new value if the dialog emits an
     * [SelectorDialogEffect.Applied] effect.
     *
     * @param T The type of value being selected (e.g., [AppLanguage], [CurrencyType]).
     * @param action The action performed on the selector dialog.
     * @param dialogState The current state of the specific selector dialog.
     * @param onDialogUpdate Callback to update the state in the UI flow.
     * @param onApplied Callback to execute the business logic (e.g., UseCase) when a value is confirmed.
     */
    private fun <T> handleSelector(
        action: SelectorDialogAction<T>,
        dialogState: SelectorDialogState<T>,
        onDialogUpdate: (SelectorDialogState<T>) -> Unit,
        onApplied: (T) -> Unit
    ) {
        val (newDialogState, effect) = reduceSelectorDialog(
            state = dialogState,
            action = action
        )

        onDialogUpdate(newDialogState)

        if (effect is SelectorDialogEffect.Applied) {
            onApplied(effect.value)
        }
    }
}