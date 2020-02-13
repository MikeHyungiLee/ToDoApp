/**
 * @file MainActivity.kt
 * @brief メインコンテンツ画面
 * @author Lee Hyungi
 * @date 2020.02.12
 */
package com.hyungilee.todoapp.activities

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.stetho.Stetho
import com.hyungilee.todoapp.R
import com.hyungilee.todoapp.adapter.ToDoListAdapter
import com.hyungilee.todoapp.application.ContextUtil
import com.hyungilee.todoapp.db.SharedPreferenceWrapper
import com.hyungilee.todoapp.db.ToDoListDatabase
import com.hyungilee.todoapp.model.ToDoListItem
import com.hyungilee.todoapp.utilities.RECYCLERVIEW_ID
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.todo_footer_layout.*
import kotlinx.android.synthetic.main.todo_recyclerview_layout.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * @brief メイン画面(タスクを管理する画面)
 *
 * @par 概要
 * アプリのメイン画面。
 * 登録するタスクの内容を入力するeditTextボックス、登録したタスクのリストを表示するRecyclerView、RecyclerViewで表示されるデータを操作するためのボタンが含むレイアウトを持つ。
 */
class MainActivity : AppCompatActivity() {

    lateinit var toDoListAdapter : ToDoListAdapter
    private lateinit var todoListDatabase: ToDoListDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Stetho.initialize(
            Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build());

        // 初期画面に表示されているキーボードを閉じる
        hideKeyboard(this)
        // データベースのインスタンス
        todoListDatabase = ToDoListDatabase.getInstance(ContextUtil.getApplicationContext())!!
        // Databaseで保存されているTo-Doリストの項目を全部取得してRecyclerViewに表示
        var itemList = getAllListItem()
        // アップデートされたリストを使ってadapterを初期化する
        initToDoListAdapter(ContextUtil.getApplicationContext(), itemList)
        // (レイアウトのfooterに表示)残っているタスクの数を表示する部分を更新
        refreshBottomTaskNum()
    }

    /**
     * @brief データを内部データベースに保存するメソット
     * @par 概要
     * 入力したタスクの内容をデータベースに保存するメソット
     * @par 処理
     * ① 入力したタスクの内容、保存する時の日付情報を保存
     * ② タスク情報を保存した後で内部テーブルに更新されたリスト項目をアップデートする
     * ③ レイアウトの下に表示されている残っている項目数部分を更新
     * @param view
     * @return なし
     */
    fun listItemSaveClicked(view: View){
        val dateFormat = SimpleDateFormat("yyyy/MM/dd")
        val currentDate = dateFormat.format(Date())
        val contentsTxt = contentEditTxt.text.toString()
        if(contentsTxt != "") {
            val listItem = ToDoListItem(0, contentsTxt, currentDate, false)
            todoListDatabase.listDao().saveListItem(listItem)
            var itemList = getAllListItem()
            // アップデートされたリストを使ってadapterを初期化する
            initToDoListAdapter(ContextUtil.getApplicationContext(), itemList)
            // レイアウトのfooterに表示されている残っているタスクの数を表示する部分を更新
            refreshBottomTaskNum()
            hideKeyboard(this)
            contentEditTxt.setText("")
            contentEditTxt.clearFocus()
        }
    }

    /**
     * @brief 全項目表示ボタンのイベント処理メソット
     * @par 概要
     * 全項目表示ボタンを押すと画面上のRecyclerViewにテーブル内の全体データを表示
     * @par 処理
     * ① データを削除する時にRecyclerViewに表示するRecyclerViewを区分するためにSharedPreferenceを使ってRECYCLERVIEW_ID(id:1)を保存する
     * ② データベースのテーブル内で保存されている全項目を取得してadapterを初期化する
     * @param view
     * @return なし
     */
    fun showAllItemClicked(view: View){
        // 画面に表示されているRecyclerViewを区分するためにSharedPreferenceに値を保存
        // 1: 全項目表示, 2: 完了項目表示
        SharedPreferenceWrapper().setInt(RECYCLERVIEW_ID, 1)
        // Databaseで保存されているTo-Doリストの項目を全部取得して画面に表示
        var itemList = getAllListItem()
        // アップデートされたリストを使ってadapterを初期化する
        initToDoListAdapter(ContextUtil.getApplicationContext(), itemList)
    }

    /**
     * @brief 完了項目表示ボタンのイベント処理メソット
     * @par 概要
     * 完了項目表示ボタンを押すと画面上のRecyclerViewにテーブル内の完了項目のデータを表示
     * @par 処理
     * ① データを削除する時にRecyclerViewに表示するRecyclerViewを区分するためにSharedPreferenceを使ってRECYCLERVIEW_ID(id:2)を保存する
     * ② データベースのテーブル内で保存されている完了項目を取得してadapterを初期化する
     * @param view
     * @return なし
     */
    fun showCompletedItemClicked(view: View){
        // 画面に表示されているRecyclerViewを区分するためにSharedPreferenceに値を保存
        // 1: 全項目表示, 2: 完了項目表示
        SharedPreferenceWrapper().setInt(RECYCLERVIEW_ID, 2)
        val completedListItem = todoListDatabase.listDao().getCompletedListItem()
        // アップデートされたリストを使ってadapterを初期化する
        initToDoListAdapter(ContextUtil.getApplicationContext(), completedListItem)
    }

    /**
     * @brief 完了項目削除ボタンのイベント処理メソット
     * @par 概要
     * 完了項目削除ボタンを押すとRecyclerViewに表示されている完了項目を削除する
     * @par 処理
     * ① 完了項目削除ボタンを押す前にRecyclerViewに表示されているデータを区分(1: 全項目表示, 2: 完了項目表示)
     * ② 条件に合うitemList変数を初期化してadapterを初期化する
     * @param view
     * @return なし
     */
    fun deleteCompletedItemClicked(view: View){
        // 削除ボタンを押す前にRecyclerViewに表示されているアイテムを区分するためにSharedPreferenceを使って保存する
        val checkRecyclerView = SharedPreferenceWrapper().getInt(RECYCLERVIEW_ID,0)
        todoListDatabase.listDao().deleteCompletedListItem()
        var itemList = listOf<ToDoListItem>()
        // (1: 全項目表示)
        if(checkRecyclerView == 1) {
            itemList = getAllListItem()
        // (2: 完了項目表示)
        }else if(checkRecyclerView == 2){
            itemList = getCompletedListItem()
        }
        // アップデートされたリストを使ってadapterを初期化する
        initToDoListAdapter(ContextUtil.getApplicationContext(), itemList)
        // レイアウトのfooterに表示されている残っているタスクの数を表示する部分を更新
        refreshBottomTaskNum()
    }

    /**
     * @brief 完了項目削除ボタンのイベント処理メソット
     * @par 概要
     * 完了項目削除ボタンを押すとRecyclerViewに表示されている完了項目を削除する
     * @par 処理
     * ① 完了項目削除ボタンを押す前にRecyclerViewに表示されているデータを区分(1: 全項目表示, 2: 完了項目表示)
     * ② 条件に合うitemList変数を初期化してadapterを初期化する
     * @param view
     * @return なし
     */
    private fun initToDoListAdapter(context: Context, itemList: List<ToDoListItem>) {
        toDoListAdapter = ToDoListAdapter(this, itemList) { todoListItem ->
            // 選択したタスクの状態を確認(True or false)
            val taskStatus = getTaskStatus(todoListItem.contentId)
            if (taskStatus) {
                updateStatus(false, todoListItem)
            } else {
                updateStatus(true, todoListItem)
            }
        }
        todoRecyclerview.adapter = toDoListAdapter
        // LinearLayoutManagerを初期化
        val layoutManager = LinearLayoutManager(this)
        // reverseLayoutをtrueに設定する(最新のデータが上段に表示)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        todoRecyclerview.layoutManager = layoutManager
        todoRecyclerview.setHasFixedSize(true)
        toDoListAdapter.notifyDataSetChanged()
    }

    /**
     * @brief 項目のStatusを更新する
     * @par 概要
     * checkBoxをチェックする時に項目のStatusをアップデートするメソット
     * @par 処理
     * ① データベースに保存されている項目のStatusをアップデートする
     * @param status　アップデートするStatus情報
     * @param todoListItem アップデートするアイテム
     * @return なし
     */
    private fun updateStatus(status: Boolean, todoListItem: ToDoListItem) {
        todoListDatabase.listDao().updateContentsStatus(status, todoListItem.contentId)
        refreshBottomTaskNum()
    }

    /**
     * @brief タスクの全項目を取得して返却するメソット
     * @par 処理
     * ① データベース内で保存されているタスク情報の全項目を取得して返却
     * @return List<ToDoListItem>
     */
    private fun getAllListItem():List<ToDoListItem> = todoListDatabase.listDao().getAllListItem()

    /**
     * @brief 完了したタスク情報を取得して返却するメソット
     * @par 処理
     * ① データベース内で保存されている完了したタスク情報を取得して返却
     * @return 完了したタスク項目のリスト情報・List<ToDoListItem>
     */
    private fun getCompletedListItem():List<ToDoListItem> = todoListDatabase.listDao().getCompletedListItem()

    /**
     * @brief　タスク項目のstatus情報を返却
     * @par 処理
     * ① タスク項目のstatus情報を返却
     * @param contentId
     * @return 項目のStatus情報(Boolean)
     */
    private fun getTaskStatus(contentId: Int): Boolean = todoListDatabase.listDao().checkTaskStatus(contentId)

    /**
     * @brief レイアウトのfooterに表示する残っている項目数をアップデート
     * @par 処理
     * ① データベースから未完了の項目の数を取得する
     * ② 取得した数をテキストビューに設定する
     * @return なし
     */
    private fun refreshBottomTaskNum(){
        // 残っているタスクの数を取得する
        val taskNum = todoListDatabase.listDao().getLeftItemCount()
        // 残っているタスクの項目数を表示
        task_num.text = "残タスク数：$taskNum "
    }

    /**
     * @brief 画面上に表示されているキーボード表示を閉じるメソット
     * @param activity 現在活性化されているActivity
     * 処理：取得したサービス情報を使ってSoft Input AreaをClose/Hide(windowToken : システム内のウィンドウを特定するToken)
     */
    private fun hideKeyboard(activity: Activity){
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        if(view == null){
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
