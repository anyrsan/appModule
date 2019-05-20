package com.any.imagelibrary.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View

/**
 * BaseActivity基类
 * Create by: chenWei.li
 * Date: 2018/10/9
 * Time: 下午11:34
 * Email: lichenwei.me@foxmail.com
 */
abstract class BaseActivity : AppCompatActivity() {

    private var mView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (mView == null) {
            mView = View.inflate(this, bindLayout(), null)
        }
        setContentView(mView)

        initConfig()
        initView()
        initListener()
        getData()
    }


    protected abstract fun bindLayout(): Int

    protected open fun initConfig() {}

    protected abstract fun initView()

    protected abstract fun initListener()

    protected abstract fun getData()


}
