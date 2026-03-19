package com.myawesome.kotlinpa2026.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myawesome.kotlinpa2026.data.local.ConfigStore
import com.myawesome.kotlinpa2026.data.repository.DiaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val serverUrl: String = "",
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: DiaryRepository,
    private val configStore: ConfigStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val url = configStore.baseUrl.first()
            _uiState.value = _uiState.value.copy(serverUrl = url)
        }
    }

    fun onServerUrlChange(value: String) {
        _uiState.value = _uiState.value.copy(serverUrl = value, error = null)
    }

    fun onEmailChange(value: String) {
        _uiState.value = _uiState.value.copy(email = value, error = null)
    }

    fun onPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(password = value, error = null)
    }

    fun login(onSuccess: () -> Unit) {
        val state = _uiState.value
        if (state.serverUrl.isBlank()) {
            _uiState.value = state.copy(error = "Server URL is required")
            return
        }
        _uiState.value = state.copy(isLoading = true, error = null)
        viewModelScope.launch {
            runCatching {
                configStore.saveBaseUrl(state.serverUrl.trimEnd('/'))
                repository.login(state.email.trim(), state.password)
            }.onSuccess {
                onSuccess()
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Login failed"
                )
            }
        }
    }

    fun checkExistingSession(onLoggedIn: () -> Unit) {
        viewModelScope.launch {
            if (repository.getToken() != null) onLoggedIn()
        }
    }
}
