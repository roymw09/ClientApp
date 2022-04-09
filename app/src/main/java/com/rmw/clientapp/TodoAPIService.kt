package com.rmw.clientapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class Todo(var id: Int, var username: String, var roles: String)

const val BASE_URL = "https://hidden-tundra-10439.herokuapp.com"

interface APIService {
    @GET("/users/user")
    suspend fun getTodos(): List<Todo>

    companion object {
        var apiService: APIService? = null
        fun getInstance(): APIService {
            if (apiService == null) {
                apiService = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(APIService::class.java)
            }
            return apiService!!
        }
    }
}