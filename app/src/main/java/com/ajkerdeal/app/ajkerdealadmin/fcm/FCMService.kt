package com.ajkerdeal.app.ajkerdealadmin.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.ui.chat.ChatActivity
import com.ajkerdeal.app.ajkerdealadmin.ui.home.HomeActivity
import com.ajkerdeal.app.ajkerdealadmin.utils.SessionManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.GsonBuilder
import timber.log.Timber

class FCMService : FirebaseMessagingService() {

    private val gson = GsonBuilder().setPrettyPrinting().create()
    private var notificationId: Int = 2140086
    private var notifiType: String = ""

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

        Timber.d("FCMServiceTag ${p0.data}")
        Timber.d("FCMServiceTag ${p0.notification}")

        var title = ""
        var body = ""
        var imageUrl = ""
        var type = ""
        var bigText = ""

        p0.notification?.let {
            Timber.d("Message Notification Title: ${it.title}")
            Timber.d("Message Notification Body: ${it.body}")
            Timber.d("Message Notification Image: ${it.imageUrl}")

            title = it.title ?: ""
            body = it.body ?: ""
            imageUrl = it.imageUrl.toString()
        }

        if (p0.data.isNotEmpty()) {
            Timber.d("Message data payload: ${p0.data}")

            p0.data["title"]?.let {
                title = it
            }
            p0.data["body"]?.let {
                body = it
            }
            p0.data["imageUrl"]?.let {
                imageUrl = it
            }
            type = p0.data["notificationType"] ?: ""
            bigText = p0.data["bigText"] ?: ""
        }

        val jsonElement = gson.toJsonTree(p0.data)
        val fcmModel: FCMData = gson.fromJson(jsonElement, FCMData::class.java)
        notifiType = fcmModel.notificationType ?: ""
        notificationId = System.currentTimeMillis().toInt()

        if (Integer.parseInt(fcmModel.receiverId ?: "") == SessionManager.dtUserId || Integer.parseInt(fcmModel.receiverId ?: "") == 906) {//906 post shipment ad user id
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            createNotificationChannels(notificationManager)
            val builder = createNotification(
                getString(R.string.default_notification_channel_id),
                title, body, createPendingIntent(fcmModel)
            )

            when (type) {
                "0" -> { // Default
                    notificationManager.notify(notificationId, builder.build())
                }
                "1" -> { // Big text
                    builder.setStyle(NotificationCompat.BigTextStyle().bigText(bigText))
                    notificationManager.notify(notificationId, builder.build())
                }
                "2" -> { //Banner
                    Glide.with(applicationContext)
                        .asBitmap()
                        .load(imageUrl)
                        .into(object : CustomTarget<Bitmap?>() {

                            override fun onLoadCleared(placeholder: Drawable?) {
                                notificationManager.notify(notificationId, builder.build())
                            }

                            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
                                val notificationStyle = NotificationCompat.BigPictureStyle()
                                notificationStyle.bigPicture(resource)
                                notificationStyle.bigLargeIcon(null)
                                builder.setStyle(notificationStyle)
                                notificationManager.notify(notificationId, builder.build())
                            }
                        })
                }
                "3" -> { // BigTextWithSideImage
                    Glide.with(applicationContext)
                        .asBitmap()
                        .load(imageUrl)
                        .into(object : CustomTarget<Bitmap?>() {

                            override fun onLoadCleared(placeholder: Drawable?) {
                                notificationManager.notify(notificationId, builder.build())
                            }

                            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
                                builder.setLargeIcon(resource)
                                builder.setStyle(NotificationCompat.BigTextStyle().bigText(bigText))
                                notificationManager.notify(notificationId, builder.build())
                            }
                        })
                }
                "dt-retention", "dt-complain", "dt-bondhu" -> {

                    if (Integer.parseInt(fcmModel.receiverId ?: "") == SessionManager.dtUserId || Integer.parseInt(fcmModel.receiverId ?: "") == 906) {
                        val builder1 = createNotification(
                            getString(R.string.notification_channel_chat),
                            title, body, createChatPendingIntent(fcmModel)
                        )
                        notificationManager.notify(notificationId, builder1.build())
                    }
                }
                "Leave", "attendance" -> {
                    val builderColor = createNotificationColorFul(
                        getString(R.string.default_notification_channel_color),
                        title, body, createPendingIntent(fcmModel))
                    notificationManager.notify(notificationId, builderColor.build())
                }
                else -> {

                    // Notification message handle
                    Timber.d("Notification message handle called")
                    builder.setContentTitle(title).setContentText(body)
                    if (imageUrl.isNotEmpty()) {
                        Glide.with(applicationContext)
                            .asBitmap()
                            .load(imageUrl)
                            .apply(RequestOptions().timeout(8000))
                            .into(object : CustomTarget<Bitmap?>() {
                                override fun onLoadCleared(placeholder: Drawable?) {
                                    notificationManager.notify(notificationId, builder.build())
                                }

                                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
                                    builder.setLargeIcon(resource)
                                    val notificationStyle = NotificationCompat.BigPictureStyle()
                                    with(notificationStyle) {
                                        bigPicture(resource)
                                        bigLargeIcon(null)
                                    }
                                    builder.setStyle(notificationStyle)
                                    notificationManager.notify(notificationId, builder.build())
                                }
                            })
                    } else {
                        notificationManager.notify(notificationId, builder.build())
                    }
                }
            }

        }
    }

    private fun createPendingIntent(fcmModel: FCMData): PendingIntent {
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra("data", fcmModel)
        intent.putExtra("notificationType", fcmModel.notificationType)
        return PendingIntent.getActivity(this, System.currentTimeMillis().toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun createChatPendingIntent(fcmModel: FCMData): PendingIntent {
        val intent = Intent(this, ChatActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("data", fcmModel)
            putExtra("notificationType", fcmModel.notificationType)
        }
        return TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(System.currentTimeMillis().toInt(), PendingIntent.FLAG_UPDATE_CURRENT)!!
        }
    }

    private fun createNotification(
        channelId: String,
        title: String,
        body: String,
        pendingIntent: PendingIntent
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(this, channelId).apply {
            setSmallIcon(R.drawable.ic_notification_ad)
            setContentTitle(title)
            setContentText(body)
            setAutoCancel(true)
            color = ContextCompat.getColor(this@FCMService, R.color.colorPrimary)
            setDefaults(NotificationCompat.DEFAULT_ALL)
            priority = NotificationCompat.PRIORITY_MAX
            setContentIntent(pendingIntent)
        }
    }

    private fun createNotificationColorFul(channelId: String, title: String, body: String, pendingIntent: PendingIntent): NotificationCompat.Builder {
        val contentView = RemoteViews(packageName, R.layout.item_view_custom_notification_color)
        contentView.apply {
            setTextViewText(R.id.title, title)
            setTextViewText(R.id.body, body)
            setImageViewResource(R.id.image, R.drawable.ic_notification_ad)
        }

        return NotificationCompat.Builder(this, channelId).apply {
            setContent(contentView)
            setCustomBigContentView(contentView)
            setSmallIcon(R.drawable.ic_notification_ad)
            setContentIntent(pendingIntent)
            setAutoCancel(true)
        }
    }

    private fun createNotificationChannels(notificationManager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build()

            val channelList: MutableList<NotificationChannel> = mutableListOf()
            val channel1 = NotificationChannel(
                getString(R.string.default_notification_channel_id),
                "Promotion",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Delivery Tiger offers and promotions"
                setShowBadge(true)
                enableLights(true)
                lightColor = Color.GREEN
                enableVibration(true)
                setSound(soundUri, audioAttributes)
            }
            channelList.add(channel1)

            val channel2 = NotificationChannel(
                getString(R.string.notification_channel_chat),
                getString(R.string.notification_channel_chat_name),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Delivery Tiger merchant & retention manager chat"
                setShowBadge(true)
                enableLights(true)
                lightColor = Color.GREEN
                enableVibration(true)
                setSound(soundUri, audioAttributes)
            }
            channelList.add(channel2)

            val channel3 = NotificationChannel(
                getString(R.string.default_notification_channel_color),
                "AjkerDeal Admin",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                //description = "Delivery Tiger merchant & retention manager chat"
                //setShowBadge(true)
                enableLights(true)
                lightColor = Color.GREEN
                enableVibration(true)
                setSound(soundUri, audioAttributes)
            }
            channelList.add(channel3)

            notificationManager.createNotificationChannels(channelList)
        }
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        SessionManager.firebaseToken = p0
    }
}