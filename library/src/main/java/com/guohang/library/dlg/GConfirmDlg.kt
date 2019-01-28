package com.sequoia.course.teacher.dlg

import android.support.v4.app.FragmentManager
import android.view.View
import android.view.ViewGroup
import com.blankj.utilcode.util.SizeUtils
import com.guohang.library.R
import com.guohang.library.base.GBaseDlg
import kotlinx.android.synthetic.main.dlg_confirm.*


class GConfirmDlg: GBaseDlg() {
    private var mTitle: String = ""
    private var mContent: String = ""
    private var mConfirmText: String? = null
    private var mConfirmClick: ()->Unit = {}
    private var mCancelClick: ()->Unit = {}
    private var mCancelVisible: Boolean? = null

    override fun getLayoutId(): Int = R.layout.dlg_confirm

    override fun initView() {
        tv_cancel.setOnClickListener {
            dismissAllowingStateLoss()
            mCancelClick()
        }
        tv_confirm.setOnClickListener {
            dismissAllowingStateLoss()
            mConfirmClick()
        }
    }

    override fun initData() {
        tv_content.text = mContent
        tv_title.text = mTitle
        mConfirmText?.apply { tv_confirm.text = this }

        mCancelVisible?.apply {
            if (this) tv_cancel.visibility = View.VISIBLE else tv_cancel.visibility = View.GONE
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(SizeUtils.dp2px(280f) , ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    fun show(manager: FragmentManager?) {
        show(manager , "confirmDlg")
    }

    fun setTitle(title: String): GConfirmDlg {
        mTitle = title
        return this
    }

    fun setContent(content: String): GConfirmDlg {
        mContent = content
        return this
    }

    fun setConfirm(listener: () -> Unit): GConfirmDlg {
        return setConfirm(listener , null)
    }

    fun setConfirm(listener: () -> Unit , text: String? = null): GConfirmDlg {
        mConfirmClick = listener
        mConfirmText = text
        return this
    }

    fun setCancel(listener: () -> Unit): GConfirmDlg {
        mCancelClick = listener
        return this
    }

    fun setCancelVisible(visible: Boolean): GConfirmDlg {
        mCancelVisible = visible
        return this
    }
}