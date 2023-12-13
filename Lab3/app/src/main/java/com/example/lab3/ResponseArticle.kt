package com.example.lab3

import com.google.gson.annotations.SerializedName

data class ResponseArticle(
    @SerializedName("status") val status: String,
    @SerializedName("totalResults") val totalResults: String,
    @SerializedName("results") val results: List<Article>)
