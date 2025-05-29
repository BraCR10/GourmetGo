package gourmetgo.client.data.repository

import android.content.Context
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
        return if (useMockup) {
            loginWithMockup(email, password)
        } else {
            loginWithApi(email, password)
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
                Result.failure(Exception("Bad credentials "))
            }
        } catch (e: Exception) {
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
            Result.failure(e)
        }
    }

    fun logout() {
        sharedPrefs.logout()
    }

    fun isLoggedIn(): Boolean {
        return sharedPrefs.isLoggedIn()
    }

    fun getCurrentUser(): User? {
        return sharedPrefs.getUser()
    }

    fun getToken(): String? {
        return sharedPrefs.getToken()
    }


}