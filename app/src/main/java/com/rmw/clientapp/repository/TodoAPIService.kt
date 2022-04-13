package com.rmw.clientapp

import com.rmw.clientapp.repository.User
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

const val BASE_URL = "https://hidden-tundra-10439.herokuapp.com"

interface APIService {
    @GET("/users/user/checkUser/{username}")
    suspend fun getUser(@Path("username") username: String): User

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