package com.example.pritesh.mypushnotification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    String CHANNEL_ID = "my_channel_01";
    private static final String KEY_TEXT_REPLY = "key_text_reply";
    private static final String replyRequestCode = "key_text_reply";
    int notification_id = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();

        findViewById(R.id.btnClick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayNotification();
            }
        });

        findViewById(R.id.btnClick1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notification_id++;
                Log.d(TAG,"notification_id = "+notification_id);
                displayNotification2(notification_id);
            }
        });

    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void displayNotification() {

        //notification_id++;

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Intent snoozeIntent = new Intent(this, MainActivity.class);
        snoozeIntent.setAction("MyBroadCast");
        snoozeIntent.putExtra("snooz", "mark as read");
        PendingIntent snoozePendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, snoozeIntent, 0);

        Intent intentMessage = new Intent(this, MessageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntentMessage = PendingIntent.getActivity(this, 0, intentMessage, 0);

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
        Intent intent1 = new Intent(this, MyBroadcastReceiver.class);
        intent1.setAction("MyBroadCast");
        intent1.putExtra("values","notification is remove");
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(getApplicationContext(), 0, intent1, 0);


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("title")
                .setContentText("desc")
                .setStyle(new NotificationCompat.BigTextStyle()
                .bigText("Much longer text that cannot fit one line..."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setContentIntent(pendingIntentMessage)
                //.addAction(R.drawable.ic_launcher_foreground, getString(R.string.read),snoozePendingIntent)
                //.addAction(R.drawable.ic_launcher_foreground, getString(R.string.reply),snoozePendingIntent)
                //.addAction(action)
                .setDeleteIntent(pendingIntent1)
                //.setDeleteIntent(snoozePendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notification_id, mBuilder.build());

    }

    private void displayNotification2(int notification_id) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Intent intentMessage = new Intent(this, MessageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //PendingIntent pendingIntentMessage = PendingIntent.getActivity(this, 0, intentMessage, 0);

        Intent intent1 = new Intent(this, MyBroadcastReceiver.class);
        intent1.setAction("MyBroadCast");
        intent1.putExtra("values","notification is remove");
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(getApplicationContext(), 0, intent1, 0);

        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.custom_push);
        contentView.setImageViewResource(R.id.image, R.mipmap.ic_launcher);
        contentView.setTextViewText(R.id.title, "Message");
        contentView.setTextViewText(R.id.text, "new message from sujit");
        contentView.setOnClickPendingIntent(R.id.reply,PendingIntent.getActivity(this, 0, intentMessage, PendingIntent.FLAG_UPDATE_CURRENT));
        contentView.setOnClickPendingIntent(R.id.seen,PendingIntent.getBroadcast(this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT));

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this,CHANNEL_ID)
                //.setSmallIcon(R.drawable.ic_launcher_background)
                //.setContentTitle("Message")
                //.setContentText("new message from sujit")
                //.setStyle(new NotificationCompat.BigTextStyle()
                //.bigText("Much longer text that cannot fit one line..."))
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .setDeleteIntent(pendingIntent1)
                .setContent(contentView);

        Notification notification = mBuilder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notification_id, notification);

    }
}