package gourmetgo.client.data.repository

import android.content.Context
import android.util.Log
import gourmetgo.client.AppConfig
import gourmetgo.client.data.remote.Connection
import gourmetgo.client.data.localStorage.SharedPrefsManager
import gourmetgo.client.data.mockups.UserMockup
import gourmetgo.client.data.models.User
import gourmetgo.client.data.models.dtos.LoginRequest
import kotlinx.coroutines.delay

class AuthRepository(context: Context) {
    private val connection = Connection()
    private val sharedPrefs = SharedPrefsManager(context)

    suspend fun login(email: String, password: String): Result<User> {
        return try {
            if (AppConfig.USE_MOCKUP) {
                loginWithMockup(email, password)
            } else {
                loginWithApi(email, password)
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error in login", e)
            Result.failure(Exception("Error : ${e.message}"))
        }
    }

    private suspend fun loginWithMockup(email: String, password: String): Result<User> {
        return try {
            if (AppConfig.ENABLE_LOGGING) {
                Log.d("AuthRepository", "Attempting mockup login for: $email")
            }
            delay(AppConfig.MOCK_NETWORK_DELAY)

            val user = UserMockup.validateCredentials(email, password)

            if (user != null) {
                val fakeToken = "mock_token_${System.currentTimeMillis()}"

                sharedPrefs.saveToken(fakeToken)
                sharedPrefs.saveUser(user)

                if (AppConfig.ENABLE_LOGGING) {
                    Log.d("AuthRepository", "Mockup login successful for user: ${user.name} (${user.role})")
                }

                Result.success(user)
            } else {
                if (AppConfig.ENABLE_LOGGING) {
                    Log.w("AuthRepository", "Mockup login failed for: $email")
                }
                Result.failure(Exception("Credenciales incorrectas"))
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error in mockup login", e)
            Result.failure(Exception("Error interno: ${e.message}"))
        }
    }

    private suspend fun loginWithApi(email: String, password: String): Result<User> {
        return try {
            if (AppConfig.ENABLE_LOGGING) {
                Log.d("AuthRepository", "Attempting API login for: $email")
            }

            val response = connection.apiService.login(
                LoginRequest(email, password)
            )

            sharedPrefs.saveToken(response.token)
            sharedPrefs.saveUser(response.user)

            if (AppConfig.ENABLE_LOGGING) {
                Log.d("AuthRepository", "API login successful for user: ${response.user.name}")
            }

            Result.success(response.user)
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error in API login", e)
            Result.failure(Exception("Error connection server"))
        }
    }

    fun logout() {
        try {
            sharedPrefs.logout()
            if (AppConfig.ENABLE_LOGGING) {
                Log.d("AuthRepository", "Logout successful")
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error in logout", e)
        }
    }

    fun isLoggedIn(): Boolean {
        return try {
            val isLoggedIn = sharedPrefs.isLoggedIn()
            if (AppConfig.ENABLE_LOGGING) {
                Log.d("AuthRepository", "User logged in status: $isLoggedIn")
            }
            isLoggedIn
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error checking login status", e)
            false
        }
    }

    fun getCurrentUser(): User? {
        return try {
            val user = sharedPrefs.getUser()
            if (AppConfig.ENABLE_LOGGING && user != null) {
                Log.d("AuthRepository", "Current user: ${user.name} (${user.role})")
            }
            user
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error getting current user", e)
            null
        }
    }

    fun getToken(): String? {
        return try {
            sharedPrefs.getToken()
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error getting token", e)
            null
        }
    }

    // FUTURE FEATURES
    suspend fun refreshToken(): Result<String> {
        return try {
            if (AppConfig.USE_MOCKUP) {
                delay(500)
                val newToken = "refreshed_mock_token_${System.currentTimeMillis()}"
                sharedPrefs.saveToken(newToken)
                Result.success(newToken)
            } else {
                // TODO: lOGIN TO REFRESH WITH API
                Result.failure(Exception("Refresh token not implemented"))
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error refreshing token", e)
            Result.failure(e)
        }
    }

    suspend fun forgotPassword(email: String): Result<String> {
        return try {
            if (AppConfig.USE_MOCKUP) {
                delay(1000)
                val user = UserMockup.getUserByEmail(email)
                if (user != null) {
                    Result.success("Código de recuperación enviado a $email")
                } else {
                    Result.failure(Exception("Email no register"))
                }
            } else {
                // TODO: Implement forgot password with API
                Result.failure(Exception("Forgot password not implemented"))
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error in forgot password", e)
            Result.failure(e)
        }
    }
}