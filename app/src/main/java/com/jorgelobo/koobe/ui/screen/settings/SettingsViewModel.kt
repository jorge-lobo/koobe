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
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogAction
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogEffect
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogState
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.reduceSelectorDialog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

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