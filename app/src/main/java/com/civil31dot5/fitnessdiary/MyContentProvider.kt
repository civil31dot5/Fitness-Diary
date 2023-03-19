package com.civil31dot5.fitnessdiary

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import androidx.core.content.FileProvider

class MyContentProvider : FileProvider(R.xml.file_path)