package com.example.date0201_room.data.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.example.date0201_room.data.Book
import kotlinx.coroutines.flow.Flow


@Dao
interface IBookDAO {

    /** 取得所有 Books */
    @Query("SELECT * FROM ${Book.TABLE_NAME} ORDER BY id DESC")
    fun getAllBooks(): LiveData<List<Book>>
//    @Query("SELECT * FROM table_books WHERE (:title IS NULL OR title Like '%' || :title || '%') AND (:price IS NULL OR price = :price) ORDER By id DESC ")
//    fun getAllBooks(title: String? = null, price: Double? = null): LiveData<List<Book>>

    /** 查詢, 可條件式查詢 */
    @Query("SELECT * FROM table_books WHERE (:title IS NULL OR title Like '%' || :title || '%') AND (:price IS NULL OR price = :price) ORDER By id DESC ")
    suspend fun getBooks(title: String? = null, price: Double? = null): List<Book>
//    fun getBooks(title: String? = null, price: Double? = null): List<Book>
//@Query("SELECT * FROM table_books WHERE (:title IS NULL OR title Like '%' || :title || '%') AND (:price IS NULL OR price = :price) ORDER By id DESC ")
//fun getBooks(title: String? = "u", price: Double? = 10.0): List<Book>

    /**
     * 取得所有 Books
     *  return type: flow<List<Book>>
     */
//    @Query("SELECT * FROM ${Book.TABLE_NAME} ORDER BY id DESC")
//    fun getBooksFlow(): Flow<List<Book>>
    @Query("SELECT * FROM table_books WHERE (:title IS NULL OR title Like '%' || :title || '%') AND (:price IS NULL OR price = :price) ORDER By id DESC ")
    fun getBooksFlow(title: String? = null, price: Double? = null): Flow<List<Book>>


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