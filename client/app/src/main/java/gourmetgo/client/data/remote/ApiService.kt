package gourmetgo.client.data.remote

import gourmetgo.client.data.models.dtos.LoginRequest
import gourmetgo.client.data.models.dtos.LoginResponse
import gourmetgo.client.data.models.dtos.ExperiencesResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("experiences")
    suspend fun getExperiences(): ExperiencesResponse
}