package com.example.date0201_room.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Book
 */
@Entity(tableName = "table_books")
data class Book(
    @PrimaryKey var id: Int?,       // entity id.
    var title: String? = null,              // 書名
    var price: Double = 0.0,              // 價格
)
