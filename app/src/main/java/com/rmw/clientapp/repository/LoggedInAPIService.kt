package com.rmw.clientapp

import com.rmw.clientapp.repository.User
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path

const val BASE_URL = "https://pubsub-gateway.herokuapp.com"


data class Content(var id: Int?, var publisher_id: Int, var content: String)
data class UserRole(var user_id: Int, var role_id: String, var role: String, var description: String, var refreshToken: String)

interface LoggedInAPIService {
    @GET("/users/user/checkUser/{username}")
    suspend fun getLoggedInUser(@Path("username") username: String): User

    // TODO
    // Should eventually take a publisher Id that the user subscribes to
    // but for now just retrieve all content since the subscriber service is not
    // on heroku yet
    @GET("/pub/content/findAll")
    suspend fun getPublisherContent(): List<Content>

    @GET("/users/role/token/publisher/{userId}")
    suspend fun getPublisherToken(@Path("userId") userId: Int): UserRole

    @GET("/pub/content/create/{token}")
    suspend fun createContent(@Path("token") token: String, @Body content: Content): Content

    companion object {
        var loggedInApiService: LoggedInAPIService? = null
        fun getInstance(): LoggedInAPIService {
            if (loggedInApiService == null) {
                loggedInApiService = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(LoggedInAPIService::class.java)
            }
            return loggedInApiService!!
        }
    }
}