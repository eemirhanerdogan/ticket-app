package com.example.ticketapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.event.Event
import com.example.core.domain.event.EventRepository
import com.example.core.domain.purchase.Purchase
import com.example.core.domain.purchase.PurchaseItemRequest
import com.example.core.domain.purchase.PurchaseRepository
import com.example.ticketapp.util.toUserMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EventDetailUiState(
    val isLoading: Boolean = false,
    val event: Event? = null,
    val selectedQuantities: Map<String, Int> = emptyMap(),
    val errorMessage: String? = null,
    val isCreatingPurchase: Boolean = false,
    val pendingPurchase: Purchase? = null,
    val paymentSuccess: Boolean = false
) {
    val totalCents: Long
        get() = event?.ticketTypes?.sumOf { 
            (selectedQuantities[it.id] ?: 0).toLong() * it.priceCents 
        } ?: 0L

    val canPurchase: Boolean
        get() = selectedQuantities.values.any { it > 0 } && !isCreatingPurchase
}

class EventDetailViewModel(
    private val eventRepository: EventRepository,
    private val purchaseRepository: PurchaseRepository
) : ViewModel() {

    private val _state = MutableStateFlow(EventDetailUiState())
    val state: StateFlow<EventDetailUiState> = _state.asStateFlow()

    fun loadEvent(eventId: String) {
        if (_state.value.isLoading) return

        _state.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            eventRepository.getEvent(eventId).fold(
                onSuccess = { event ->
                    _state.update { it.copy(event = event, isLoading = false) }
                },
                onFailure = { e ->
                    _state.update { it.copy(isLoading = false, errorMessage = e.toUserMessage()) }
                }
            )
        }
    }

    fun increase(ticketTypeId: String) {
        val event = _state.value.event ?: return
        val ticketType = event.ticketTypes.find { it.id == ticketTypeId } ?: return
        val currentQty = _state.value.selectedQuantities[ticketTypeId] ?: 0
        
        val maxAllowed = minOf(20L, ticketType.remaining).toInt()
        
        if (currentQty < maxAllowed) {
            _state.update { 
                it.copy(
                    selectedQuantities = it.selectedQuantities + (ticketTypeId to currentQty + 1)
                )
            }
        }
    }

    fun decrease(ticketTypeId: String) {
        val currentQty = _state.value.selectedQuantities[ticketTypeId] ?: 0
        if (currentQty > 0) {
            _state.update { 
                it.copy(
                    selectedQuantities = it.selectedQuantities + (ticketTypeId to currentQty - 1)
                )
            }
        }
    }

    fun createPurchase() {
        val selectedItems = _state.value.selectedQuantities.filter { it.value > 0 }
        
        val items = selectedItems.map { (ticketTypeId, quantity) ->
            Log.d("EventDetailViewModel", "Creating purchase item: ticketTypeId=$ticketTypeId, quantity=$quantity")
            PurchaseItemRequest(ticketTypeId, quantity)
        }
        
        if (items.isEmpty()) return

        _state.update { it.copy(isCreatingPurchase = true, errorMessage = null) }

        viewModelScope.launch {
            purchaseRepository.createPurchase(items).fold(
                onSuccess = { purchase ->
                    Log.d("EventDetailViewModel", "Purchase created successfully: id=${purchase.id}")
                    _state.update { it.copy(isCreatingPurchase = false, pendingPurchase = purchase) }
                },
                onFailure = { e ->
                    Log.e("EventDetailViewModel", "Purchase creation failed", e)
                    _state.update { it.copy(isCreatingPurchase = false, errorMessage = e.toUserMessage()) }
                    if (e.message?.contains("capacity_exceeded") == true) {
                        _state.value.event?.id?.let { loadEvent(it) }
                    }
                }
            )
        }
    }

    fun confirmPayment() {
        val purchase = _state.value.pendingPurchase ?: return

        _state.update { it.copy(isCreatingPurchase = true, errorMessage = null) }

        viewModelScope.launch {
            purchaseRepository.pay(purchase.id).fold(
                onSuccess = {
                    Log.d("EventDetailViewModel", "Payment successful for purchase: id=${purchase.id}")
                    _state.update { it.copy(isCreatingPurchase = false, paymentSuccess = true, pendingPurchase = null) }
                },
                onFailure = { e ->
                    Log.e("EventDetailViewModel", "Payment failed", e)
                    _state.update { it.copy(isCreatingPurchase = false, errorMessage = e.toUserMessage()) }
                }
            )
        }
    }

    fun dismissPaymentDialog() {
        _state.update { it.copy(pendingPurchase = null) }
    }

    fun refreshEvent() {
        _state.value.event?.id?.let { loadEvent(it) }
    }
}
