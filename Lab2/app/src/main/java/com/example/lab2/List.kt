package com.example.lab2

import java.io.Serializable

data class List(val title: String, val goods: Array<Item> = emptyArray()) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as List

        if (title != other.title) return false
        if (!goods.contentEquals(other.goods)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + goods.contentHashCode()
        return result
    }
}