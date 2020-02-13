/**
 * @file ToDoListAdapter.kt
 * @brief To-Doリストのタスク項目を出力する時に使うAdapterクラス
 * @author Lee Hyungi
 * @date 2020.02.12
 */
package com.hyungilee.todoapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hyungilee.todoapp.R
import com.hyungilee.todoapp.model.ToDoListItem

/**
 * @brief　メイン画面であるRecyclerViewにタスクの項目を出力する時に使うAdapterクラス
 * @param context アプリのContext
 * @param listItems Recyclerviewに表示するタスクの項目リスト
 * @param itemClick Adapterを呼び出す部分からクリックイベントを処理するためのitemClickパラメーター
 * @par 概要
 * メイン画面のRecyclerViewにタスクの項目を出力する時に使うAdapterクラス
 * RecyclerViewのCustomizeレイアウト：R.layout.todo_list_item_layout
 * リストの項目としてはCheckbox、タスクの内容、登録した日付の情報が含む
 */
class ToDoListAdapter(private val context: Context, private val listItems: List<ToDoListItem>, val itemClick: (ToDoListItem) -> Unit) : RecyclerView.Adapter<ToDoListAdapter.Holder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.todo_list_item_layout, parent,false)
        return Holder(view, itemClick)
    }

    override fun getItemCount(): Int {
        return listItems.count()
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bindListItem(listItems[position])
    }

    inner class Holder(itemView: View, itemClick: (ToDoListItem) -> Unit):RecyclerView.ViewHolder(itemView){
        val checkBox = itemView.findViewById<CheckBox>(R.id.checkBox)
        val contents = itemView.findViewById<TextView>(R.id.item_contents)
        val date = itemView.findViewById<TextView>(R.id.item_date)

        fun bindListItem(listItem: ToDoListItem){
            checkBox.isChecked = listItem.status
            contents.text = listItem.contents
            date.text = listItem.date

            checkBox.setOnCheckedChangeListener { _, _ ->
                itemClick(listItem)
            }
        }
    }
}