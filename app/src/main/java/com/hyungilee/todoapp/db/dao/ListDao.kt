/**
 * @file ListDao.kt
 * @brief
 * AndroidのRoom Dao Interface
 * To-Doリストにアイテム情報をAndroidの内部データベースに保存
 * @author Lee Hyungi
 * @date 2020.02.12
 */
package com.hyungilee.todoapp.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hyungilee.todoapp.model.ToDoListItem

/**
 * @brief　AndroidのRoom Dao Interface
 * @par 概要
 * データベースのデータを操作するために以下のメソットを含む
 * ①　saveListItem - タスクのリストにタスクの項目を追加する時に使うメソット
 * ②  deleteCompletedListItem - 完了されたタスクの項目を削除する時に使うメソット
 * ③  deleteListItem - 指定されたアイテムを削除する時に使うメソット
 * ④  getAllListItem - データベースに保存されている全項目のタスク情報を取得するメソット
 * ⑤  getCompletedListItem - 完了したタスクのリストを取得するメソット
 * ⑥  getLeftItemCount - 残っているタスクの数を数えるメソット
 * ⑦  updateContentsStatus -　登録されているタスクの状態をアップデートする時に使うメソット
 */
@Dao
interface ListDao {

    // タスクのリストにタスクの項目を追加する時に使うメソット
    @Insert
    fun saveListItem(vararg listItem: ToDoListItem)

    // 完了されたタスクの項目を削除する時に使うメソット
    @Query("DELETE FROM listItem_table WHERE status = 1")
    fun deleteCompletedListItem()

    // 指定されたアイテムを削除する時に使うメソット
    @Query("DELETE FROM listItem_table WHERE contentId = :contentId")
    fun deleteListItem(vararg contentId: String?)

    // データベースに保存されている全項目のタスク情報を取得するメソット
    @Query("SELECT * FROM listItem_table")
    fun getAllListItem():List<ToDoListItem>

    // 完了したタスクのリストを取得するメソット
    @Query("SELECT * FROM listItem_table WHERE status = 1")
    fun getCompletedListItem():List<ToDoListItem>

    // 残っているタスクの数を数えるメソット
    @Query("SELECT COUNT(contentId) FROM listItem_table WHERE status = 0")
    fun getLeftItemCount(): Int

    // 登録されているタスクの状態をアップデートする時に使うメソット
    @Query("UPDATE listItem_table SET status=:status WHERE contentId = :id")
    fun updateContentsStatus(status: Boolean, id: Int)

    // タスクの状態を確認する時に使うメソット
    @Query("SELECT status FROM listItem_table WHERE contentId = :contentId")
    fun checkTaskStatus(vararg contentId: Int): Boolean

}