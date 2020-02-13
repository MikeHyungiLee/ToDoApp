package com.hyungilee.todoapp.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hyungilee.todoapp.model.ToDoListItem

@Dao
interface ListDao {

    @Insert
    fun saveListItem(vararg listItem: ToDoListItem)

    @Query("DELETE FROM listItem_table WHERE status = 'true'")
    fun deleteCompletedListItem()

    @Query("DELETE FROM listItem_table WHERE contentId = :contentId")
    fun deleteListItem(vararg contentId: String?)

    @Query("SELECT * FROM listItem_table")
    fun getAllListItem():List<ToDoListItem>

    @Query("SELECT * FROM listItem_table WHERE status = 'true'")
    fun getCompletedListItem():List<ToDoListItem>

    @Query("SELECT COUNT(contentId) FROM listItem_table WHERE status = 'false'")
    fun getLeftItemCount(): Int

    @Query("UPDATE listItem_table SET status=:status WHERE contentId = :id")
    fun updateContentsStatus(status: Boolean, id: Int)

}