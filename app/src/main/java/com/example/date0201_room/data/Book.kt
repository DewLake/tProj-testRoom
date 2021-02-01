package com.example.date0201_room.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat


/**
 * Book
 */
@Entity(tableName = Book.TABLE_NAME)
data class Book(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,               // entity id.
    var title: String? = null,          // 書名
    var price: Double = 0.0,            // 價格
) {
    companion object {
        const val TABLE_NAME = "table_books"
    }
}


/**
 * Fetch random book
 */
fun FetchRandomBook(): Book {
    // current date and time, used for fake book data.
    val currTimeMillis = System.currentTimeMillis()
    val tmpTitle = SimpleDateFormat("MMMddHHmmss").format(currTimeMillis).toString()
    val tmpPrice = 10.0 * SimpleDateFormat("ss").format(currTimeMillis).toDouble()

    return Book(
            title = "Title_$tmpTitle",
            price = tmpPrice
        )
} // end FetchRandomBook().