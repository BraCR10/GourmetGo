package gourmetgo.client.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import gourmetgo.client.AppConfig

class Connection {

    private var baseurl = AppConfig.API_BASE_URL

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseurl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}