package com.example.ticketapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.Event
import com.example.core.domain.EventRepository
import com.example.core.domain.Ticket
import com.example.core.domain.TicketRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState<T>(
    val data: List<T> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class HomeViewModel(
    private val eventRepository: EventRepository,
    private val ticketRepository: TicketRepository
) : ViewModel() {

    private val _eventsState = MutableStateFlow(HomeUiState<Event>())
    val eventsState: StateFlow<HomeUiState<Event>> = _eventsState.asStateFlow()

    private val _ticketsState = MutableStateFlow(HomeUiState<Ticket>())
    val ticketsState: StateFlow<HomeUiState<Ticket>> = _ticketsState.asStateFlow()

    init {
        loadEvents()
        loadTickets()
    }

    fun loadEvents() {
        _eventsState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            eventRepository.getEvents()
                .onSuccess { events ->
                    _eventsState.update { it.copy(data = events, isLoading = false) }
                }
                .onFailure { error ->
                    _eventsState.update { it.copy(isLoading = false, errorMessage = error.message) }
                }
        }
    }

    fun loadTickets() {
        _ticketsState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            ticketRepository.getMyTickets()
                .onSuccess { tickets ->
                    _ticketsState.update { it.copy(data = tickets, isLoading = false) }
                }
                .onFailure { error ->
                    val message = if (error.message?.contains("401") == true) {
                        "Oturum süresi doldu, tekrar giriş yapın"
                    } else {
                        error.message
                    }
                    _ticketsState.update { it.copy(isLoading = false, errorMessage = message) }
                }
        }
    }
}
