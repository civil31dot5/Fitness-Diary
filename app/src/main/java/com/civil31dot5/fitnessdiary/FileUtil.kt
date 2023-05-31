package com.civil31dot5.fitnessdiary

import android.content.Context
import java.io.File

object FileUtil {

    fun createTempJpgFile(context: Context): File {
        return File.createTempFile(System.currentTimeMillis().toString(), ".jpg", context.cacheDir)
    }
}