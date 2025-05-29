package gourmetgo.client.data.repository

import gourmetgo.client.data.models.User
import gourmetgo.client.data.models.dtos.LoginRequest
import gourmetgo.client.data.remote.Connection
import gourmetgo.client.data.localStorage.SharedPrefsManager
import android.content.Context



class AuthRepository(context: Context){
    private val connection = Connection()
    private val sharedPrefs = SharedPrefsManager(context)

    suspend fun login(email: String, password: String): Result<User> {
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