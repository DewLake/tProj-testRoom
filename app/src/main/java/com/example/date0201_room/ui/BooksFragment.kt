package com.example.date0201_room.ui

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


class BooksFragment : Fragment(R.layout.fragment_books) {
    // TAG
    val TAG = "$[TAG]-${BooksFragment::class.simpleName}"

    // Views
    // Texts
    private lateinit var edtTitle: EditText
    private lateinit var edtPrice: EditText

    // Buttons
    private lateinit var btnAdd: Button
    private lateinit var btnDelete: Button
    private lateinit var btnUpdate: Button
    private lateinit var btnQuery: Button

    // RecyclerView
    private lateinit var rcvBooks: RecyclerView


    // ViewModel
    private lateinit var viewModel: BooksViewModel

    // BooksAdapter
    private lateinit var booksAdapter: BooksAdapter


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

        // initialize ViewMOdel
        initViewModel()


        // Initialize Views
        initViews(view)

        // Observers subscribe
        configObservers()

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


    /** Observers subscribe */
    private fun configObservers() {
        // selected (Adapter) position, 已點選的位置
        viewModel.selectedItemPosition.observe(viewLifecycleOwner, { position ->
            Log.d(TAG, "position: ${booksAdapter.previousIndex} --> $position")
            booksAdapter.notifyItemChanged(booksAdapter.previousIndex)  // (通知)已取消選取, 應要改變背景色
            booksAdapter.notifyItemChanged(position)        // (通知)被選取, 應要改變背景色
            booksAdapter.previousIndex = position           // 記錄新的位置
        })

        // selectedItem, 已點選的書本
        viewModel.selectedItem.observe(viewLifecycleOwner, Observer { book ->
            // EditText content
            edtTitle.setText(book?.title ?: "")
            edtPrice.setText(book?.price?.toString() ?: "")

            // Buttons enable state
            btnDelete.isEnabled = (book != null)        // 有點選書本時, 允許刪除
            btnUpdate.isEnabled = (book != null)        // 有點選書本時, 允許更新
            btnAdd.isEnabled = (book == null)           // 沒有點選晝本時才允許新增(因期用UI, 有點選時視為"更新"資料)
        })

        // books
        viewModel.books.observe(viewLifecycleOwner, Observer {
            Log.i(TAG, "adapter update")
            it.let { booksAdapter.data = it }
        })
    } // end setObservers().

    /** initialize Views */
    private fun initViews(view: View) {
        //
        edtTitle = view.findViewById(R.id.edtTitle__FragmentBooks)
        edtPrice = view.findViewById(R.id.edtPrice__FragmentBooks)


        /// RecyclerView, books list
        // adapter
        booksAdapter = BooksAdapter { position: Int -> onItemClickCallback(position) }
        //
        rcvBooks = view.findViewById(R.id.rcvBooksList)
        rcvBooks.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = booksAdapter
        }
//
//        viewModel.books.observe(viewLifecycleOwner, Observer {
//            Log.i("Fra", "adapter update")
//            it.let { booksAdapter.data = it }
//        })


        /// Buttons
        // Add
        btnAdd = view.findViewById<Button>(R.id.btnAdd)
        btnAdd.setOnClickListener {
            Log.i(TAG, "btnAdd clicked...")

            val book = FetchRandomBook()

            viewModel.addBook(book)
        }

        // Delete (selected item)
        btnDelete = view.findViewById<Button>(R.id.btnDleate)
        btnDelete.setOnClickListener {
//            viewModel.deleteAllBooks()
            viewModel.selectedItem.value?.let {
                // delete book
                viewModel.delete(it)

//                resetSelectedItem()
            }
        }

        // Update
        btnUpdate = view.findViewById<Button>(R.id.btnUpdate)
        btnUpdate.setOnClickListener {
            viewModel.selectedItem.value?.let { book ->

                // get user input
                val (title: String, price: Double?) = grabUserInput()
                book.title = title
                book.price = price ?: 0.0

                // update book
                viewModel.update(book)
//                (rcvBooks.adapter as BooksAdapter).data = viewModel.books.value?.toList()!!
//                viewModel.books.value = viewModel.books.value?.toList()
//                rcvBooks.adapter?.notifyDataSetChanged()
//                resetSelectedItem()

                // hide keyboard
                hideKeyboard(view)
            }
        }


        // Query
        btnQuery = view.findViewById<Button>(R.id.btnQuery)
        btnQuery.setOnClickListener {
//            val books = viewModel.books
            val (title: String?, price: Double?) = grabUserInput()
            Log.i(TAG, "Query title: $title, price: $price")

//            // query books
//            viewModel.searchBooks(title, price)
//            viewModel.books.value?.let { books -> displayBooks(books) }
            viewModel.searchBooks(title, price)

//            resetSelectedItem()

            // hide keyboard
            hideKeyboard(view)
        }
    } // end initViews()


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
        val imm: InputMethodManager = requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


    /**
     * onItemClickCallback
     * Books list item 被按下時執行此方法
     * 選定項目 - ViewModel set selectedItem
     */
    private fun onItemClickCallback(position: Int) {
        viewModel.updateSelectedItem(position)
//        selectedItemPosition = position
//
//        if (position != RecyclerView.NO_POSITION) {
//            viewModel.selectBookByPosition(position)
//        }
    }

    /**
     *
     */
    private fun displayBooks(books: List<Book>) {
        booksAdapter.submitList(books)
    }


//    private fun resetSelectedItem() {
//        val (pos, item) = Pair(viewModel.selectedItemPosition, viewModel.selectedItem.value)
//        if (pos != RecyclerView.NO_POSITION || item != null) {
//            rcvBooks.adapter?.notifyItemChanged(viewModel.selectedItemPosition)
//            viewModel.resetSelectedItem()
//        }
//    }
} // end class BooksFragment.