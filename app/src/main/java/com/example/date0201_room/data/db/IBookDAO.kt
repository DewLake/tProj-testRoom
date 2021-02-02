package com.example.date0201_room.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.date0201_room.data.Book


@Dao
interface IBookDAO {

    @Query("SELECT * FROM ${Book.TABLE_NAME} ORDER BY id DESC")
    fun getAllBooks(): LiveData<List<Book>>

    @Query("SELECT * FROM ${Book.TABLE_NAME} WHERE id LIKE :id LIMIT 1")
    suspend fun getBookById(id: Long): Book

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Book): Long

    @Update
    suspend fun update(item: Book): Int

    @Delete
    suspend fun delete(item: Book): Int

    /**
     * Deletes all values from the table.
     *
     * This does not delete the table, only its contents.
     */
    @Query("DELETE FROM ${Book.TABLE_NAME}")
    suspend fun deleteAll()
}