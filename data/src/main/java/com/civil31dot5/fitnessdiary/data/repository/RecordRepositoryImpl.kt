package com.civil31dot5.fitnessdiary.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import android.media.ThumbnailUtils
import android.os.Build
import android.os.CancellationSignal
import android.provider.MediaStore
import android.util.Size
import com.civil31dot5.fitnessdiary.data.*
import com.civil31dot5.fitnessdiary.data.database.RecordDao
import com.civil31dot5.fitnessdiary.domain.model.BodyShapeRecord
import com.civil31dot5.fitnessdiary.domain.model.DietRecord
import com.civil31dot5.fitnessdiary.domain.repository.RecordRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.roundToInt

class RecordRepositoryImpl @Inject constructor(
    private val recordDao: RecordDao,
    private val imageFileProcessor: RecordImageFileProcessor,
    @ApplicationContext private val context: Context
) : RecordRepository {

    override suspend fun addDietRecord(record: DietRecord): Result<Unit> = kotlin.runCatching {

        recordDao.insertDietRecord(record.toDietRecordEntity())

        record.images.forEach { image ->
            imageFileProcessor.copyFile(image.filePath, "diet")
                .fold(
                    onSuccess = { newFilePath ->
                        recordDao.insertRecordImage(
                            image.toRecordImageEntity(
                                record.id,
                                newFilePath
                            )
                        )
                    },
                    onFailure = { Timber.e(it) }
                )
        }
    }

    override fun getAllDietRecords(): Flow<List<DietRecord>> {
        return recordDao.getAllDietRecord().map {
            it.map { it.toDietRecord() }
        }
    }

    override fun getDietRecords(from: LocalDate, to: LocalDate): Flow<List<DietRecord>> {
        val fromDateTime = LocalDateTime.of(from.year, from.month, from.dayOfMonth, 0, 0)
        val toDateTime = LocalDateTime.of(to.year, to.month, to.dayOfMonth, 23, 59)
        return recordDao.searchDietRecord(fromDateTime, toDateTime)
            .map { it.map { it.toDietRecord() } }
    }

    override suspend fun deleteDietRecord(record: DietRecord) {

        recordDao.deleteDietRecord(record.toDietRecordEntity())

        record.images.forEach { image ->
            imageFileProcessor.deleteFile(image.filePath)
            recordDao.deleteRecordImage(image.toRecordImageEntity(record.id, ""))
        }
    }

    override fun getMonthDietRecord(yearMonth: YearMonth): Flow<List<DietRecord>> {
        val from = LocalDateTime.of(yearMonth.year, yearMonth.month, 1, 0, 0)
        val to =
            LocalDateTime.of(yearMonth.year, yearMonth.month, yearMonth.lengthOfMonth(), 23, 59)
        return recordDao.searchDietRecord(from, to).map { it.map { it.toDietRecord() } }
    }

    override suspend fun addBodyShapeRecord(record: BodyShapeRecord): Result<Unit> =
        kotlin.runCatching {

            recordDao.insertBodyShapeRecord(record.toBodyShapeRecordEntity())

            record.images.forEach { image ->
                imageFileProcessor.copyFile(image.filePath, "body_shape")
                    .fold(
                        onSuccess = { newFilePath ->
                            recordDao.insertRecordImage(
                                image.toRecordImageEntity(
                                    record.id,
                                    newFilePath
                                )
                            )
                        },
                        onFailure = { Timber.e(it) }
                    )
            }

        }

    override fun getAllBodyShapeRecords(): Flow<List<BodyShapeRecord>> {
        return recordDao.getAllBodyShapeRecord().map { it.map { it.toBodyShapeRecord() } }
    }

    override suspend fun deleteBodyShapeRecord(record: BodyShapeRecord) {
        recordDao.deleteBodyShapeRecord(record.toBodyShapeRecordEntity())

        record.images.forEach { image ->
            imageFileProcessor.deleteFile(image.filePath)
            recordDao.deleteRecordImage(image.toRecordImageEntity(record.id, ""))
        }
    }

    override fun getBodyShapeRecords(from: LocalDate, to: LocalDate): Flow<List<BodyShapeRecord>> {
        val fromDateTime = LocalDateTime.of(from.year, from.month, from.dayOfMonth, 0, 0)
        val toDateTime = LocalDateTime.of(to.year, to.month, to.dayOfMonth, 23, 59)
        return recordDao.getBodyShapeRecord(fromDateTime, toDateTime)
            .map { it.map { it.toBodyShapeRecord() } }
    }

    override suspend fun createWeekDietImage(from: LocalDate, to: LocalDate): File = withContext(Dispatchers.IO){
        val records = getDietRecords(from, to).first()
        return@withContext createWeekDietImage(records)
    }

    private fun createWeekDietImage(dietRecords: List<DietRecord>): File {

        val imageSize = 200
        val borderLineWidth = 2
        val titleHeight = 50

        val groupedRecords = dietRecords.groupBy { it.dateTime.toLocalDate() }

        val imageWidth =
            groupedRecords.size * imageSize + (groupedRecords.size + 1) * borderLineWidth
        val imageHeight = (groupedRecords.maxOf { (_, recordsAtSameDay) ->
            recordsAtSameDay.sumOf {
                val option = BitmapFactory.Options().apply {
                    inJustDecodeBounds = true
                }
                it.images.sumOf {
                    BitmapFactory.decodeFile(
                        File(context.filesDir, it.filePath).path,
                        option
                    )

                    val scale = imageSize.toFloat() / option.outWidth
                    (option.outHeight * scale).toDouble()
                }
            }
        } + 2 * borderLineWidth + titleHeight + borderLineWidth).roundToInt()

        val resultBitmap = Bitmap.createBitmap(imageWidth, imageHeight, Bitmap.Config.ARGB_8888)

        val paint = Paint().apply {
            isAntiAlias = true
            isFilterBitmap = true
            isDither = true
        }
        val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            strokeWidth = borderLineWidth.toFloat()
        }
        val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = 12f
            color = Color.BLACK
            textAlign = Paint.Align.CENTER
            typeface = Typeface.DEFAULT_BOLD
        }
        val canvas = Canvas(resultBitmap)
        canvas.drawColor(Color.WHITE)
        var xOffset = 0f

        //draw top border
        canvas.drawLine(
            0f,
            borderLineWidth / 2f,
            imageWidth.toFloat(),
            borderLineWidth / 2f,
            borderPaint
        )

        //draw title
        groupedRecords.keys.forEach {
            xOffset += borderLineWidth + imageSize/2f

            canvas.drawText(
                it.format(DateTimeFormatter.ISO_LOCAL_DATE),
                xOffset,
                borderLineWidth + titleHeight/2f,
                textPaint
            )
            xOffset += imageSize/2f
        }

        //draw title border
        canvas.drawLine(
            0f,
            borderLineWidth + titleHeight + borderLineWidth / 2f,
            imageWidth.toFloat(),
            borderLineWidth + titleHeight + borderLineWidth / 2f,
            borderPaint
        )

        xOffset = 0f

        groupedRecords.toList().sortedBy { it.first }
            .forEachIndexed { index, (_, recordAtSameDay) ->
                //draw left border
                xOffset += borderLineWidth / 2f
                canvas.drawLine(xOffset, 0f, xOffset, imageHeight.toFloat(), borderPaint)
                xOffset += borderLineWidth / 2f

                var yOffset: Float = (borderLineWidth + titleHeight + borderLineWidth).toFloat()
                recordAtSameDay.sortedBy { it.dateTime }
                    .forEach { record ->
                        record.images.sortedBy { it.id }.forEach { image ->
                            val bitmap = BitmapFactory.decodeFile(
                                File(
                                    context.filesDir,
                                    image.filePath
                                ).path
                            )
                            val scale = imageSize / bitmap.width.toFloat()
                            val drawHeightSize = bitmap.height * scale

                            val outRect = RectF(
                                xOffset,
                                yOffset,
                                xOffset + imageSize,
                                yOffset + drawHeightSize
                            )
                            canvas.drawBitmap(bitmap, null, outRect, paint)
                            yOffset += drawHeightSize
                        }
                    }
                xOffset += imageSize
            }

        //draw right border
        canvas.drawLine(
            (imageWidth - borderLineWidth / 2f), 0f,
            (imageWidth - borderLineWidth / 2f), imageHeight.toFloat(), borderPaint
        )


        //draw bottom border
        canvas.drawLine(
            0f,
            (imageHeight - borderLineWidth / 2f),
            imageWidth.toFloat(),
            (imageHeight - borderLineWidth / 2f),
            borderPaint
        )

        return saveBitmapToFile(resultBitmap)
    }

    private fun saveBitmapToFile(bitmap: Bitmap): File {
        context.cacheDir.deleteRecursively()
        val file =
            File.createTempFile(System.currentTimeMillis().toString(), ".jpg", context.cacheDir)
        val out = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, out)
        out.flush()
        out.close()
        return file
    }
}