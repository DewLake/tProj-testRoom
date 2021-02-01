package com.example.date0201_room.data.db

import androidx.room.*
import com.example.date0201_room.data.Book


@Dao
interface IBookDAO {

    @Query("SELECT * FROM ${Book.TABLE_NAME}")
    suspend fun getAll(): List<Book>

    @Query("SELECT * FROM ${Book.TABLE_NAME} WHERE id LIKE :id LIMIT 1")
    suspend fun getBookById(id: Long): Book

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Book): Long

    @Update
    suspend fun update(item: Book): Int

    @Delete
    suspend fun delete(item: Book): Int
}