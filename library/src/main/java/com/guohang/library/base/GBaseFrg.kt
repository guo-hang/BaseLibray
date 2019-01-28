package com.guohang.library.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blankj.utilcode.util.LogUtils
import com.gyf.barlibrary.ImmersionBar
import com.hwangjr.rxbus.RxBus
import com.trello.rxlifecycle2.components.support.RxFragment

/**
 * Created by guohang on 2018/6/19.
 */
abstract class GBaseFrg: RxFragment() {
    protected lateinit var mContext : GBaseAct
    protected var mRootView : View? = null
    protected val TAG: String = this.javaClass.simpleName

    private var mImmersionBar: ImmersionBar? = null
     private var mHasShow = false                //是否展示过

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as GBaseAct

        LogUtils.iTag(TAG)
    }

//    override fun onAttach(activity: Activity) {
//        super.onAttach(activity)
//        mContext = activity as BaseAct
//
//        LogUtils.i("")
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        (mRootView?.parent as? ViewGroup)?.removeView(mRootView)
        LogUtils.iTag(TAG)

        val layoutId = getLayoutId()
        if (layoutId == 0) return null

        if (null == mRootView) {
            mRootView = View.inflate(mContext , layoutId , null)
        } else {
            (mRootView?.parent as? ViewGroup)?.removeView(mRootView)
        }
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        if (isRegister()) RxBus.get().register(this)

        LogUtils.iTag(TAG)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()

        LogUtils.iTag(TAG)
    }


    abstract fun getLayoutId() : Int
    abstract fun initView()
    abstract fun initData()

    open fun isRegister() = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtils.iTag(TAG)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (isRegister()) RxBus.get().unregister(this)
        LogUtils.iTag(TAG)
    }

    override fun onDestroy() {
        super.onDestroy()

        mImmersionBar?.destroy()
        mImmersionBar = null
        LogUtils.iTag(TAG)
    }

    override fun onDetach() {
        super.onDetach()
        LogUtils.iTag(TAG)
    }

    /**
     * =====================================    fragment 可见  ======================================
     */

    //show hide 时调用
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        userVisibleHint = !hidden
    }

    //viewpager中调用 第一个fragment中会先于构造方法创建
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
    }
}