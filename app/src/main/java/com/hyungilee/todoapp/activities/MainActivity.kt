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
import com.facebook.stetho.Stetho
import com.hyungilee.todoapp.R
import com.hyungilee.todoapp.adapter.ToDoListAdapter
import com.hyungilee.todoapp.db.ToDoListDatabase
import com.hyungilee.todoapp.model.ToDoListItem
import com.hyungilee.todoapp.services.DataService
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.todo_recyclerview_layout.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var toDoListAdapter : ToDoListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toDoListAdapter = ToDoListAdapter(this, DataService.listItems)
        todoRecyclerview.adapter = toDoListAdapter
        val layoutManager = LinearLayoutManager(this)
        todoRecyclerview.layoutManager = layoutManager
        todoRecyclerview.setHasFixedSize(true)

        Stetho.initialize(
            Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build());

    }

    fun listItemSaveClicked(view: View){
        // 現在の日付情報を取得
        val dateFormat = SimpleDateFormat("dd/M/yyyy")
        val currentDate = dateFormat.format(Date())
        val contentsTxt = contentEditTxt.text.toString()
        val todoListDatabase: ToDoListDatabase = ToDoListDatabase.getInstance(applicationContext)!!

        Completable.fromAction {
            var listItem = ToDoListItem(1, contentsTxt, currentDate, false)
            todoListDatabase.listDao().saveListItem(listItem)
        }.subscribeOn(Schedulers.io()).subscribe()
    }

    fun showAllItemClicked(view: View){

    }

    fun showCompletedItemClicked(view: View){

    }

    fun deleteCompletedItemClicked(view: View){

    }

}
