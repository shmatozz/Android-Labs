package com.example.lab3

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {
    @GET("1/news")
    fun getAllNews(@Query("apikey") apikey: String): Call<ResponseArticle>
    @GET("1/news")
    fun getKeyNews(@Query("apikey") apikey: String, @Query("q") query: String): Call<ResponseArticle>
}