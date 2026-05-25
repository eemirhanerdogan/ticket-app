package com.example.ticketapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.auth.AuthRepository
import com.example.ticketapp.util.toUserMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RegisterUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
) {
    val canSubmit: Boolean get() = email.isNotBlank() && 
            password.length >= 8 && 
            password.length <= 128 &&
            password == confirmPassword && 
            !isLoading
}

class RegisterViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterUiState())
    val state: StateFlow<RegisterUiState> = _state.asStateFlow()

    fun onEmailChange(value: String) = _state.update { it.copy(email = value, errorMessage = null) }
    fun onPasswordChange(value: String) = _state.update { it.copy(password = value, errorMessage = null) }
    fun onConfirmPasswordChange(value: String) = _state.update { it.copy(confirmPassword = value, errorMessage = null) }

    fun submit() {
        val current = _state.value
        
        // Validation checks
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(current.email).matches()) {
            _state.update { it.copy(errorMessage = "Geçersiz email formatı") }
            return
        }
        
        if (current.password.length < 8 || current.password.length > 128) {
            _state.update { it.copy(errorMessage = "Şifre 8-128 karakter arasında olmalıdır") }
            return
        }

        if (current.password != current.confirmPassword) {
            _state.update { it.copy(errorMessage = "Şifreler eşleşmiyor") }
            return
        }

        _state.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            authRepository.register(current.email, current.password)
                .onSuccess {
                    _state.update { it.copy(isLoading = false, isSuccess = true) }
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, errorMessage = error.toUserMessage()) }
                }
        }
    }
}
