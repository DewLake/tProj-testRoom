package com.example.date0201_room.data.db

import androidx.room.*
import com.example.date0201_room.data.Book


@Dao
interface IBookDAO {

    @Query("SELECT * FROM ${Book.TABLE_NAME}")
    fun getAll(): List<Book>

    @Query("SELECT * FROM ${Book.TABLE_NAME} WHERE id LIKE :id LIMIT 1")
    fun getBookById(id: Long): Book

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: Book): Long

    @Update
    fun update(item: Book): Int

    @Delete
    fun delete(item: Book): Int
}