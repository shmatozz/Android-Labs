package com.example.lab2

import android.app.Dialog
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lab2.databinding.ActivityListBinding

class ListActivity : AppCompatActivity(), ItemAdapter.Listener {

    private lateinit var binding: ActivityListBinding
    private val adapter = ItemAdapter(this)
    private lateinit var listsDB: SQLiteDatabase
    private var listID = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.listOfItems.layoutManager = LinearLayoutManager(this@ListActivity)
        binding.listOfItems.adapter = adapter

        val intent = intent
        val listInfo = intent.getSerializableExtra("list") as List

        listsDB = openOrCreateDatabase("lists.db", MODE_PRIVATE, null)
        listID = getListID(listInfo)

        loadListInfo(listInfo)

        binding.addItem.setOnClickListener {
            createNewItem()
        }
    }

    private fun createNewItem() {
        val addDialog = Dialog(this)
        addDialog.setContentView(R.layout.dialog_add)
        addDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        addDialog.setCancelable(true)

        val createButton: Button = addDialog.findViewById(R.id.create_button)
        val newItemTitleInput: EditText = addDialog.findViewById(R.id.new_title)

        createButton.setOnClickListener {
            val newItemTitle = newItemTitleInput.text.toString()
            if (newItemTitle.isEmpty()) {
                newItemTitleInput.setHintTextColor(Color.argb(255, 255, 160, 160))
                newItemTitleInput.setHint(R.string.new_title_hint)
                newItemTitleInput.setText("")
            } else if (isUnique(newItemTitle)) {
                val newItem = Item(newItemTitle)
                adapter.addItem(newItem)
                addDialog.dismiss()
            } else {
                newItemTitleInput.setHintTextColor(Color.argb(255, 255, 160, 160))
                newItemTitleInput.setHint(R.string.exist_list)
                newItemTitleInput.setText("")
            }
        }

        addDialog.show()
    }

    private fun loadListInfo(list: List) {
        val actionBar = supportActionBar
        actionBar?.title = list.title

        if (list.goods.isNotEmpty()) {
            for (item in list.goods) {
                adapter.addItem(item)
            }
        }
    }

    private fun addItemToDB(item: Item) {
        val newItem = ContentValues()
        newItem.put("id", listID)
        newItem.put("title", item.title)
        when (item.isBought) {
            true -> newItem.put("bought", 1)
            else -> newItem.put("bought", 0)
        }
        listsDB.insert("items", null, newItem)
    }

    private fun getListID(list: List) : Int {
        val cursor = listsDB.rawQuery(
            "select id from lists where title = \"${list.title}\"", null)
        cursor.moveToFirst()
        val id = cursor.getInt(0)

        cursor.close()
        return id
    }

    override fun onDeleteItemClick(item: Item) {
        adapter.deleteItem(item)
    }

    private fun isUnique(title: String) : Boolean {
        for (list in adapter.items) {
            if (list.title == title) {
                return false
            }
        }
        return true
    }

    override fun onPause() {
        super.onPause()

        listsDB.delete("items", "id = $listID", null)

        for (item in adapter.items) {
            addItemToDB(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        listsDB.close()
    }
}