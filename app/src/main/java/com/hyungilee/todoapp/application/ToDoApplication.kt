/**
 * @file ToDoApplication.kt
 * @brief To-Do ListアプリのApplicationクラス
 * @author Lee Hyungi
 * @date 2020.02.13
 */
package com.hyungilee.todoapp.application

import android.app.Application
import android.content.Context

/**
 * @brief　Applicationクラス
 * @par 概要
 * Singleton Instanceを含むToDoApplicationクラス
 */
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

/**
 * Context操作ユーティリティ
 *
 * AndroidのContextのうち、ApplicationContext と ActivityContext の使い分けを行うためのクラス
 */
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
