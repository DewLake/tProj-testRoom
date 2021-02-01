package com.example.date0201_room.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.date0201_room.R
import com.example.date0201_room.data.Book
import com.example.date0201_room.data.db.ABookDatabase

class BooksFragment: Fragment(R.layout.fragment_books) {
    // TAG
    val TAG = "$[TAG]-${BooksFragment::class.simpleName}"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // dao
        val dao = ABookDatabase.getInstance(view.context).getBookDao()


        // buttons
        view.findViewById<Button>(R.id.btnAdd).setOnClickListener {
            Log.i(TAG, "btnAdd clicked...")

            dao.insert(Book(
                id = null,
                title = "title1",
                price = 100.0
            ))
        }
    }
} // end class BooksFragment.