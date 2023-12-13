package com.example.lab3

import com.google.gson.annotations.SerializedName

data class Article(@SerializedName("title") val title: String,
                   @SerializedName("link") val link: String)
