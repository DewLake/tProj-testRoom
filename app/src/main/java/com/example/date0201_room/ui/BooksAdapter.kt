package com.example.date0201_room.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.date0201_room.R
import com.example.date0201_room.data.Book

class BooksAdapter(
        private val onItemClickCallback: (Book) -> Unit
): ListAdapter<Book, BooksAdapter.ItemViewHolder>(BookDiffCallBack()) {
    //
    private var mPreviousIndex = RecyclerView.NO_POSITION        // previous selected/clicked index.
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
        // holder bind data
        data.get(position).let { holder.onBind(it) }

        holder.layout.setBackgroundColor(Color.CYAN)

        holder.layout.setOnClickListener {
            println("before click $position, $mPreviousIndex")
            it.setBackgroundColor(Color.MAGENTA)
            if(mPreviousIndex != RecyclerView.NO_POSITION) { notifyItemChanged(mPreviousIndex) }
            mPreviousIndex = position
            println("after $position, $mPreviousIndex")
//            notifyDataSetChanged()
        }

//        // coloring
//        if(mPreviousIndex == position) {
//            holder.layout.setBackgroundColor(Color.MAGENTA)
//        } else {
//            holder.layout.setBackgroundColor(Color.CYAN)
//        }



        // clear old item background color.
//        if (position == mPreviousIndex) {
//            holder.layout.setBackgroundColor(Color.CYAN)
//            notifyItemChanged(mPreviousIndex)
//        }

        // remember the position
        mPreviousIndex = position
    }


    ///////////////////////////////////////////////////////////////// ViewHolder:
    inner class ItemViewHolder(
            itemView: View,
            onItemClickCallback: (Book) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        // item data
        private var book: Book? = null

        // Views
        val layout: ConstraintLayout = itemView.findViewById(R.id.layout__ListItemBook)
        private val txvTitle: TextView = itemView.findViewById(R.id.txvTitle__ListItemBook)
        private val txvPrice: TextView = itemView.findViewById(R.id.txvPrice__ListItemBook)

        fun onBind(item: Book) {
            this.book = item
            txvTitle.text = ("${item.id} - ${item.title}")
            txvPrice.text = item.price.toString()
        }


        /// Click Event Handler
        // private val clickListener = View.OnClickListener { book?.let { onItemClickCallback.invoke(it) } }
        private val clickListener = object : View.OnClickListener {
            override fun onClick(view: View?) {
                // item 被點擊(選)時, 改變背景色
                layout.setBackgroundColor(Color.MAGENTA)

                // 將 book 作為參數, 執行 Callback.
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