package gourmetgo.client.data.models.additional
import gourmetgo.client.data.models.User

data class AuthUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val user: User? = null,
    val error: String? = null
)