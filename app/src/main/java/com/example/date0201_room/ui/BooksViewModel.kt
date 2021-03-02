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
    var books: LiveData<List<Book>> = dataSource.getAllBooks()
//    private var _books: MutableLiveData<List<Book>> = MutableLiveData()
//
//    init {
//        viewModelScope.launch { _books.postValue(dataSource.getBooks()) }
//    }
//
//    val books: LiveData<List<Book>>
//        get() = _books


    private var _selectedItemPosition: MutableLiveData<Int> = MutableLiveData(RecyclerView.NO_POSITION)
    val selectedItemPosition: LiveData<Int>
        get() = _selectedItemPosition


    // selectedItem: set by item clicked; observer by editText, buttons.
    private var _selectedItem: MutableLiveData<Book?> = MutableLiveData(null)
    val selectedItem: LiveData<Book?>
        get() = _selectedItem


//    /**
//     * select book
//     * While list item clicked, set the selected item.
//     *
//     *     @Deprecated("Use the alternative selectBookByPosition(position: Int) method.")
//     */
//    fun select(book: Book?) {
//        Log.i(TAG, "select book: $book")
//        this._selectedItem.value = book
//    }

//    /**
//     * select book
//     * While list item clicked, set the selected item.
//     */
//    fun selectBookByPosition(position: Int) {
//        Log.i(TAG, "select position: $position")
//
//        this.selectedItemPosition = position        // remember the position, used by UpdateButton OnClick.
//        this._selectedItem.value = books.value?.get(position)
//    }


    /** insert */
    fun addBook(book: Book) {
        viewModelScope.launch {
            Log.i(TAG, "add book")
            dataSource.insert(book)
//            _books.postValue(dataSource.getBooks())
            resetSelectedItem()
        }
    }


    /** update */
    fun update(book: Book) {
        viewModelScope.launch {
            Log.i(TAG, "update book: ${book.id}")
            dataSource.update(book)
//            _books.postValue(dataSource.getBooks().toList())
            resetSelectedItem()
        }
    }


    /** delete all */
    fun deleteAllBooks() {
        viewModelScope.launch {
            Log.i(TAG, "delete all books...")
            dataSource.deleteAll()
//            _books.postValue(dataSource.getBooks())
            resetSelectedItem()
        }
    }

    /** delete selected item */
    fun delete(book: Book) {
        viewModelScope.launch {
            Log.i(TAG, "delete book: ${book.id} ...")
            dataSource.delete(book)
//            _books.postValue(dataSource.getBooks())
            resetSelectedItem()
        }
    }


//    fun searchBooks(title: String?, price: Double?): Unit {
//        Thread {
//            _books.postValue(dataSource.getBooks(title, price))
//            Log.i(TAG, "getBooks: ${books.value}")
////            resetSelectedItem()
//        }.start()
//    }

    fun searchBooks(title: String?, price: Double?): Unit {
        viewModelScope.launch {
            // TODO( 回傳 LiveData, 無法重指派; 指派新 LiveData, 要解舊訂閱?")
//            _books.postValue(dataSource.getBooks(title, price))
            resetSelectedItem()
        }
    }

    /** reset selected item */
    fun resetSelectedItem() {
        _selectedItemPosition.postValue(RecyclerView.NO_POSITION)
        _selectedItem.value = null
    }


    // select item position at adapter; used by UpdateButton OnClick.
    // source from Adapter.ViewHolder Callback invoke.
    // RecyclerView Item 被點擊時, 會送來點擊位置, position.
    /** update selected item */
    fun updateSelectedItem(position: Int) {
        Log.d(TAG, "updateSelectedItem: $position")

        // 先記住之前的 position
        val prePosi = this._selectedItemPosition.value

        ///// set selectedItemPosition
        // 點選一項 Item 時
        if (position == prePosi) { // 同一筆被點選, 設定成未選取
            _selectedItemPosition.postValue(RecyclerView.NO_POSITION)
            _selectedItem.value = null
        } else {
            // 不同筆被點選時, 設定為選取位置
            _selectedItemPosition.postValue(position)
            _selectedItem.value = books.value?.get(position)
        }
    } // end updateSelectedItem().


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
