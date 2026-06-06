package com.example.ticketapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.Ticket
import com.example.core.domain.TicketRepository
import com.example.core.domain.auth.AuthRepository
import com.example.core.domain.auth.UserRole
import com.example.core.domain.event.Event
import com.example.core.domain.event.EventRepository
import com.example.ticketapp.util.toUserMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val isEventsLoading: Boolean = false,
    val events: List<Event> = emptyList(),
    val eventsError: String? = null,
    val isTicketsLoading: Boolean = false,
    val tickets: List<Ticket> = emptyList(),
    val ticketsError: String? = null,
    val userRole: UserRole? = null
)

class HomeViewModel(
    private val eventRepository: EventRepository,
    private val ticketRepository: TicketRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    init {
        observeUserRole()
        loadEvents()
        loadTickets()
    }

    private fun observeUserRole() {
        viewModelScope.launch {
            authRepository.userRole.collect { role ->
                _state.update { it.copy(userRole = role) }
            }
        }
    }

    fun loadEvents() {
        if (_state.value.isEventsLoading) return

        _state.update { it.copy(isEventsLoading = true, eventsError = null) }

        viewModelScope.launch {
            eventRepository.getEvents().fold(
                onSuccess = { list ->
                    _state.update { it.copy(events = list, isEventsLoading = false, eventsError = null) }
                },
                onFailure = { e ->
                    _state.update {
                        it.copy(
                            isEventsLoading = false,
                            eventsError = e.toUserMessage()
                        )
                    }
                }
            )
        }
    }

    fun loadTickets() {
        if (_state.value.isTicketsLoading) return

        _state.update { it.copy(isTicketsLoading = true, ticketsError = null) }

        viewModelScope.launch {
            ticketRepository.getMyTickets().fold(
                onSuccess = { list ->
                    _state.update { it.copy(tickets = list, isTicketsLoading = false, ticketsError = null) }
                },
                onFailure = { e ->
                    _state.update {
                        it.copy(
                            isTicketsLoading = false,
                            ticketsError = e.toUserMessage()
                        )
                    }
                }
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}
