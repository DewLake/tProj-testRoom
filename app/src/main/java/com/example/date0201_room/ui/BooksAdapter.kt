package com.example.date0201_room.ui

import android.graphics.Color
import android.util.Log
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
        private val onItemClickCallback: ((Int) -> Unit)? = null
) : ListAdapter<Book, BooksAdapter.ItemViewHolder>(BookDiffCallBack()) {

    // 已點選的項目, position
    var previousIndex = RecyclerView.NO_POSITION        // previous selected/clicked index.

    //
    var data = listOf<Book>()
        set(value) {
            field = value
            // notifyDataSetChanged()
            previousIndex = RecyclerView.NO_POSITION
            this.submitList(field)
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_book, parent, false)

        val holder = ItemViewHolder(view, onItemClickCallback)

        return holder
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        Log.i("Adapter", "onBindViewHolder $position")
        // bind data to holder
        data.get(position).let { item -> holder.onBind(item) }


        //
        if (position != RecyclerView.NO_POSITION && previousIndex == position) {
            holder.layout.setBackgroundColor(Color.MAGENTA)
        } else {
            holder.layout.setBackgroundColor(Color.parseColor("#FFFFF0"))
        }
    }


    ///////////////////////////////////////////////////////////////// ViewHolder:
    inner class ItemViewHolder(
            itemView: View,
            onItemClickCallback: ((Int) -> Unit)? = null
    ) : RecyclerView.ViewHolder(itemView) {

        // item data
        private var book: Book? = null

        // Views
        val layout: ConstraintLayout = itemView.findViewById(R.id.layout__ListItemBook)
        private val txvTitle: TextView = itemView.findViewById(R.id.txvTitle__ListItemBook)
        private val txvPrice: TextView = itemView.findViewById(R.id.txvPrice__ListItemBook)

        /** onBind */
        fun onBind(item: Book) {
            this.book = item
            txvTitle.text = ("${item.id} - ${item.title}")
            txvPrice.text = item.price.toString()
        }


        /// Click Event Handler
        // private val clickListener = View.OnClickListener { book?.let { onItemClickCallback.invoke(it) } }
        private val clickListener = object : View.OnClickListener {
            override fun onClick(view: View?) {
                //
                val pos = this@ItemViewHolder.adapterPosition       // 取得 position

//                // 點擊(選)一項 item 時
//                if (mPreviousIndex == pos) { // 同一筆被點選
//                    mPreviousIndex = RecyclerView.NO_POSITION       // 設定成未選取
//                    notifyItemChanged(pos)
//
//                    // 將 null 作為參數(沒選擇書), 執行 Callback.
////                    onItemClickCallback.invoke(null)
//
//                } else { // 不同筆被點選時
//                    // item 被點擊(選)時, 改變背景色
//                    layout.setBackgroundColor(Color.MAGENTA)
//
//                    // reset old item (background color)
//                    if (mPreviousIndex != RecyclerView.NO_POSITION) {
//                        notifyItemChanged(mPreviousIndex)
//                    }
//
//                    // remember the new position as "previous index".
//                    mPreviousIndex = pos
//
////                    // 將 book 作為參數, 執行 Callback.
////                    book?.let { onItemClickCallback.invoke(it) }
//                }

                // 將 pos 作為參數, 執行 Callback.
                onItemClickCallback?.invoke(pos)
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