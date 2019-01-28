package com.guohang.library.base

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.annotation.Size
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hwangjr.rxbus.RxBus
import com.trello.rxlifecycle2.components.support.RxDialogFragment

abstract class GBaseDlg: RxDialogFragment() {
    protected lateinit var mContext : GBaseAct
    protected var mRootView : View? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as GBaseAct
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        mContext = activity as GBaseAct
    }

    override fun onStart() {
        super.onStart()
        setSize()?.apply {
            dialog?.window?.setLayout(this[0] , this[1])
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val layoutId = getLayoutId()
        if (layoutId == 0) return null

       return mRootView?.apply {
            (parent as? ViewGroup)?.removeView(this)
        }?: View.inflate(mContext , layoutId , null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        if (isRegister()) RxBus.get().register(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
    }

    //是否使用Rxbus
    open fun isRegister() = false

    //设置宽高
    /**
     * @return [宽度 ， 高度]
     */
    @Size(2)
    open fun setSize(): IntArray? = null

    abstract fun getLayoutId() : Int
    abstract fun initView()
    abstract fun initData()

    override fun show(manager: FragmentManager?, tag: String?) {
        try {
            manager?.beginTransaction()?.remove(this)?.commitAllowingStateLoss()
            super.show(manager, tag)
        } catch (e: Exception) {
        }
    }

    fun show(context: GBaseAct) {
        show(context.supportFragmentManager , javaClass.simpleName)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (isRegister()) RxBus.get().unregister(this)
    }
}