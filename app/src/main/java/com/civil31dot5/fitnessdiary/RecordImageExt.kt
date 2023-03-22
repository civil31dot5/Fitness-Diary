package com.civil31dot5.fitnessdiary

import android.content.Context
import com.civil31dot5.fitnessdiary.data.RecordImageFileProcessor
import com.civil31dot5.fitnessdiary.domain.model.RecordImage
import java.io.File


fun RecordImage.extraFile(context: Context): File{
    return if (filePath.startsWith(RecordImageFileProcessor.imageFolderName)){
        File(context.filesDir, filePath)
    }else{
        File(filePath)
    }
}

