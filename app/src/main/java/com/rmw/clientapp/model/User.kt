package com.rmw.clientapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class User(var id: Int?, var username: String, var roles: String?) : Parcelable