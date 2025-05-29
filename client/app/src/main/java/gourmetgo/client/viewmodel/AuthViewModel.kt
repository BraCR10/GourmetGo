package gourmetgo.client.viewmodel
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gourmetgo.client.data.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(  context: Context) : ViewModel() {
    private val repository = AuthRepository(context)

    fun login(email: String, password: String) {
        viewModelScope.launch {
            repository.login(email, password)
                .onSuccess { user ->
                    println("Login success: ${user.name}")
                    println("Token storage: ${repository.getToken()}")
                }
                .onFailure { error ->
                    println("Error: ${error.message}")
                }
        }
    }

    fun checkLoginStatus() {
        if (repository.isLoggedIn()) {
            val user = repository.getCurrentUser()
            println("User logged: ${user?.name}")
        }
    }
}