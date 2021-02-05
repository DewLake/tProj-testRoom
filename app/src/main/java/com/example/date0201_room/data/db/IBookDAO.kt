package com.example.date0201_room.data.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.example.date0201_room.data.Book


@Dao
interface IBookDAO {

    @Query("SELECT * FROM ${Book.TABLE_NAME} ORDER BY id DESC")
    fun getAllBooks(): LiveData<List<Book>>

    @Query("SELECT * FROM table_books WHERE (:title IS NULL OR title Like '%' || :title || '%') OR (:price IS NULL OR price = :price) ORDER By id DESC ")
    fun getBooks(title: String? = null, price: Double? = null): List<Book>

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