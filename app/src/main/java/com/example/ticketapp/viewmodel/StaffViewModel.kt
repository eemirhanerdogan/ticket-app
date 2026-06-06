package com.example.ticketapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.checkin.CheckInRepository
import com.example.core.domain.checkin.CheckInResult
import com.example.ticketapp.util.toUserMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class StaffUiState(
    val isLoading: Boolean = false,
    val lastScannedCode: String? = null,
    val result: CheckInResult? = null,
    val errorMessage: String? = null
)

class StaffViewModel(
    private val checkInRepository: CheckInRepository
) : ViewModel() {

    private val _state = MutableStateFlow(StaffUiState())
    val state: StateFlow<StaffUiState> = _state.asStateFlow()

    fun onQrScanned(content: String?) {
        if (content.isNullOrBlank()) {
            _state.update { it.copy(errorMessage = "QR kod okunamadı.", result = null) }
            return
        }

        _state.update { it.copy(isLoading = true, lastScannedCode = content, errorMessage = null, result = null) }

        viewModelScope.launch {
            checkInRepository.scan(content).fold(
                onSuccess = { res ->
                    _state.update { it.copy(isLoading = false, result = res) }
                },
                onFailure = { e ->
                    _state.update { it.copy(isLoading = false, errorMessage = e.toUserMessage()) }
                }
            )
        }
    }

    fun clearResult() {
        _state.update { it.copy(result = null, errorMessage = null, lastScannedCode = null) }
    }

    fun clearError() {
        _state.update { it.copy(errorMessage = null) }
    }
}
