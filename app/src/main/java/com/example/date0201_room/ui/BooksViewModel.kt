package com.example.date0201_room.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.date0201_room.data.Book
import com.example.date0201_room.data.db.BookDatabase
import com.example.date0201_room.data.db.IBookDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.IllegalArgumentException

/**
 * ViewModel for BooksFragment.
 */
class BooksViewModel(
    val dataSource: IBookDAO,
    application: Application
) : AndroidViewModel(application) {
    //
    val books:LiveData<List<Book>> = dataSource.getAllBooks()


    /** insert */
    fun addBook(book: Book) {
        viewModelScope.launch {
            Log.i("VM", "add book")
            dataSource.insert(book)
        }
    }

    /** delete all */
    fun deleteAllBooks() {
        viewModelScope.launch {
            Log.i("VM", "delete all books...")
            dataSource.deleteAll()
        }
    }


    /////////////////////////////////////////////////////// ViewModel Factory:
    class BooksViewModelFactory(
        private val dataSource: IBookDAO,
        private val application: Application
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(BooksViewModel::class.java)) {
                return BooksViewModel(dataSource, application) as T
            }
            throw IllegalArgumentException("Unknown VewModel class.")       // the value is null.
        }

    }
    /////////////////////////////////////////////////////// ViewModel Factory end.
} // end class BooksViewModel.
