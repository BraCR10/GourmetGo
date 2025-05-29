package gourmetgo.client.data.repository

import android.content.Context
import android.util.Log
import gourmetgo.client.data.remote.Connection
import gourmetgo.client.data.localStorage.SharedPrefsManager
import gourmetgo.client.data.mockups.UserMockup
import gourmetgo.client.data.models.User
import gourmetgo.client.data.models.dtos.LoginRequest

class AuthRepository(context: Context) {
    private val connection = Connection()
    private val sharedPrefs = SharedPrefsManager(context)
    private val useMockup = true

    suspend fun login(email: String, password: String): Result<User> {
        return try {
            if (useMockup) {
                loginWithMockup(email, password)
            } else {
                loginWithApi(email, password)
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error in login", e)
            Result.failure(Exception("Connection error: ${e.message}"))
        }
    }

    private fun loginWithMockup(email: String, password: String): Result<User> {
        return try {
            val user = UserMockup.validateCredentials(email, password)

            if (user != null) {
                val fakeToken = "mock_token_${System.currentTimeMillis()}"

                sharedPrefs.saveToken(fakeToken)
                sharedPrefs.saveUser(user)

                Result.success(user)
            } else {
                Result.failure(Exception("Bad credentials"))
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error in mockup login", e)
            Result.failure(e)
        }
    }

    private suspend fun loginWithApi(email: String, password: String): Result<User> {
        return try {
            val response = connection.apiService.login(
                LoginRequest(email, password)
            )

            sharedPrefs.saveToken(response.token)
            sharedPrefs.saveUser(response.user)

            Result.success(response.user)
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error in API login", e)
            Result.failure(Exception("Error connecting to the server"))
        }
    }

    fun logout() {
        try {
            sharedPrefs.logout()
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error in logout", e)
        }
    }

    fun isLoggedIn(): Boolean {
        return try {
            sharedPrefs.isLoggedIn()
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error checking login status", e)
            false
        }
    }

    fun getCurrentUser(): User? {
        return try {
            sharedPrefs.getUser()
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
}