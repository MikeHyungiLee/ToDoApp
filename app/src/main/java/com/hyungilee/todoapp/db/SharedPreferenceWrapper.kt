/**
 * @file SharedPreferenceWrapper.kt
 * @brief SharedPreferenceを共通処理するクラス
 * @author Lee Hyungi
 * @date 2020.02.13
 */
package com.hyungilee.todoapp.db

import android.content.Context
import android.content.SharedPreferences
import com.hyungilee.todoapp.application.ContextUtil

/** SharedPreferenceを初期化する時に使うname変数 */
private const val FILE_NAME = "prefs"

/**
 * @brief SharedPreferenceをクラスとして共通化して
 * SharedPreferenceからデータ保存、取得、削除することを各メソットに分けて作成
 * @par context
 */
class SharedPreferenceWrapper {

    private val prefs: SharedPreferences =
        ContextUtil.getApplicationContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

    /**
     * @brief SharedPreferenceから保存されているデータを取得
     *
     * @param value key
     * @param default default value
     *
     * @par 概要 SharedPreferenceから取得するデータのKeyValue
     *
     * @par 処理 SharedPreferenceオブジェクトのgetIntメソットを使ってデータ取得
     *
     * @return　prefs.getInt(value,default)
     */
    fun getInt(value: String, default: Int): Int {

        return prefs.getInt(value, default)
    }

    /**
     * @brief SharedPreferenceから保存されているデータをセット
     *
     * @param key
     * @param value
     *
     * @par 概要 SharedPreferenceからセットするデータのKeyとValue
     *
     * @par 処理 SharedPreferenceオブジェクトのputIntメソットを使ってデータ保存
     *
     * @return　なし
     */
    fun setInt(key: String, value: Int) {

        prefs.edit().putInt(key, value).apply()
    }

    /**
     * @brief SharedPreferenceから保存されているデータを削除
     *
     * @param key
     *
     * @par 概要 SharedPreferenceから削除するデータのKeyValue
     *
     * @par 処理 SharedPreferenceオブジェクトのremoveメソットを
     * 使って保存されているデータ削除
     *
     * @return　なし
     */
    fun remove(key: String) {
        prefs.edit().remove(key).apply()
    }

}