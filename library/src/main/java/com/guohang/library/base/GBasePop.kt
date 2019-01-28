package com.guohang.library.base

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.widget.PopupWindow

abstract class GBasePop constructor(var mContext: Activity)  : PopupWindow(mContext) {

    init {
        contentView = View.inflate(mContext , getLayoutId() , null)
        initSetting()
        initView()
    }

    private fun initSetting() {
        isFocusable = true
        isOutsideTouchable = true
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    abstract fun getLayoutId(): Int
    abstract fun initView()

    /**
     * @param alpha 透明度(0.0~1.0) 0:完全透明 ；1：完全不透明
     */
    fun setWindowAlpha(alpha: Float) {
        mContext.window?.apply {
            attributes = attributes.apply { this.alpha = alpha }
        }
    }

    fun showUp(v: View , y: Int) {
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)

        var location = IntArray(2)
        v.getLocationOnScreen(location)
        showAtLocation(v , Gravity.NO_GRAVITY , location[0] - contentView.measuredWidth/2 + v.measuredWidth/2 , location[1] - contentView.measuredHeight -y)
    }
}