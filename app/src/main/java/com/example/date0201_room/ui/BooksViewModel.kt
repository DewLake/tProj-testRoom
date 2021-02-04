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

    // TAG
    val TAG = "[TAG]-${BooksViewModel::class.simpleName}"

    // books: from Room Database; observer by listAdapter.
//    val books: LiveData<List<Book>> = dataSource.getAllBooks()
//    private val _books: MutableLiveData<List<Book>> = dataSource.getAllBooks()
    val books: LiveData<List<Book>> = dataSource.getAllBooks()
//        get() = _books

    // selectedItem: set by item clicked; observer by editText, buttons.
    private val _selectedItem: MutableLiveData<Book?> = MutableLiveData(null)
    val selectedItem: LiveData<Book?>
        get() = _selectedItem


    /**
     * select book
     * While list item clicked, set the selected item.
     */
    fun select(book: Book?) {
        Log.i(TAG, "select book: $book")
        this._selectedItem.value = book
    }


    /** insert */
    fun addBook(book: Book) {
        viewModelScope.launch {
            Log.i(TAG, "add book")
            dataSource.insert(book)
        }
    }


    /** update */
    fun update(book: Book) {
        viewModelScope.launch {
            Log.i(TAG, "update book: ${book.id}")
            dataSource.update(book)
        }
    }


    /** delete all */
    fun deleteAllBooks() {
        viewModelScope.launch {
            Log.i(TAG, "delete all books...")
            dataSource.deleteAll()
        }
    }

    /** delete selected item */
    fun delete(book: Book) {
        viewModelScope.launch {
            Log.i(TAG, "delete book: ${book.id} ...")
            dataSource.delete(book)
            _selectedItem.value = null
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
