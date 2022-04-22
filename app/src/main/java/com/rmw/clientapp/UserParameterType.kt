package com.rmw.clientapp

import android.os.Bundle
import androidx.navigation.NavType
import com.google.gson.Gson
import com.rmw.clientapp.model.User
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class UserParameterType : NavType<User>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): User? {
        return bundle.getParcelable(key) as User?
    }

    override fun parseValue(value: String): User {
        return Gson().fromJson(value, User::class.java)
    }

    override fun put(bundle: Bundle, key: String, value: User) {
        bundle.putParcelable(key, value)
    }
}