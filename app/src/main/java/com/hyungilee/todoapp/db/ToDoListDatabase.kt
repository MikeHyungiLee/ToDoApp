/**
 * @file ToDoListDatabase.kt
 * @brief
 * AndroidのRoom DB クラス
 * To-Doリストのアイテム情報を管理するためのデータベースクラス
 * @author Lee Hyungi
 * @date 2020.02.12
 */
package com.hyungilee.todoapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hyungilee.todoapp.db.dao.ListDao
import com.hyungilee.todoapp.model.ToDoListItem

/**
 * @brief　To-Doリストに表示するデータを管理するRoomDatabaseクラス
 * @param
 * @par 概要
 * メイン画面のRecyclerViewにタスクの項目を出力する時に使うAdapterクラス
 * RecyclerViewのCustomizeレイアウト：R.layout.todo_list_item_layout
 * リストの項目としてはCheckbox、タスクの内容、登録した日付の情報が含む
 */
@Database(entities = [ToDoListItem::class], version = 1)
abstract class ToDoListDatabase: RoomDatabase() {
    abstract fun listDao(): ListDao

    companion object {
        private var toDoListDatabaseInstance: ToDoListDatabase? = null

        fun getInstance(context: Context): ToDoListDatabase? {
            if (toDoListDatabaseInstance == null) {
                synchronized(ToDoListDatabase::class) {
                    toDoListDatabaseInstance = Room.databaseBuilder(
                        context.applicationContext,
                        ToDoListDatabase::class.java, "ToDoListItem.db"
                    ).allowMainThreadQueries()
                     .build()
                }
            }
            return toDoListDatabaseInstance
        }
    }
}