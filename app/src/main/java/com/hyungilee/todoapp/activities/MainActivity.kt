/**
 * @file MainActivity.kt
 * @brief メインコンテンツ画面
 * @author Lee Hyungi
 * @date 2020.02.12
 */
package com.hyungilee.todoapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.facebook.stetho.Stetho
import com.hyungilee.todoapp.R
import com.hyungilee.todoapp.adapter.ToDoListAdapter
import com.hyungilee.todoapp.db.ToDoListDatabase
import com.hyungilee.todoapp.model.ToDoListItem
import com.hyungilee.todoapp.services.DataService
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.todo_recyclerview_layout.*
import kotlinx.android.synthetic.main.todo_recyclerview_layout.view.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var toDoListAdapter : ToDoListAdapter
    private lateinit var todoListDatabase: ToDoListDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Stetho.initialize(
            Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build());

        todoListDatabase = Room.databaseBuilder(applicationContext, ToDoListDatabase::class.java, "ToDoListItem.db")
                                                .allowMainThreadQueries()
                                                .build()

        // Databaseで保存されているTo-Doリストの項目を全部取得して画面に表示
        var itemList = todoListDatabase.listDao().getAllListItem()
        toDoListAdapter = ToDoListAdapter(this, itemList){todoListItem ->
            todoListDatabase.listDao().updateContentsStatus(true, todoListItem.contentId)
        }
        todoRecyclerview.adapter = toDoListAdapter
        val layoutManager = LinearLayoutManager(this)
        todoRecyclerview.layoutManager = layoutManager
        todoRecyclerview.setHasFixedSize(true)
    }

    fun listItemSaveClicked(view: View){
        val dateFormat = SimpleDateFormat("dd/M/yyyy")
        val currentDate = dateFormat.format(Date())
        val contentsTxt = contentEditTxt.text.toString()
        val listItem = ToDoListItem(0, contentsTxt, currentDate, false)
        todoListDatabase.listDao().saveListItem(listItem)
        var itemList = todoListDatabase.listDao().getAllListItem()
        toDoListAdapter = ToDoListAdapter(this, itemList){todoListItem ->
            todoListDatabase.listDao().updateContentsStatus(true, todoListItem.contentId)
        }
        todoRecyclerview.adapter = toDoListAdapter
        toDoListAdapter.notifyDataSetChanged()
    }

    fun showAllItemClicked(view: View){
        // Databaseで保存されているTo-Doリストの項目を全部取得して画面に表示
        var itemList = todoListDatabase.listDao().getAllListItem()
        toDoListAdapter = ToDoListAdapter(this, itemList){todoListItem ->
            todoListDatabase.listDao().updateContentsStatus(true, todoListItem.contentId)
        }
        todoRecyclerview.adapter = toDoListAdapter
        toDoListAdapter.notifyDataSetChanged()
    }

    fun showCompletedItemClicked(view: View){
        val completedListItem = todoListDatabase.listDao().getCompletedListItem()
        toDoListAdapter = ToDoListAdapter(this, completedListItem){todoListItem ->
            todoListDatabase.listDao().updateContentsStatus(true, todoListItem.contentId)
        }
        todoRecyclerview.adapter = toDoListAdapter
        toDoListAdapter.notifyDataSetChanged()
    }

    fun deleteCompletedItemClicked(view: View){
        todoListDatabase.listDao().deleteCompletedListItem()
    }
}
