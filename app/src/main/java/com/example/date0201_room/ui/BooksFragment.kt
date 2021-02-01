package com.example.date0201_room.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.date0201_room.R
import com.example.date0201_room.data.Book
import com.example.date0201_room.data.FetchRandomBook
import com.example.date0201_room.data.db.BookDatabase

class BooksFragment: Fragment(R.layout.fragment_books) {
    // TAG
    val TAG = "$[TAG]-${BooksFragment::class.simpleName}"

    // ViewModel
    lateinit var booksViewModel: BooksViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // initialize ViewMOdel
        initViewModel()

        // View
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    /** initialize ViewModel */
    private fun initViewModel() {
        // application
        val app = requireNotNull(activity).application

        // dataSource
        val ds = BookDatabase.getInstance(app).getBookDao()

        // viewModel
        booksViewModel = ViewModelProvider(
            this,
            BooksViewModel.BooksViewModelFactory(ds, app)
        ).get(BooksViewModel::class.java)
    } // end initViewModel().


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // buttons
        view.findViewById<Button>(R.id.btnAdd).setOnClickListener {
            Log.i(TAG, "btnAdd clicked...")

            val book = FetchRandomBook()

            booksViewModel.addBook(book)
        }
    }
} // end class BooksFragment.