/**
 * @file ToDoListItem.kt
 * @brief To-Doリストのタスク項目のData class
 * @author Lee Hyungi
 * @date 2020.02.12
 */
package com.hyungilee.todoapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @brief リストのタスク情報のEntityクラス (データクラス)
 * @par 概要
 * リストのタスク項目情報デーブルの各項目を初期化する
 */
@Entity(tableName = "listItem_table")
data class ToDoListItem(
    @PrimaryKey(autoGenerate = true)
    var contentId: Int = 0,

    @ColumnInfo(name = "contents")
    var contents: String = "",

    @ColumnInfo(name = "date")
    var date: String = "",

    @ColumnInfo(name = "status")
    var status: Boolean = false
)