package com.example.edugard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AdminLoginState(
    val username: String = "",
    val pin: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

class AdminLoginViewModel : ViewModel() {
    private val _state = MutableStateFlow(AdminLoginState())
    val state: StateFlow<AdminLoginState> = _state.asStateFlow()

    // Dummy credentials
    private val DUMMY_USERNAME = "admin"
    private val DUMMY_PIN = "123456"

    fun updateUsername(username: String) {
        _state.update { it.copy(username = username, error = null) }
    }

    fun updatePin(pin: String) {
        // Limit PIN to 6 digits
        if (pin.length <= 6 && pin.all { it.isDigit() }) {
            _state.update { it.copy(pin = pin, error = null) }
        }
    }

    fun login(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            // Simulate network delay
            delay(1000)

            val currentState = _state.value
            if (currentState.username == DUMMY_USERNAME && currentState.pin == DUMMY_PIN) {
                _state.update { it.copy(isLoading = false) }
                onSuccess()
            } else {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Invalid credentials. Try admin/123456"
                    )
                }
            }
        }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }
}
