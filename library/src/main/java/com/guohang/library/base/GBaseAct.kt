package com.guohang.library.base

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import com.gyf.barlibrary.ImmersionBar
import com.hwangjr.rxbus.RxBus
import com.sequoia.course.teacher.dlg.GLoadingDlg
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

abstract class GBaseAct: RxAppCompatActivity(){
    private var mImmersionBar: ImmersionBar? = null
    private val mLoading by lazy { initLoading() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //是否全屏
        if (isFullScreen()) {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN , WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }

        //加载布局
        val layoutId = getLayoutId()
        if (layoutId != 0) setContentView(layoutId)
        initView()

        //RxBus事件传递
        if (isRegister()) RxBus.get().register(this)

        //初始化数据
        initData()
    }

    abstract fun getLayoutId(): Int
    abstract fun initView()
    abstract fun initData()

    open fun isFullScreen() = false
    open fun isRegister() = false


    //生命周期
    override fun onDestroy() {
        mImmersionBar?.destroy()
        mImmersionBar = null
        if (isRegister()) RxBus.get().unregister(this)

        super.onDestroy()
    }

    //公共操作方法
    fun initLoading() = GLoadingDlg()

    fun showLoading() {
        mLoading.show(this)
    }

    fun hideLoading() {
        mLoading.dismissAllowingStateLoss()
    }


    //设置沉浸式状态栏
    fun setStatusBar(statusBarColor: Int = 0 ,fitsWindow: Boolean = true , fuc: ImmersionBar.()->Unit = {}) {
        mImmersionBar = ImmersionBar.with(this).apply {
            fitsSystemWindows(fitsWindow)

            if (0 != statusBarColor) {
                //设置状态栏颜色
                statusBarColor(statusBarColor)
                //如果状态栏为白色，则字体为黑色
                if (ContextCompat.getColor(this@GBaseAct , statusBarColor) == Color.WHITE) {
                    statusBarDarkFont(true , 0.2f)
                }
            }

            //执行传入的操作
            fuc()

            //初始化
            init()
        }
    }

    //简化fragment操作
    inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
        val fragmentTransaction = beginTransaction()
        fragmentTransaction.func()
        fragmentTransaction.commitAllowingStateLoss()
    }

    //简化EditText输入监听
    inline fun EditText.addTextChangedListener(crossinline func: Editable.() -> Unit) {
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.func()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }


    //简化RxJava+Retrofit网络请求
    inline fun <T> Observable<T>.callback(loading: Boolean) {
        this.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .let {
                if (loading) {
                    it.doOnSubscribe { showLoading() }
                        .doOnTerminate { hideLoading() }
                } else {
                    it
                }
            }
    }

}