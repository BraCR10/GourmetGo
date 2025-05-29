package gourmetgo.client.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gourmetgo.client.data.models.User
import gourmetgo.client.data.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(context: Context) : ViewModel() {
    private val repository = AuthRepository(context)

    var uiState by mutableStateOf(AuthUiState())
        private set

    fun login(email: String, password: String) {
        uiState = uiState.copy(isLoading = true, error = null)

        viewModelScope.launch {
            repository.login(email, password)
                .onSuccess { user ->
                    uiState = uiState.copy(
                        isLoading = false,
                        isLoggedIn = true,
                        user = user
                    )
                }
                .onFailure { error ->
                    uiState = uiState.copy(
                        isLoading = false,
                        error = error.message ?: "Unknown Error "
                    )
                }
        }
    }

    fun logout() {
        repository.logout()
        uiState = AuthUiState()
    }

    fun checkLoginStatus() {
        if (repository.isLoggedIn()) {
            val user = repository.getCurrentUser()
            uiState = uiState.copy(
                isLoggedIn = true,
                user = user
            )
        }
    }

    fun clearError() {
        uiState = uiState.copy(error = null)
    }
}

data class AuthUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val user: User? = null,
    val error: String? = null
)