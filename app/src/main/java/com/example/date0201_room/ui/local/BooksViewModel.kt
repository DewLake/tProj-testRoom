package com.example.date0201_room.ui.local

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.date0201_room.data.Book
import com.example.date0201_room.data.db.IBookDAO
import kotlinx.coroutines.launch

/**
 * Item Model
 */

class ItemModel(val book: Book) {

    // 是否被選取
    var isSelected: Boolean = false

} // end class ItemModel.


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
//    var books: LiveData<List<Book>> = dataSource.getAllBooks()
    private val _books: MutableLiveData<List<Book>> = MutableLiveData()

    init {
        // init _books from dataSource.
        viewModelScope.launch { _books.postValue(dataSource.getBooks()) }
    }

    // 包裝成 顯示用的 item
    val items: LiveData<List<ItemModel>> = Transformations.map(_books) { list ->
        val datas = list.map { b -> ItemModel(b) }
        return@map datas
    }

    // selectedItem: set by item clicked; observer by editText, buttons.
    // 註: 請用 setupSelectedItem, 而不自己用 _selectedItem.postValue(item)
    private var _selectedItem: MutableLiveData<ItemModel?> = MutableLiveData(null)
    val selectedItem: LiveData<ItemModel?>
        get() = _selectedItem


    /**
     * 設定 被選取的 item
     * 註: 請用 setupSelectedItem, 而不自己用 _selectedItem.postValue(item)
     */
    fun setupSelectedItem(item: ItemModel?) {
        // 先回覆舊的狀態
        val preSelectedItem = this._selectedItem.value
        preSelectedItem?.isSelected = false

        // 再更新 新的狀態
        item?.isSelected = true
        this._selectedItem.postValue(item)
    }


    /** insert */
    fun addBook(book: Book) {
        viewModelScope.launch {
            Log.i(TAG, "add book")
            dataSource.insert(book)
            _books.postValue(dataSource.getBooks())         // !!註: Transformation.map 會包裝成 ItemModel.
            setupSelectedItem(null)
        }
    }


    /** update */
    fun update(book: Book) {
        viewModelScope.launch {
            Log.i(TAG, "update book: ${book.id}")
            dataSource.update(book)
            _books.postValue(dataSource.getBooks().toList())
//            setupSelectedItem(null)           // 修改者 仍保持選取狀態
        }
    }


    /** delete all */
    fun deleteAllBooks() {
        viewModelScope.launch {
            Log.i(TAG, "delete all books...")
            dataSource.deleteAll()
            _books.postValue(dataSource.getBooks())
            setupSelectedItem(null)
        }
    }

    /** delete selected item */
    fun delete(item: ItemModel) {
        viewModelScope.launch {
            Log.i(TAG, "delete book: ${item.book.id} ...")
            dataSource.delete(item.book)
            _books.postValue(dataSource.getBooks())
            setupSelectedItem(null)
        }
    }

    /**
     * 依 title, price 條件式搜詢
     *  若 為(null, null)時, 則為全部資料
     *  此處為後端處理, 已作 SQL 語句.
     */
    fun searchBooks(title: String?, price: Double?): Unit {
        viewModelScope.launch {
            _books.postValue(dataSource.getBooks(title, price))
            setupSelectedItem(null)
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
