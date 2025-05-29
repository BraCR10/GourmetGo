package gourmetgo.client.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Connection {
    private var baseurl = "http://0.0.0.0/api/"

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