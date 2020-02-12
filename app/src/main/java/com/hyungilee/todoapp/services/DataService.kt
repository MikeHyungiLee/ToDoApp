package com.hyungilee.todoapp.services

import com.hyungilee.todoapp.model.ToDoListItem
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object DataService {

    // 現在の日付情報を取得
    val dateFormat = SimpleDateFormat("dd/M/yyyy")
    val currentDate = dateFormat.format(Date())

    // Dummy data(To Do List)
    val listItems = listOf(
        ToDoListItem(0,"タスク1", currentDate, true),
        ToDoListItem(1,"タスク2" ,currentDate, false),
        ToDoListItem(2,"タスク3", currentDate, true),
        ToDoListItem(3,"タスク4", currentDate, false),
        ToDoListItem(4,"タスク5", currentDate, true)
    )

}