package com.rmw.clientapp

import com.rmw.clientapp.model.Content
import com.rmw.clientapp.model.User
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

const val BASE_URL = "http://192.168.2.56:8080"

interface LoggedInAPIService {
    @GET("/users/user/checkUser/{username}")
    suspend fun getLoggedInUser(@Path("username") username: String): User

    // TODO
    // Should eventually take the publisher Id of a publisher that the user subscribes to
    // but for now just retrieve all content since the subscriber service is not
    // on heroku yet
    @GET("/pub/content/findAll")
    suspend fun getPublisherContent(): List<Content>

    @POST("/pub/content/create")
    suspend fun createContent(@Body content: Content): Content

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