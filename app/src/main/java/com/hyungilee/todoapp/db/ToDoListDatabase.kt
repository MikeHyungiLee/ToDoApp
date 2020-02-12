package com.hyungilee.todoapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hyungilee.todoapp.db.dao.ListDao
import com.hyungilee.todoapp.model.ToDoListItem

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
                    ).build()
                }
            }
            return toDoListDatabaseInstance
        }
    }
}