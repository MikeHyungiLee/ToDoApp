package com.hyungilee.todoapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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