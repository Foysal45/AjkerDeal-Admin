package com.ajkerdeal.app.ajkerdealadmin.broadcast

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.ui.home.HomeActivity
import timber.log.Timber

class ReminderBroadcast : BroadcastReceiver() {

    //private val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US)

    override fun onReceive(context: Context?, intent: Intent?) {

        Timber.d("ReminderBroadcastDebug onReceive called")
        if (context == null || intent == null) return
        Timber.d("ReminderBroadcastDebug onReceive context,intent not null")

        val title = intent.getStringExtra("title") ?: ""
        val body = intent.getStringExtra("body") ?: ""


        // Create intent, put data
        val activityIntent = Intent(context, HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(context, System.currentTimeMillis().toInt(), activityIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationBuilder = NotificationCompat.Builder(context, context.getString(R.string.notification_channel_id_reminder))
            .setSmallIcon(R.drawable.ic_notification_ad)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setColor(ContextCompat.getColor(context, R.color.colorPrimaryVariant))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = context.getString(R.string.notification_channel_name_reminder)
            val description: String = context.getString(R.string.notification_channel_description_reminder)
            val channelID: String = context.getString(R.string.notification_channel_id_reminder)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build()
            val channel = NotificationChannel(channelID, name, importance)
            channel.description = description
            channel.setShowBadge(true)
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.setSound(soundUri, audioAttributes)

            notificationManager.createNotificationChannel(channel)
        }
        val notificationId = 21404

        notificationManager.notify(notificationId, notificationBuilder.build())

        /*Glide.with(context)
            .asBitmap()
            .load(coverPhoto)
            .listener(object : RequestListener<Bitmap?> {
                override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Bitmap?>, isFirstResource: Boolean): Boolean {
                    notificationManager.notify(notificationId, notificationBuilder.build())
                    return false
                }

                override fun onResourceReady(resource: Bitmap?, model: Any, target: Target<Bitmap?>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                    notificationBuilder.setLargeIcon(resource)
                    notificationManager.notify(notificationId, notificationBuilder.build())
                    return false
                }
            })
            .submit(256, 256)*/
    }
}