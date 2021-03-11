package com.example.date0201_room.ui.local

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.date0201_room.R

class BooksAdapter() : ListAdapter<ItemModel, BooksAdapter.ItemViewHolder>(ItemDiffCallBack()) {

    // 顯示用的資料
    var dataset = listOf<ItemModel>()
        set(value) {
            field = value
            this.submitList(field)
//            notifyDataSetChanged()
        }


    // Item clicked call back
    var onItemClickCallback: (ItemModel) -> Unit = {}


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_book, parent, false)

        val holder = ItemViewHolder(view, onItemClickCallback)

        return holder
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        Log.i("Adapter", "onBindViewHolder $position")
        // bind data to holder
        dataset.get(position).let { item -> holder.onBind(item) }
    }


    ///////////////////////////////////////////////////////////////// ViewHolder:
    inner class ItemViewHolder(
        itemView: View,
        onItemClickCallback: (ItemModel) -> Unit = {}
    ) : RecyclerView.ViewHolder(itemView) {

        // item data
        private var itemData: ItemModel? = null

        // Views
        val layout: ConstraintLayout = itemView.findViewById(R.id.layout__ListItemBook)
        private val txvTitle: TextView = itemView.findViewById(R.id.txvTitle__ListItemBook)
        private val txvPrice: TextView = itemView.findViewById(R.id.txvPrice__ListItemBook)

        /** onBind */
        fun onBind(item: ItemModel) {
            this.itemData = item
            val book = item.book
            txvTitle.text = ("${book.id} - ${book.title}")
            txvPrice.text = book.price.toString()

            val color = if (item.isSelected) R.color.selected else R.color.white
            layout.setBackgroundColor(ContextCompat.getColor(itemView.context, color))
        }


        /// Click Event Handler
        // private val clickListener = View.OnClickListener { book?.let { onItemClickCallback.invoke(it) } }
        private val clickListener = object : View.OnClickListener {
            override fun onClick(view: View?) {
                // 執行 Callback.
                this@ItemViewHolder.itemData?.let { data ->
                    onItemClickCallback.invoke(data)
                }
            }
        }.also { itemView.setOnClickListener(it) }
    } // end class ItemViewHolder.

    ///////////////////////////////////////////////////////////////// ViewHolder end.
    ///////////////////////////////////////////////////////////////// DiffCallBack:
    class ItemDiffCallBack : DiffUtil.ItemCallback<ItemModel>() {
        override fun areItemsTheSame(oldItem: ItemModel, newItem: ItemModel): Boolean {
            val oldData = oldItem.book
            val newData = newItem.book
            return (oldData.id == newData.id)
        }

        override fun areContentsTheSame(oldItem: ItemModel, newItem: ItemModel): Boolean {
            val oldData = oldItem.book
            val newData = newItem.book
            return (oldData == newData)
        }

    }
    ///////////////////////////////////////////////////////////////// DiffCallBack end.
} // end class BookAdapter.