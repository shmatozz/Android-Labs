package com.example.lab2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lab2.databinding.ListCardBinding

class ListAdapter(val listener: Listener): RecyclerView.Adapter<ListAdapter.ListHolder>() {

    val lists = ArrayList<List>()

    class ListHolder(item: View): RecyclerView.ViewHolder(item) {
        private val binding = ListCardBinding.bind(item)
        fun bind(list: List, listener: Listener) = with(binding) {
            listTitle.text = list.title
            listItems.text = listItems.text.toString().format(list.goods.size)

            itemView.setOnClickListener {
                listener.onClick(list)
            }

            deleteListButton.setOnClickListener {
                listener.onDeleteButtonClick(list)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_card, parent, false)

        return ListHolder(view)
    }

    override fun getItemCount(): Int {
        return lists.size
    }

    override fun onBindViewHolder(holder: ListHolder, position: Int) {
        holder.bind(lists[position], listener)
    }

    fun addList(list: List) {
        lists.add(list)
        notifyDataSetChanged()
    }

    fun deleteList() {
        if (lists.size > 0) {
            lists.removeLast()
            notifyDataSetChanged()
        }
    }

    fun deleteList(list: List) {
        lists.remove(list)
        notifyDataSetChanged()
    }

    interface Listener {
        fun onClick(list: List)
        fun onDeleteButtonClick(list: List)
    }
}