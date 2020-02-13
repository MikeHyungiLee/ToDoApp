package com.hyungilee.todoapp.application

import android.app.Application
import android.content.Context

class ToDoApplication : Application(){
    companion object{
        // Singleton Instance
        lateinit var instance: Application private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

    }
}

class ContextUtil{
    companion object {
        /**
         * ApplicationContext取得
         * @return ApplicationContext ApplicationクラスのSingletonインスタンス
         */
        fun getApplicationContext(): Context {
            return ToDoApplication.instance
        }
    }
}