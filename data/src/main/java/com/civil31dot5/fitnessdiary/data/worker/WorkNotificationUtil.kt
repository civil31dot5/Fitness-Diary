package com.civil31dot5.fitnessdiary.data.worker

import android.app.Notification
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.civil31dot5.fitnessdiary.data.R

object WorkNotificationUtil {

    private const val ChannelId = "BackgroundWork"
    private const val ChannelName = "背景任務"

    fun createNotification(ctx: Context, title: String): Notification {

        val builder = NotificationCompat.Builder(ctx, ChannelId)
            .setContentTitle(title)
            .setSmallIcon(R.drawable.ic_notifications)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(ctx)
        }

        return builder.build()
    }

    private fun createNotificationChannel(ctx: Context) {
        val notificationManager = NotificationManagerCompat.from(ctx)

        val channel =
            NotificationChannelCompat.Builder(ChannelId, NotificationManagerCompat.IMPORTANCE_LOW)
                .setName(ChannelName)
                .build()

        notificationManager.createNotificationChannel(channel)
    }

}