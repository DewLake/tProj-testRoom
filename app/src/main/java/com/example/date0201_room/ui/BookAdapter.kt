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

class BookAdapter(val data: List<Book>): ListAdapter<Book, BookAdapter.ItemViewHolder>(BookDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_book, parent, false)

        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.onBind(data[position])
    }

    ///////////////////////////////////////////////////////////////// ViewHolder:
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txvTitle: TextView = itemView.findViewById(R.id.txvTitle__ListItemBook)
        val txvPrice: TextView = itemView.findViewById(R.id.txvPrice__ListItemBook)

        fun onBind(item: Book) {
            txvTitle.text = item.title
            txvPrice.text = item.price.toString()
        }
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