package com.example.lab2

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lab2.databinding.ItemCardBinding

class ItemAdapter(val listener: Listener): RecyclerView.Adapter<ItemAdapter.ItemHolder>() {

    val items = ArrayList<Item>()

    class ItemHolder(item: View): RecyclerView.ViewHolder(item) {
        private val binding = ItemCardBinding.bind(item)

        fun bind(item: Item, listener: Listener) = with(binding) {
            if (item.title.isNotEmpty()) {
                itemName.text = item.title

                deleteItemButton.setOnClickListener {
                    listener.onDeleteItemClick(item)
                }

                checkItem.setOnClickListener {
                    if (checkItem.isChecked) {
                        binding.itemName.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                        binding.background.alpha = 0.4F
                    } else {
                        binding.itemName.paintFlags = binding.itemName.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                        binding.background.alpha = 1F
                    }
                }
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

    fun deleteItem() {
        if (items.size > 0) {
            items.removeLast()
            notifyDataSetChanged()
        }
    }

    fun deleteItem(item: Item) {
        items.remove(item)
        notifyDataSetChanged()
    }

    interface Listener {
        fun onDeleteItemClick(item: Item)
    }
}