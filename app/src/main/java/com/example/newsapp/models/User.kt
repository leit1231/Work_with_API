package com.example.newsapp.models

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    val id: Int,

    @SerializedName("full_name")
    val fullName: String,

    @SerializedName("avatar")
    val avatar: String
)
