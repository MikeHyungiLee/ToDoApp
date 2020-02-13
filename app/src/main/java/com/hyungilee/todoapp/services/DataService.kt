/**
 * @file DataService.kt
 * @brief To-Doリストに出力するSampleデータを含むObjectクラス
 * @author Lee Hyungi
 * @date 2020.02.12
 */
package com.hyungilee.todoapp.services

import com.hyungilee.todoapp.model.ToDoListItem
import java.text.SimpleDateFormat
import java.util.*

object DataService {

    // 現在の日付情報を取得
    private val dateFormat = SimpleDateFormat("yyyy/mm/dd")
    private val currentDate = dateFormat.format(Date())

    // Dummy data(To Do List)
    val listItems = listOf(
        ToDoListItem(0,"タスク1", currentDate, true),
        ToDoListItem(1,"タスク2" ,currentDate, false),
        ToDoListItem(2,"タスク3", currentDate, true),
        ToDoListItem(3,"タスク4", currentDate, false),
        ToDoListItem(4,"タスク5", currentDate, true)
    )

}