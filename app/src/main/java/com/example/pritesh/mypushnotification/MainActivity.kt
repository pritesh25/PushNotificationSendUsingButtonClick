package com.example.pritesh.mypushnotification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.RemoteViews
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.FileProvider
import java.io.File


class MainActivity : AppCompatActivity() {
    private var CHANNEL_ID = "my_channel_01"
    private var notification_id = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createNotificationChannel()
        findViewById<View>(R.id.btnClick).setOnClickListener { displayNotification() }
        findViewById<View>(R.id.btnClick1).setOnClickListener {
            notification_id++
            Log.d(TAG, "notification_id = $notification_id")
            displayNotification2(notification_id)
        }
        findViewById<Button>(R.id.buttonSampleNotification).setOnClickListener {
            samplePushNotification()
        }

    }

    private fun samplePushNotification() {

        /*
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Notifications Example")
                .setContentText("This is a test notification")

        //val notificationIntent = Intent(this, MainActivity::class.java)
        //val contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT)
        //builder.setContentIntent(contentIntent)

        // Add as notification
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(0, builder.build())
        */

        val notificationIntent = Intent(Intent.ACTION_VIEW)
        notificationIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //notificationIntent.data = Uri.fromFile(File("/storage/emulated/0/Download/KredUno Invoices/412.pdf"))
        notificationIntent.data = FileProvider.getUriForFile(this, "$packageName.provider", File("/storage/emulated/0/Download/KredUno Invoices/412.pdf"))
        val pending = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            notificationManager.createNotificationChannel(channel)
        }

        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Notifications Example")
                .setContentText("This is a test notification")
        builder.setContentIntent(pending)
        notificationManager.notify(0, builder.build())

    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = getString(R.string.channel_name)
            val description = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun displayNotification() {

        //notification_id++;
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val snoozeIntent = Intent(this, MainActivity::class.java)
        snoozeIntent.action = "MyBroadCast"
        snoozeIntent.putExtra("snooz", "mark as read")
        val snoozePendingIntent = PendingIntent.getBroadcast(applicationContext, 0, snoozeIntent, 0)
        val intentMessage = Intent(this, MessageActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntentMessage = PendingIntent.getActivity(this, 0, intentMessage, 0)

//        String replyLabel = getResources().getString(R.string.reply_label);
//        RemoteInput remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY)
//                .setLabel(replyLabel)
//                .build();

        //PendingIntent replyPendingIntent = PendingIntent.getBroadcast(getApplicationContext(),replyRequestCode,PendingIntent.FLAG_UPDATE_CURRENT);

//        NotificationCompat.Action action =
//                new NotificationCompat.Action.Builder(R.drawable.ic_launcher_foreground,
//                        getString(R.string.label), replyPendingIntent)
//                        .addRemoteInput(remoteInput)
//                        .build();

        //send broadcast receiver to detect removal of notification
        val intent1 = Intent(this, MyBroadcastReceiver::class.java)
        intent1.action = "MyBroadCast"
        intent1.putExtra("values", "notification is remove")
        val pendingIntent1 = PendingIntent.getBroadcast(applicationContext, 0, intent1, 0)
        val mBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("title")
                .setContentText("desc")
                .setStyle(NotificationCompat.BigTextStyle()
                        .bigText("Much longer text that cannot fit one line..."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setContentIntent(pendingIntentMessage) //.addAction(R.drawable.ic_launcher_foreground, getString(R.string.read),snoozePendingIntent)
                //.addAction(R.drawable.ic_launcher_foreground, getString(R.string.reply),snoozePendingIntent)
                //.addAction(action)
                .setDeleteIntent(pendingIntent1) //.setDeleteIntent(snoozePendingIntent)
                .setAutoCancel(true)
        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(notification_id, mBuilder.build())
    }

    private fun displayNotification2(notification_id: Int) {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val intentMessage = Intent(this, MessageActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        //PendingIntent pendingIntentMessage = PendingIntent.getActivity(this, 0, intentMessage, 0);
        val intent1 = Intent(this, MyBroadcastReceiver::class.java)
        intent1.action = "MyBroadCast"
        intent1.putExtra("values", "notification is remove")
        val pendingIntent1 = PendingIntent.getBroadcast(applicationContext, 0, intent1, 0)
        val contentView = RemoteViews(packageName, R.layout.custom_push)
        contentView.setImageViewResource(R.id.image, R.mipmap.ic_launcher)
        contentView.setTextViewText(R.id.title, "Message")
        contentView.setTextViewText(R.id.text, "new message from sujit")
        contentView.setOnClickPendingIntent(R.id.reply, PendingIntent.getActivity(this, 0, intentMessage, PendingIntent.FLAG_UPDATE_CURRENT))
        contentView.setOnClickPendingIntent(R.id.seen, PendingIntent.getBroadcast(this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT))
        val mBuilder = NotificationCompat.Builder(this, CHANNEL_ID) //.setSmallIcon(R.drawable.ic_launcher_background)
                //.setContentTitle("Message")
                //.setContentText("new message from sujit")
                //.setStyle(new NotificationCompat.BigTextStyle()
                //.bigText("Much longer text that cannot fit one line..."))
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .setDeleteIntent(pendingIntent1)
                .setContent(contentView)
        val notification = mBuilder.build()
        notification.flags = notification.flags or Notification.FLAG_AUTO_CANCEL
        notification.defaults = notification.defaults or Notification.DEFAULT_SOUND
        notification.defaults = notification.defaults or Notification.DEFAULT_VIBRATE
        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(notification_id, notification)
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private const val KEY_TEXT_REPLY = "key_text_reply"
        private const val replyRequestCode = "key_text_reply"
    }
}