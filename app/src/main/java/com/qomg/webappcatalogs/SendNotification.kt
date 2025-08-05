package com.qomg.webappcatalogs

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat

fun sendNotification(context: Context, messageBody: String) {
    val intent = Intent(context, MainActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    val requestCode = 0
    val pendingIntent = PendingIntent.getActivity(
        context,
        requestCode,
        intent,
        PendingIntent.FLAG_IMMUTABLE,
    )

    val channelId = "fcm_default_channel"
    val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    val notificationBuilder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle("FCM Message")
        .setContentText(messageBody)
        .setAutoCancel(true)
        .setSound(defaultSoundUri)
        .setContentIntent(pendingIntent)

    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    // Since android Oreo notification channel is needed.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId,
            "Channel human readable title",
            NotificationManager.IMPORTANCE_DEFAULT,
        )
        notificationManager.createNotificationChannel(channel)
    }

    val notificationId = 0
    notificationManager.notify(notificationId, notificationBuilder.build())
}