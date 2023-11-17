package com.example.lab2

import java.io.Serializable

data class Item(val title: String, var isBought: Boolean = false) : Serializable