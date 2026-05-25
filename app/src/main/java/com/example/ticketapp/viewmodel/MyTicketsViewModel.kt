package com.example.ticketapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.Ticket
import com.example.core.domain.TicketRepository
import com.example.ticketapp.util.toUserMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MyTicketsUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val tickets: List<Ticket> = emptyList(),
    val errorMessage: String? = null
)

class MyTicketsViewModel(
    private val ticketRepository: TicketRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MyTicketsUiState())
    val state: StateFlow<MyTicketsUiState> = _state.asStateFlow()

    init {
        loadTickets()
    }

    fun loadTickets() {
        _state.update { it.copy(isLoading = true, errorMessage = null) }
        fetchTickets { tickets ->
            _state.update { it.copy(isLoading = false, tickets = tickets) }
        }
    }

    fun refreshTickets() {
        _state.update { it.copy(isRefreshing = true, errorMessage = null) }
        fetchTickets { tickets ->
            _state.update { it.copy(isRefreshing = false, tickets = tickets) }
        }
    }

    private fun fetchTickets(onResult: (List<Ticket>) -> Unit) {
        viewModelScope.launch {
            ticketRepository.getMyTickets().fold(
                onSuccess = { tickets ->
                    onResult(tickets)
                },
                onFailure = { e ->
                    _state.update { 
                        it.copy(
                            isLoading = false, 
                            isRefreshing = false, 
                            errorMessage = e.toUserMessage() 
                        ) 
                    }
                }
            )
        }
    }
}
