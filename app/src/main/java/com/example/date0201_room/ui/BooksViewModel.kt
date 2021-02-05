package com.example.date0201_room.ui

import android.app.Application
import android.provider.ContactsContract.CommonDataKinds.Note
import android.util.Log
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import com.example.date0201_room.data.Book
import com.example.date0201_room.data.db.IBookDAO
import kotlinx.coroutines.launch


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
    private var _books: MutableLiveData<List<Book>> = MutableLiveData()

    init {
        Thread {
            _books.postValue(dataSource.getBooks())
        }.start()
    }

    val books: LiveData<List<Book>>
        get() = _books

    // select item position at adapter; used by UpdateButton OnClick.
    // source from Adapter.ViewHolder Callback invoke.
    var selectedItemPosition: Int = RecyclerView.NO_POSITION;

    // selectedItem: set by item clicked; observer by editText, buttons.
    private var _selectedItem: MutableLiveData<Book?> = MutableLiveData(null)
    val selectedItem: LiveData<Book?>
        get() = _selectedItem


    /**
     * select book
     * While list item clicked, set the selected item.
     *
     *     @Deprecated("Use the alternative selectBookByPosition(position: Int) method.")
     */
    fun select(book: Book?) {
        Log.i(TAG, "select book: $book")
        this._selectedItem.value = book
    }

    /**
     * select book
     * While list item clicked, set the selected item.
     */
    fun selectBookByPosition(position: Int) {
        Log.i(TAG, "select position: $position")
        this.selectedItemPosition = position        // remember the position, used by UpdateButton OnClick.
        this._selectedItem.value = books.value?.get(position)
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
            resetSelectedItem()
        }
    }


    /** delete all */
    fun deleteAllBooks() {
        viewModelScope.launch {
            Log.i(TAG, "delete all books...")
            dataSource.deleteAll()
            resetSelectedItem()
        }
    }

    /** delete selected item */
    fun delete(book: Book) {
        viewModelScope.launch {
            Log.i(TAG, "delete book: ${book.id} ...")
            dataSource.delete(book)
            resetSelectedItem()
        }
    }

    /** reset selected item */
    private fun resetSelectedItem() {
        selectedItemPosition = RecyclerView.NO_POSITION
        _selectedItem.value = null
    }

    fun getBooks(title: String?, price: Double?): Unit {
        Thread {
            _books.postValue(dataSource.getBooks(title, price))
            Log.i(TAG, "getBooks: ${books.value}")
        }.start()
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
