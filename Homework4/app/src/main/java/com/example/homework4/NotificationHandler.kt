package com.example.homework4

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import androidx.core.app.NotificationCompat
import kotlin.random.Random

class NotificationHandler(private val context: Context) {
    private val notificationManager = context.getSystemService(NotificationManager::class.java)
    private val notificationChannelID = "notification_channel_id"

    // Simple demo notification
    // TODO: delete showSimpleNotification()
    fun showSimpleNotification() {
        val notification = NotificationCompat.Builder(context, notificationChannelID)
            .setContentTitle("Simple Notification")
            .setContentText("Message or text with notification")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true) // dismiss notification when user clicks
            .build()  // finalizes the creation

        notificationManager.notify(Random.nextInt(), notification)
    }

    // Temperature Alert
    fun showTemperatureNotification(currentTemp: String) {

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, notificationChannelID)
            .setContentTitle("Temperature Logger")
            .setContentText("Temperature out of range: $currentTemp °C")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Temperature out of range: $currentTemp °C\nPlease check the environment.")
            )
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)   // open app when clicked
            .setAutoCancel(true) // dismiss notification when user clicks
            .build()  // finalizes the creation

        notificationManager.notify(Random.nextInt(), notification)
    }
}