package com.example.date0201_room.data

import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * Book
 */
@Entity(tableName = Book.TABLE_NAME)
data class Book(
    @PrimaryKey var id: Long? = 0,       // entity id.
    var title: String? = null,              // 書名
    var price: Double = 0.0,              // 價格
) {
    companion object {
        const val TABLE_NAME = "table_books"
    }
}
