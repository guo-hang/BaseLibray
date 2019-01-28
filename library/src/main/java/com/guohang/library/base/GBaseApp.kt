package com.guohang.library.base

import android.app.Application

class GBaseApp: Application() {
    companion object {
        lateinit var mInstance: GBaseApp
    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this
    }
}