package com.example.date0201_room.data.fakerapi.model

import com.example.date0201_room.data.Book

data class RData(
    val author: String,
    val description: String,
    val genre: String,
    val image: String,
    val isbn: String,
    val published: String,
    val publisher: String,
    val title: String
) {

    /** Transformation as class Book */
    fun asBook(id: Long? = null): Book {
        val title: String = this.title
        val price: Double = (this.isbn.toDouble()) / (1000.0)

        return Book(id = id, title = title, price = price)
    }
} // end data class RData().