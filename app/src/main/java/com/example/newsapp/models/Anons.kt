package com.example.newsapp.models

import com.google.gson.annotations.SerializedName

data class Anons(
    @SerializedName("id")
    val id: Int,

    @SerializedName("user")
    val user: User,

    @SerializedName("title")
    val title: String,

    @SerializedName("avatar")
    val imageUrl: String,

    @SerializedName("content")
    val description: String,

    @SerializedName("price")
    val price: String
)

