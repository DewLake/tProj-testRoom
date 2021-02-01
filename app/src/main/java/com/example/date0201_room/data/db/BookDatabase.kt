package com.example.date0201_room.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.date0201_room.data.Book

@Database(entities = [(Book::class)], exportSchema = false, version = 2)
abstract class BookDatabase: RoomDatabase() {

    // Book DAO:
    abstract fun getBookDao(): IBookDAO

    //
    companion object {
        const val DATABASE_NAME = "database_book"

        /**
         * provide a database
         */
        @Volatile           // 避免被快取, 直接由主記憶體存取值  // make sure the value of INSTANCE is always up-to-date and the same
        private var INSTANCE: BookDatabase? = null

        fun getInstance(context: Context): BookDatabase {
            // makes sure the database only gets initialized once
            synchronized(this) { // only one thread of execution at a time can enter this block of code.
                var tempInst = INSTANCE
                if(tempInst == null) {
                    tempInst = Room.databaseBuilder(context.applicationContext, BookDatabase::class.java, DATABASE_NAME)
                        .fallbackToDestructiveMigration()       // destroy and rebuild the database. (註: 資料會遺失)
                        .build()
                    // TODO(when close?)
                    INSTANCE = tempInst
                }
                return tempInst
            } // end synchronized(){}.
        } // end getInstance().

    } // end companion object{}.

} // end class ABookDatabase.