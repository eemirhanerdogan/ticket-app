package com.example.ticketapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.purchase.Purchase
import com.example.core.domain.purchase.PurchaseRepository
import com.example.ticketapp.util.toUserMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MyPurchasesUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val purchases: List<Purchase> = emptyList(),
    val errorMessage: String? = null,
    val payingPurchaseId: String? = null,
    val paymentSuccess: Boolean = false
)

class MyPurchasesViewModel(
    private val purchaseRepository: PurchaseRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MyPurchasesUiState())
    val state: StateFlow<MyPurchasesUiState> = _state.asStateFlow()

    init {
        loadPurchases()
    }

    fun loadPurchases() {
        _state.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            purchaseRepository.getMyPurchases().fold(
                onSuccess = { list ->
                    _state.update { it.copy(isLoading = false, purchases = list) }
                },
                onFailure = { e ->
                    _state.update { it.copy(isLoading = false, errorMessage = e.toUserMessage()) }
                }
            )
        }
    }

    fun refreshPurchases() {
        _state.update { it.copy(isRefreshing = true, errorMessage = null) }
        viewModelScope.launch {
            purchaseRepository.getMyPurchases().fold(
                onSuccess = { list ->
                    _state.update { it.copy(isRefreshing = false, purchases = list) }
                },
                onFailure = { e ->
                    _state.update { it.copy(isRefreshing = false, errorMessage = e.toUserMessage()) }
                }
            )
        }
    }

    fun continuePayment(purchaseId: String) {
        _state.update { it.copy(payingPurchaseId = purchaseId, errorMessage = null) }
        viewModelScope.launch {
            purchaseRepository.pay(purchaseId).fold(
                onSuccess = {
                    _state.update { it.copy(payingPurchaseId = null, paymentSuccess = true) }
                },
                onFailure = { e ->
                    _state.update { it.copy(payingPurchaseId = null, errorMessage = e.toUserMessage()) }
                }
            )
        }
    }

    fun consumePaymentSuccess() {
        _state.update { it.copy(paymentSuccess = false) }
    }
}
