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
import com.hyungilee.todoapp.application.ContextUtil
import com.hyungilee.todoapp.db.SharedPreferenceWrapper
import com.hyungilee.todoapp.db.ToDoListDatabase
import com.hyungilee.todoapp.model.ToDoListItem
import com.hyungilee.todoapp.utilities.RECYCLERVIEW_ID
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.todo_recyclerview_layout.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * @brief メイン画面(タスクを管理する画面)
 *
 * @par 概要
 * アプリのメイン画面。
 * 登録するタスクの内容を入力するeditTextボックス、登録したタスクのリストを表示するRecyclerView、RecyclerViewで表示されるデータを操作するためのボタンが含むレイアウトを持つ。
 */
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

        // データベースのインスタンス
        todoListDatabase = ToDoListDatabase.getInstance(ContextUtil.getApplicationContext())!!

        // Databaseで保存されているTo-Doリストの項目を全部取得して画面に表示
        var itemList = todoListDatabase.listDao().getAllListItem()
        toDoListAdapter = ToDoListAdapter(this, itemList){todoListItem ->
            // 選択したタスクの状態を確認(True or false)
            val taskStatus = todoListDatabase.listDao().checkTaskStatus(todoListItem.contentId)
            if(taskStatus){
                todoListDatabase.listDao().updateContentsStatus(false, todoListItem.contentId)
            }else{
                todoListDatabase.listDao().updateContentsStatus(true, todoListItem.contentId)
            }
        }
        todoRecyclerview.adapter = toDoListAdapter
        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        todoRecyclerview.layoutManager = layoutManager
        todoRecyclerview.setHasFixedSize(true)
    }

    fun listItemSaveClicked(view: View){
        val dateFormat = SimpleDateFormat("yyyy/MM/dd")
        val currentDate = dateFormat.format(Date())
        val contentsTxt = contentEditTxt.text.toString()
        val listItem = ToDoListItem(0, contentsTxt, currentDate, false)
        todoListDatabase.listDao().saveListItem(listItem)
        var itemList = todoListDatabase.listDao().getAllListItem()
        toDoListAdapter = ToDoListAdapter(this, itemList){todoListItem ->
            // 選択したタスクの状態を確認(True or false)
            val taskStatus = todoListDatabase.listDao().checkTaskStatus(todoListItem.contentId)
            if(taskStatus){
                todoListDatabase.listDao().updateContentsStatus(false, todoListItem.contentId)
            }else{
                todoListDatabase.listDao().updateContentsStatus(true, todoListItem.contentId)
            }
        }
        todoRecyclerview.adapter = toDoListAdapter
        toDoListAdapter.notifyDataSetChanged()
    }

    fun showAllItemClicked(view: View){
        // 画面に表示されているRecyclerViewを区分するためにSharedPreferenceに値を保存
        // 1: 全項目表示, 2: 完了項目表示
        SharedPreferenceWrapper().setInt(RECYCLERVIEW_ID, 1)
        // Databaseで保存されているTo-Doリストの項目を全部取得して画面に表示
        var itemList = todoListDatabase.listDao().getAllListItem()
        toDoListAdapter = ToDoListAdapter(this, itemList){todoListItem ->
            // 選択したタスクの状態を確認(True or false)
            val taskStatus = todoListDatabase.listDao().checkTaskStatus(todoListItem.contentId)
            if(taskStatus){
                todoListDatabase.listDao().updateContentsStatus(false, todoListItem.contentId)
            }else{
                todoListDatabase.listDao().updateContentsStatus(true, todoListItem.contentId)
            }
        }
        todoRecyclerview.adapter = toDoListAdapter
        toDoListAdapter.notifyDataSetChanged()
    }

    fun showCompletedItemClicked(view: View){
        // 画面に表示されているRecyclerViewを区分するためにSharedPreferenceに値を保存
        // 1: 全項目表示, 2: 完了項目表示
        SharedPreferenceWrapper().setInt(RECYCLERVIEW_ID, 2)
        val completedListItem = todoListDatabase.listDao().getCompletedListItem()
        toDoListAdapter = ToDoListAdapter(this, completedListItem){todoListItem ->
            // 選択したタスクの状態を確認(True or false)
            val taskStatus = todoListDatabase.listDao().checkTaskStatus(todoListItem.contentId)
            if(taskStatus){
                todoListDatabase.listDao().updateContentsStatus(false, todoListItem.contentId)
            }else{
                todoListDatabase.listDao().updateContentsStatus(true, todoListItem.contentId)
            }
        }
        todoRecyclerview.adapter = toDoListAdapter
        toDoListAdapter.notifyDataSetChanged()
    }

    fun deleteCompletedItemClicked(view: View){
        val checkRecyclerView = SharedPreferenceWrapper().getInt(RECYCLERVIEW_ID,0)
        todoListDatabase.listDao().deleteCompletedListItem()
        var itemList = listOf<ToDoListItem>()

        if(checkRecyclerView == 1) {
            itemList = todoListDatabase.listDao().getAllListItem()
        }else if(checkRecyclerView == 2){
            itemList = todoListDatabase.listDao().getCompletedListItem()
        }
        toDoListAdapter = ToDoListAdapter(this, itemList){todoListItem ->
            // 選択したタスクの状態を確認(True or false)
            val taskStatus = todoListDatabase.listDao().checkTaskStatus(todoListItem.contentId)
            if(taskStatus){
                todoListDatabase.listDao().updateContentsStatus(false, todoListItem.contentId)
            }else{
                todoListDatabase.listDao().updateContentsStatus(true, todoListItem.contentId)
            }
        }
        todoRecyclerview.adapter = toDoListAdapter
        toDoListAdapter.notifyDataSetChanged()
    }
}
