package com.example.date0201_room.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.date0201_room.R
import com.example.date0201_room.data.Book

class BooksAdapter(
        private val onItemClickCallback: (Book) -> Unit
): ListAdapter<Book, BooksAdapter.ItemViewHolder>(BookDiffCallBack()) {
    //
    var data =  listOf<Book>()
        set(value) {
            field = value
            // notifyDataSetChanged()
            this.submitList(field)
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_book, parent, false)

        val holder = ItemViewHolder(view, onItemClickCallback)
        return holder
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        data?.get(position)?.let { holder.onBind(it) }
    }

    ///////////////////////////////////////////////////////////////// ViewHolder:
    class ItemViewHolder(
            itemView: View,
            onItemClickCallback: (Book) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        // item data
        var book: Book? = null
        // Views
        val txvTitle: TextView = itemView.findViewById(R.id.txvTitle__ListItemBook)
        val txvPrice: TextView = itemView.findViewById(R.id.txvPrice__ListItemBook)

        fun onBind(item: Book) {
            this.book = item
            txvTitle.text = ("${item.id} - ${item.title}")
            txvPrice.text = item.price.toString()
        }

        /// Click Event Handler
        // private val clickListener = View.OnClickListener { book?.let { onItemClickCallback.invoke(it) } }
        private val clickListener = object : View.OnClickListener {
            override fun onClick(view: View?) {
                book?.let { onItemClickCallback.invoke(it) }
            }
        }.also { itemView.setOnClickListener(it) }
    }
    ///////////////////////////////////////////////////////////////// ViewHolder end.
    ///////////////////////////////////////////////////////////////// DiffCallBack:
    class BookDiffCallBack : DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
            return (oldItem.id == newItem.id)
        }

        override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
            return (oldItem == newItem)
        }

    }
    ///////////////////////////////////////////////////////////////// DiffCallBack end.
} // end class BookAdapter.