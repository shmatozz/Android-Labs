package com.example.lab2

import android.app.Dialog
import android.content.Intent
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
        adapter.deleteList(list)
    }

    private fun isUnique(title: String) : Boolean {
        for (list in adapter.lists) {
            if (list.title == title) {
                return false
            }
        }
        return true
    }
}