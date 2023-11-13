package com.example.lab2

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lab2.databinding.ItemCardBinding

class ItemAdapter(private val listener: Listener): RecyclerView.Adapter<ItemAdapter.ItemHolder>() {

    val items = ArrayList<Item>()

    class ItemHolder(item: View): RecyclerView.ViewHolder(item) {
        private val binding = ItemCardBinding.bind(item)

        fun bind(item: Item, listener: Listener) = with(binding) {
            itemName.text = item.title

            changeItemStyle(item)

            deleteItemButton.setOnClickListener {
                listener.onDeleteItemClick(item)
            }

            checkItem.setOnClickListener {
                item.isBought = !item.isBought
                changeItemStyle(item)
            }
        }

        private fun changeItemStyle(item: Item) = with(binding) {
            if (item.isBought) {
                itemName.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                background.alpha = 0.4F
                checkItem.isChecked = true
            } else {
                itemName.paintFlags = itemName.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                background.alpha = 1F
                checkItem.isChecked = false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)

        return ItemHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(items[position], listener)
    }

    fun addItem(item: Item) {
        items.add(item)
        notifyDataSetChanged()
    }

    fun deleteItem(item: Item) {
        items.remove(item)
        notifyDataSetChanged()
    }

    interface Listener {
        fun onDeleteItemClick(item: Item)
    }
}