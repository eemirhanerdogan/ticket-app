package com.example.ticketapp.ui.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.AuthRepository
import com.example.core.domain.AuthSession
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = mutableStateOf<LoginState>(LoginState.Idle)
    val state: State<LoginState> = _state

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _state.value = LoginState.Loading
            authRepository.login(email, password)
                .onSuccess { session ->
                    _state.value = LoginState.Success(session)
                }
                .onFailure { error ->
                    _state.value = LoginState.Error(error.message ?: "Bir hata oluştu")
                }
        }
    }
}

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val session: AuthSession) : LoginState()
    data class Error(val message: String) : LoginState()
}
