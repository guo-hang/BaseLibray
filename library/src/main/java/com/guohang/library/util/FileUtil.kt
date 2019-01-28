package com.guohang.library.util

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.support.v4.content.FileProvider
import java.io.File

object FileUtil {
    /**
     *  路径为:/mnt/sdcard//Android/data/< package name >/cach/…
     *  或
     *  路径是:/data/data/< package name >/cach/…
     */

    fun getCacheFile(name: String , context: Context): File {
        var rootFile = when {
            Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED -> context.externalCacheDir
            else -> context.cacheDir
        }

        return File(rootFile , name)
    }

    /**
     * 路径为:/mnt/sdcard/Android/data/< package name >/files/…
     * 或
     * 路径是:/data/data/< package name >/files/…
     */
    fun getFilePath(dir: String , context: Context): String = when {
            Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED -> {
                val filesDir = context.getExternalFilesDir(dir)
                if (!filesDir.exists()) {
                    filesDir.mkdirs()
                }
                filesDir.absolutePath
            }
            else -> {
                val file = File(context.filesDir ,  dir)
                if (file.exists()) {
                    file.mkdirs()
                }
                file.absolutePath
            }
        }

    /**
     * 获取文件的uri
     */
    fun getFileUri(file: File , context:Context) = when {
        Build.VERSION.SDK_INT <= Build.VERSION_CODES.M -> Uri.fromFile(file)
        else -> FileProvider.getUriForFile(context , "${context.packageName}.provider" , file)
    }
}