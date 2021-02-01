package com.example.date0201_room.data.db

import androidx.room.*
import com.example.date0201_room.data.Book
import com.example.date0201_room.data.TABLE_NAME_BOOKS

@Dao
interface IBookDAO {

    @Query("SELECT * FROM $TABLE_NAME_BOOKS")
    fun getAll(): List<Book>

    @Query("SELECT * FROM $TABLE_NAME_BOOKS WHERE id LIKE :id LIMIT 1")
    fun getBookById(id: Long): Book

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: Book): Long

    @Update
    fun update(item: Book): Int

    @Delete
    fun delete(item: Book): Int
}