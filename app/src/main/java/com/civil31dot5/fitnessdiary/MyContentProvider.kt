package com.civil31dot5.fitnessdiary

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

class MyContentProvider : FileProvider(R.xml.file_path) {

    companion object {
        private const val authorities = "com.civil31dot5.fitnessdiary.fileprovider"

        fun getContentUri(context: Context, file: File): Uri {
            return getUriForFile(context, authorities, file)
        }
    }


}