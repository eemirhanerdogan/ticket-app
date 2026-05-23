package com.example.ticketapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.Ticket
import com.example.core.domain.TicketRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TicketDetailUiState(
    val isLoading: Boolean = false,
    val ticket: Ticket? = null,
    val error: String? = null
)

class TicketDetailViewModel(
    private val ticketRepository: TicketRepository
) : ViewModel() {
    private val _state = MutableStateFlow(TicketDetailUiState())
    val state: StateFlow<TicketDetailUiState> = _state.asStateFlow()

    fun loadTicket(ticketId: String) {
        if (_state.value.isLoading) return

        _state.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            ticketRepository.getTicketDetail(ticketId).fold(
                onSuccess = { ticket ->
                    _state.update { it.copy(ticket = ticket, isLoading = false) }
                },
                onFailure = { e ->
                    _state.update { it.copy(isLoading = false, error = e.message ?: "Bilet detayı yüklenemedi.") }
                }
            )
        }
    }
}
