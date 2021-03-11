package com.example.date0201_room.ui.local

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.date0201_room.R
import com.example.date0201_room.data.Book
import com.example.date0201_room.data.FetchRandomBook
import com.example.date0201_room.data.db.BookDatabase
import com.example.date0201_room.util.setupClearButtonWithAction


class BooksFragment : Fragment(R.layout.fragment_books) {
    // TAG
    val TAG = "$[TAG]-${BooksFragment::class.simpleName}"

    // ViewModel
    private lateinit var viewModel: BooksViewModel


    ///// Views
    // Texts
    private lateinit var edtTitle: EditText
    private lateinit var edtPrice: EditText

    // Buttons
    private lateinit var btnAdd: Button
    private lateinit var btnDelete: Button
    private lateinit var btnUpdate: Button
    private lateinit var btnQuery: Button

    // BooksAdapter
    private lateinit var booksAdapter: BooksAdapter

    // RecyclerView
    private lateinit var rcvBooks: RecyclerView


    /** onCreateView */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // View
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    /** onViewCreated */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "onViewCreated: $this")

        // initialize ViewModel
        initViewModel()

        // Initialize Views
        initViews(view)

        // Observers subscribe
        setUpObservers()

    } // end onViewCreated().


    /** initialize ViewModel */
    private fun initViewModel() {
        // application
        val app = requireNotNull(activity).application

        // dataSource
        val ds = BookDatabase.getInstance(app).getBookDao()

        // viewModel
        viewModel = ViewModelProvider(
                this,
                BooksViewModel.BooksViewModelFactory(ds, app)
        ).get(BooksViewModel::class.java)
    } // end initViewModel().



    /** initialize Views */
    private fun initViews(view: View) {
        //
        edtTitle = view.findViewById(R.id.edtTitle__FragmentBooks)
        edtTitle.setupClearButtonWithAction()
        edtPrice = view.findViewById(R.id.edtPrice__FragmentBooks)
        edtPrice.setupClearButtonWithAction()


        /// RecyclerView, books list
        // adapter
        booksAdapter = BooksAdapter()
        booksAdapter.onItemClickCallback = { item ->
            Log.d(TAG, "CallbackFun: ${item.book.id}")
            viewModel.setupSelectedItem(item)
        }

        // recycler view.
        rcvBooks = view.findViewById(R.id.rcvBooksList)
        rcvBooks.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = booksAdapter
        }


        /// Buttons
        // Add
        btnAdd = view.findViewById<Button>(R.id.btnAdd)
        btnAdd.setOnClickListener {
            Log.i(TAG, "btnAdd clicked...")

            val book = grabBook()

            viewModel.addBook(book)
        }

        // Delete (selected item)
        btnDelete = view.findViewById<Button>(R.id.btnDleate)
        btnDelete.setOnClickListener {
//            viewModel.deleteAllBooks()
            viewModel.selectedItem.value?.let {
                // delete item
                viewModel.delete(it)
            }
        }

        // Update
        btnUpdate = view.findViewById<Button>(R.id.btnUpdate)
        btnUpdate.setOnClickListener {
            viewModel.selectedItem.value?.let { item ->
                val book = item.book

                // get user input
                val (title: String, price: Double?) = grabUserInput()
                book.title = title
                book.price = price ?: 0.0

                // update book
                viewModel.update(book)

                // hide keyboard
                hideKeyboard(view)
            }
        }


        // Query
        btnQuery = view.findViewById<Button>(R.id.btnQuery)
        btnQuery.setOnClickListener {

            // 取得使用者輸入
            val (title: String?, price: Double?) = grabUserInput()
            Log.i(TAG, "Query title: $title, price: $price")

//            // query books
//            viewModel.searchBooks(title, price)
//            viewModel.books.value?.let { books -> displayBooks(books) }
            viewModel.searchBooks(title, price)

            // hide keyboard
            hideKeyboard(view)
        }
    } // end initViews()


    /** Observers subscribe */
    private fun setUpObservers() {
        // Items
        viewModel.items.observe(viewLifecycleOwner, Observer { list ->
            Log.d(TAG, "adapter update")
            booksAdapter.dataset = list
        })


        // selectedItem, 已點選的書本
        viewModel.selectedItem.observe(viewLifecycleOwner, Observer { item ->
            val isSelected = (item != null)

            // Buttons enable state
            btnDelete.isEnabled = isSelected        // 有點選書本時, 允許刪除
            btnUpdate.isEnabled = isSelected        // 有點選書本時, 允許更新
            btnAdd.isEnabled = (!isSelected)           // 沒有點選晝本時才允許新增(因共用UI, 有點選時視為"更新"資料)

            // editText value
            if (item != null) {
                val book = item.book // EditText content
                edtTitle.setText(book.title ?: "")
                edtPrice.setText(book.price.toString())

                // 觸發背景色設定
                booksAdapter.notifyDataSetChanged()
            }
        })

    } // end setObservers().

    /** 取得 Book to add */
    private fun grabBook(): Book {
        val (title, price) = grabUserInput()

        return if (title.isEmpty() || price == null) {
            FetchRandomBook()
        } else {
            Book(title = title, price = price)
        }
    }


    /** grab user input */
    private fun grabUserInput(): Pair<String, Double?> {
        val title: String = edtTitle.text.toString().trim()
        val p = edtPrice.text.toString().trim()
        val price: Double? = if (p.isNullOrEmpty()) {
            null
        } else {
            p.toDouble()
        }
        return Pair(title, price)
    }

    /** hide keyboard */
    private fun hideKeyboard(view: View) {
        val imm: InputMethodManager =
            requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

} // end class BooksFragment.