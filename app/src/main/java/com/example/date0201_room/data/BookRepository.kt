package com.example.date0201_room.data

import com.example.date0201_room.data.fakerapi.model.BooksFackerApiDataSource
import com.example.date0201_room.data.fakerapi.model.BooksResponse
import com.example.date0201_room.data.fakerapi.model.RData

/**
 * 作為 提供資料(Model) 的單一來源
 */
class BookRepository() {

    // Facker Api
    private val fakerApi = BooksFackerApiDataSource

    /**
     * get books list from Facker Api.
     */
    fun getBooksFromFackerApi(
        quantity: Int,
        callback: (books: List<Book>) -> Unit
    ) {
        fakerApi.GetBooks(quantity.toString()) { booksResponse: BooksResponse ->
            val datas: List<RData> = booksResponse.data
            val books: MutableList<Book> = mutableListOf()
            datas.forEachIndexed { i, el -> books.add(el.asBook(i.toLong())) }
            callback.invoke(books)
        }
    } // end fun getBooksFromFackerApi().
} // end class BookRepository.