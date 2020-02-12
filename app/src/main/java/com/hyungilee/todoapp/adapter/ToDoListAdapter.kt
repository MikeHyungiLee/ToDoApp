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

class ToDoListAdapter(private val context: Context, private val listItems: List<ToDoListItem>) : RecyclerView.Adapter<ToDoListAdapter.Holder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.todo_list_item_layout, parent,false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return listItems.count()
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bindListItem(listItems[position])
    }

    inner class Holder(itemView: View):RecyclerView.ViewHolder(itemView){
        val checkBox = itemView.findViewById<CheckBox>(R.id.checkBox)
        val contents = itemView.findViewById<TextView>(R.id.item_contents)
        val date = itemView.findViewById<TextView>(R.id.item_date)

        fun bindListItem(listItem: ToDoListItem){
            checkBox.isChecked = listItem.status
            contents.text = listItem.contents
            date.text = listItem.date
        }
    }
}