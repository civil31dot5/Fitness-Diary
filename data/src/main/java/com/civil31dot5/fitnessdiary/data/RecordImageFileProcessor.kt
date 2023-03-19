package com.civil31dot5.fitnessdiary.data

import android.content.Context
import android.text.TextUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class RecordImageFileProcessor @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val imageFolderName = "images"

    /**
     * Copy file 複製記錄圖片到app/files/images/
     *
     * @param fromPath 原始圖片路徑
     * @param namePrefix 檔名前綴
     * @return 新檔案路徑
     */
    fun copyFile(fromPath: String, namePrefix: String): Result<String> = kotlin.runCatching {

        val imageFolder = File(context.filesDir, imageFolderName)
        if (!imageFolder.exists()){
            imageFolder.mkdir()
        }

        val originFile = File(fromPath)


        val fileExtension = if (TextUtils.isEmpty(originFile.extension)) "jpg" else originFile.extension

        val newFileName = buildString {
            append(namePrefix)
            append("_")
            append(System.currentTimeMillis())
            append(".")
            append(fileExtension)
        }

        val newFile = File(imageFolder, newFileName)

        originFile.copyTo(newFile, overwrite = true)

        "$imageFolderName/$newFileName"
    }
}