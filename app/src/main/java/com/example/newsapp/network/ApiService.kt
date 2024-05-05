package com.example.newsapp.network

import com.example.newsapp.models.Anons
import com.example.newsapp.models.News
import com.example.newsapp.models.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @GET("users")
    suspend fun getAllUsers(): List<User>

    @GET("news?_relations=users")
    suspend fun getAllNews(): List<News>

    @GET("announcement?_relations=users")
    suspend fun getAllAnons(): List<Anons>

    @DELETE("news/{id}")
    suspend fun deleteNews(@Path("id") id: Int)

    @DELETE("announcement/{id}")
    suspend fun deleteAnons(@Path("id") id: Int)

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: String): User

    @POST("news")
    suspend fun addNews(@Body news: News): Response<Void>

    @POST("announcement")
    suspend fun addAnons(@Body anons: Anons): Response<Void>

}