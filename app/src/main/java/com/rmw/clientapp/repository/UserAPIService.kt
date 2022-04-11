package com.rmw.clientapp.repository

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

// Data retrieved from UMS
data class User(var id: Int?, var username: String, var roles: String?)

const val BASE_URL = "https://hidden-tundra-10439.herokuapp.com"
interface UserAPIService {

    @POST("/users/user/create")
    suspend fun createUser(@Body user: User): User

    @GET("/users/user/checkUser/{username}")
    suspend fun checkIfUserExists(@Path("username") username: String): User

    companion object {
        var apiService: UserAPIService? = null
        fun getInstance(): UserAPIService {
            if (apiService == null) {
                apiService = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(UserAPIService::class.java)
            }
            return apiService!!
        }
    }
}