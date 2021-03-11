package com.example.date0201_room.data.fakerapi.model

/**
 * Data Model from FackerApi Response
 */
data class BooksResponse(
        val code: Int,
        val data: List<RData>,
        val status: String,
        val total: Int
)