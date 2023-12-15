package com.example.lab3

import com.google.gson.annotations.SerializedName

data class Article(@SerializedName("title") var title: String?,
                   @SerializedName("link") var link: String?,
                   @SerializedName("description") var description: String?,
                   @SerializedName("pubDate") var pubDate: String?)
