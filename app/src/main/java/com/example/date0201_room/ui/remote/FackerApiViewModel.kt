package com.example.date0201_room.ui.remote

import androidx.lifecycle.*
import com.example.date0201_room.data.Book
import com.example.date0201_room.data.BookRepository
import com.example.date0201_room.data.fakerapi.model.BooksFackerApiDataSource
import com.example.date0201_room.ui.local.ItemModel
import kotlinx.coroutines.launch

/**
 * ViewModel for Facker Api Fragment
 * 註: 沿用 BooksViewModel 的 ItemModel.
 */
class FackerApiViewModel() : ViewModel() {
    // TAG
    val TAG = "[TAG]-${FackerApiViewModel::class.simpleName}"

    // Data Source
    // TODO(作成單例, getInstance)
    private val mDataSource = BookRepository()

    // books: from FackerApi, a web api.
    private val _books: MutableLiveData<List<Book>> = MutableLiveData()

    // 包裝成顯示用的 Item
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


    /**
     * get books
     */
    fun getBooks(quantity: Int) {
        val callback: (List<Book>) -> Unit = {
            _books.postValue(it)
        }

        mDataSource.getBooksFromFackerApi(quantity, callback)
    } // end getBooks().

} // end class FackerApiViewModel.