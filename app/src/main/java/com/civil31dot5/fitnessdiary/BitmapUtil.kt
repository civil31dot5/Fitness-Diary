package com.civil31dot5.fitnessdiary

import android.graphics.*
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


object BitmapUtil {
    fun createBitmap(w: Int, h: Int, s: String): Bitmap {
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)

        val paint = TextPaint(Paint.ANTI_ALIAS_FLAG)
        paint.textSize = 50f
        paint.color = Color.BLACK
        paint.textAlign = Paint.Align.CENTER

        val bounds = Rect()
        paint.getTextBounds(s, 0, s.length, bounds)

        val x = canvas.width / 2f
        val y = canvas.height / 2f - bounds.exactCenterY()

        val textLayout = StaticLayout.Builder.obtain(s, 0, s.length, paint, w)
            .setAlignment(Layout.Alignment.ALIGN_CENTER)
            .setLineSpacing(0f, 1f)
            .setIncludePad(true)
            .build()

        textLayout.draw(canvas)

        return bitmap
    }

}

fun Bitmap.writeToJPGFile(file: File){
    try {
        val fos = FileOutputStream(file)
        compress(Bitmap.CompressFormat.JPEG, 90, fos)
        fos.flush()
        fos.close()
    }catch (e: Exception){
        e.printStackTrace()
    }
}