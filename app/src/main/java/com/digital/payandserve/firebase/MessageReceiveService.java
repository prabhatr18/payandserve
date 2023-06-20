package com.digital.payandserve.firebase;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;


import com.digital.payandserve.R;
import com.digital.payandserve.database.AppDatabase;
import com.digital.payandserve.database.dao.NotificationDAO;
import com.digital.payandserve.database.entity.NotificationTable;
import com.digital.payandserve.utill.Print;
import com.digital.payandserve.utill.SharedPrefs;
import com.digital.payandserve.views.SplashScreen;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;



@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class MessageReceiveService extends FirebaseMessagingService {
    private final String TAG = "MessageReceiveService";

    public MessageReceiveService() {
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Print.P("NEW TOKEN : " + s);
        SharedPrefs.setValue(getApplicationContext(), SharedPrefs.FIREBASE_TOKEN, s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {
            Log.d(TAG, "From: " + remoteMessage.getFrom());
            if (remoteMessage.getData().size() > 0) {
                Log.d(TAG, "Message data payload: " + remoteMessage.getData());
                Map<String, String> str = remoteMessage.getData();
                String title = str.get("title");
                String body = str.get("body");
                String type = str.get("type");
                Print.P("TITLE: " + title + " body: " + body + " type: " + type);
                try {
                    NotificationDAO dao = AppDatabase.getInstance(getApplicationContext()).getNotificationDao();
                    NotificationTable notificationTable = new NotificationTable(title, body, "false");
                    dao.insert(notificationTable);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (type != null && type.equalsIgnoreCase("alert")) {
                    NotificationDialog.message = body;
                    NotificationDialog.title = title;
                    Intent i = new Intent(this, NotificationDialog.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    this.startActivity(i);
                } else {
                    //sendNotification(remoteMessage.getNotification().getBody());
                    showNotification(title, body);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showNotification(String title, String body) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(getApplicationContext(), SplashScreen.class);

        PendingIntent pi = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //String sound = "android.resource://" + getPackageName() + "/" + R.raw.eventually;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Creating an Audio Attribute
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();

            int importance = NotificationManager.IMPORTANCE_HIGH;

            // Default channel
            NotificationChannel mChannel = new NotificationChannel(getString(R.string.default_notification_channel_id), "Notifications",
                    importance);
            // Configure the notification channel.
            mChannel.setDescription("Latest Updates");
            mChannel.enableLights(true);
            // Sets the notification light color for notifications posted to this
            // channel, if the device supports this feature.
            mChannel.setLightColor(Color.RED);
            //mChannel.enableVibration(true);
            //mChannel.setVibrationPattern(NOTIFICATION_VIBRATION_PATTERN);
            //mChannel.setSound(Uri.parse(sound), audioAttributes);
            notificationManager.createNotificationChannel(mChannel);
        }

        //Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder(this,
                getString(R.string.default_notification_channel_id))
                //     .setTicker(r.getString(R.string.app_name))
                //.setStyle(new NotificationCompat.BigTextStyle().bigText(data.get("message")))
                .setSmallIcon(R.drawable.ic_noti)
                //.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentTitle(title)
                .setContentText(body)
                .setContentIntent(pi)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_MAX)
                .setAutoCancel(true);

        //mNotificationBuilder.setSound(Uri.parse(sound));
        mNotificationBuilder.setChannelId(getString(R.string.default_notification_channel_id));
        notificationManager.notify(0, mNotificationBuilder.build());
    }
}
