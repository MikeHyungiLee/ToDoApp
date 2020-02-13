/**
 * @file SplashActivity.kt
 * @brief Splash画面
 * @author Lee Hyungi
 * @date 2020.02.13
 */
package com.hyungilee.todoapp.activities.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.hyungilee.todoapp.R
import com.hyungilee.todoapp.activities.MainActivity
import com.hyungilee.todoapp.application.ContextUtil
import com.hyungilee.todoapp.db.ToDoListDatabase
import kotlinx.android.synthetic.main.activity_splash.*

/**
 * @brief Splash画面のクラス
 *
 * @par 概要
 * アプリを起動する時に初期表示されるSplash画面のクラス
 * 画面上には真ん中でロゴが配置されています。
 */
class SplashActivity : AppCompatActivity() {

    // Splash画面を何秒ぐらい遅延表示する為のHandler
    private var mDelayHandler: Handler? = null

    //2秒遅延して画面表示
    private val splashDelay: Long = 3000

    private val mRunnable: Runnable = Runnable {
        // Splash画面を表示する時に初期化することを作成する部分
        val intent = Intent(this@SplashActivity, MainActivity::class.java)
        startActivity(intent)
    }

    private lateinit var todoListDatabase: ToDoListDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        // データベースのインスタンス
        todoListDatabase = ToDoListDatabase.getInstance(ContextUtil.getApplicationContext())!!
        val entireTaskNum = todoListDatabase.listDao().getEntireItemCount()
        val leftTaskNum = todoListDatabase.listDao().getLeftItemCount()

        caseTxt1.text = "$entireTaskNum 件の中で"
        caseTxt2.text = "$leftTaskNum 件です！"

        //Handlerを初期化する
        mDelayHandler = Handler()
        //Navigate with delay
        mDelayHandler!!.postDelayed(mRunnable, splashDelay)
    }

    public override fun onDestroy() {
        if (mDelayHandler != null) {
            mDelayHandler!!.removeCallbacks(mRunnable)
        }
        super.onDestroy()
    }

}



