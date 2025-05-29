package gourmetgo.client.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Connection {
    // Cambia esta URL por la IP real de tu servidor
    // Para pruebas locales usa la IP de tu computadora en la red local
    // Ejemplo: "http://192.168.1.100:3000/api/"
    // Para emulador de Android Studio usa: "http://10.0.2.2:3000/api/"
    private var baseurl = "http://10.0.2.2:3000/api/"

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