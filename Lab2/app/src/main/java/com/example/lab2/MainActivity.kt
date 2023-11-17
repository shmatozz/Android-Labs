package com.example.lab2

import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lab2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), ListAdapter.Listener {

    private lateinit var binding: ActivityMainBinding
    private val adapter = ListAdapter(this)
    private lateinit var listsDB: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.listOfLists.layoutManager = LinearLayoutManager(this@MainActivity)
        binding.listOfLists.adapter = adapter

        binding.addList.setOnClickListener {
            createNewList()
        }

        listsDB = openOrCreateDatabase("lists.db", MODE_PRIVATE, null)
    }

    private fun loadLists() {
        while (adapter.lists.size > 0) {
            adapter.deleteList()
        }

        listsDB.execSQL(
            "CREATE TABLE IF NOT EXISTS lists (id integer primary key AUTOINCREMENT, title TEXT(16) unique)" )

        listsDB.execSQL(
            "CREATE TABLE IF NOT EXISTS items (id integer, title text(16), bought integer default 0)" )

        val cursor = listsDB.rawQuery(
            "select lists.title as list_title, items.title as item_title, bought from lists LEFT JOIN items ON lists.id = items.id", null)

        var currentListTitle = ""
        var currentListItems: MutableList<Item> = mutableListOf()
        while (cursor.moveToNext()) {
            val receivedListTitle = cursor.getString(0)
            val receivedItemTitle = cursor.getString(1)
            val receivedItemStatus = cursor.getInt(2)
            if (receivedListTitle != currentListTitle) {
                if (currentListTitle != "") {
                    adapter.addList(List(currentListTitle, currentListItems.toTypedArray()))
                    currentListItems = mutableListOf()
                }
                currentListTitle = receivedListTitle
            }
            if (receivedItemTitle != null) {
                if (receivedItemStatus == 0) {
                    currentListItems.add(Item(receivedItemTitle, false))
                } else {
                    currentListItems.add(Item(receivedItemTitle, true))
                }
            }
        }

        if (currentListTitle != "") {
            adapter.addList(List(currentListTitle, currentListItems.toTypedArray()))
        }

        cursor.close()
    }


    private fun createNewList() {
        val addDialog = Dialog(this)
        addDialog.setContentView(R.layout.dialog_add)
        addDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        addDialog.setCancelable(true)

        val createButton: Button = addDialog.findViewById(R.id.create_button)
        val newListTitleInput: EditText = addDialog.findViewById(R.id.new_title)

        createButton.setOnClickListener {
            val newListTitle = newListTitleInput.text.toString()
            if (isUnique(newListTitle)) {
                val newList = List(newListTitle)
                adapter.addList(newList)
                addListToDB(newList)
                openList(newList)
                addDialog.dismiss()
            } else {
                newListTitleInput.setHintTextColor(Color.argb(255, 255, 160, 160))
                newListTitleInput.setHint(R.string.exist_list)
                newListTitleInput.setText("")
            }
        }

        addDialog.show()
    }

    private fun openList(newList: List) {
        onClick(newList)
    }

    private fun addListToDB(list: List) {
        val newList = ContentValues()
        newList.put("title", list.title)
        listsDB.insert("lists", null, newList)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete -> deleteAllLists()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun deleteAllLists() : Boolean {
        while (adapter.lists.size > 0) {
            deleteListFromDB(adapter.lists.last())
            adapter.deleteList()
        }

        return true
    }

    override fun onClick(list: List) {
        val intent = Intent(this, ListActivity::class.java).apply {
            putExtra("list", list)
        }
        startActivity(intent)
    }

    override fun onDeleteButtonClick(list: List) {
        deleteListFromDB(list)
        adapter.deleteList(list)
    }

    private fun deleteListFromDB(list: List) {
        val cursor = listsDB.rawQuery(
            "select id from lists where title = \"${list.title}\"", null)
        cursor.moveToFirst()
        val idToDelete = cursor.getInt(0)
        cursor.close()

        listsDB.delete("lists", "id = $idToDelete", null)
        listsDB.delete("items", "title = " +
                "$idToDelete", null)
    }

    private fun isUnique(title: String) : Boolean {
        for (list in adapter.lists) {
            if (list.title == title) {
                return false
            }
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()

        listsDB.close()
    }

    override fun onResume() {
        super.onResume()
        loadLists()
    }
}