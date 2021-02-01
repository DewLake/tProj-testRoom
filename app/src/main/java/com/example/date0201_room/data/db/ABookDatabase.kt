package com.example.date0201_room.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.date0201_room.data.Book

@Database(entities = [(Book::class)], version = 1)
abstract class ABookDatabase: RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "database_book"
    }
} // end class ABookDatabase.