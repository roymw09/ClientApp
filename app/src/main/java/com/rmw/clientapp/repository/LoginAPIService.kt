package com.rmw.clientapp.repository

import com.rmw.clientapp.model.User
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

const val BASE_URL = "https://pubsub-gateway.herokuapp.com"
interface LoginAPIService {

    @POST("/users/user/create")
    suspend fun createUser(@Body user: User): User

    @GET("/users/user/checkUser/{username}")
    suspend fun checkIfUserExists(@Path("username") username: String): User

    companion object {
        var apiService: LoginAPIService? = null
        fun getInstance(): LoginAPIService {
            if (apiService == null) {
                apiService = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(LoginAPIService::class.java)
            }
            return apiService!!
        }
    }
}