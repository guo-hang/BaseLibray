package com.sequoia.course.teacher.dlg

import com.blankj.utilcode.util.SizeUtils
import com.guohang.library.R
import com.guohang.library.base.GBaseDlg
import kotlinx.android.synthetic.main.dlg_loading.*

class GLoadingDlg: GBaseDlg() {
    private var mTextResId = 0

    override fun getLayoutId() = R.layout.dlg_loading

    override fun initView() {
        tv.text = getString(mTextResId)
    }

    override fun initData() {
    }

    override fun setSize(): IntArray? = intArrayOf(SizeUtils.dp2px(150f) , SizeUtils.dp2px(120f))

    fun text(id: Int):GLoadingDlg {
        mTextResId = id
        return this
    }
}