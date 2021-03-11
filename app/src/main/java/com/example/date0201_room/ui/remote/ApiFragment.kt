package com.example.date0201_room.ui.remote

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextClock
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.date0201_room.R
import com.example.date0201_room.data.fakerapi.model.BooksFackerApiDataSource
import com.example.date0201_room.ui.local.BooksAdapter

class ApiFragment() : Fragment(R.layout.fragment_books_api) {
    // TAG
    val TAG = "[TAG]-${ApiFragment::class.simpleName}"

    // ViewModel
    private lateinit var viewModel: FackerApiViewModel

    /// Views:
    private lateinit var txvTitle: TextView
    private lateinit var btnApi: Button

    // RecyclerView
    private lateinit var rcvBooks: RecyclerView
    private lateinit var booksAdapter: BooksAdapter


    // Button click listener:
    private val onApiButtonClickListener = View.OnClickListener {
        Log.d(TAG, "btnApi Clicked...")

        // call api
        val quantity = 6
//        BooksFackerApiDataSource.GetBooks(quantity.toString()) { booksResponse ->
//            println(booksResponse)
//        } // end ds.GetBooks().
        viewModel.getBooks(quantity)
    } // end val onApiButtonClickListener.


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /// initialize viewModel:
        viewModel = FackerApiViewModel()

        /// initialize views:
        initViews(view)

        /// setup observes
        viewModel.items.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "update adapter: $it")
            booksAdapter.dataset = it
        })

    } // end onViewCreated().

    private fun initViews(view: View) {
        txvTitle = view.findViewById(R.id.txvTitle__FragmentBooksApi)
        btnApi = view.findViewById(R.id.button)
        btnApi.setOnClickListener(onApiButtonClickListener)

        /// RecyclerView
        // adapter
        booksAdapter = BooksAdapter()
        booksAdapter.onItemClickCallback = { item ->
            Log.d(TAG, "RecyclerViewItemClick Func: ${item.book.id}")
        }

        // recycler view
        rcvBooks = view.findViewById(R.id.rcvBooksList__FragmentBooksApi)
        rcvBooks.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = booksAdapter
        }
    }
} // end class ApiFragment.