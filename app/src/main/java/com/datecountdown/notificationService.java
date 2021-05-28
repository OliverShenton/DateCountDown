package com.datecountdown;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class notificationService extends Application {

    public static final String CHANNEL_1_ID = "channel1";
    public static final String CHANNEL_2_ID = "channel2";
    public static final String CHANNEL_3_ID = "channel3";
    public static final String CHANNEL_4_ID = "channel4";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID, "Date Ended Notification", NotificationManager.IMPORTANCE_HIGH
            );

            channel1.setDescription("Check what EVENT is starting now!");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel2 = new NotificationChannel(
                    CHANNEL_2_ID, "One Week Notification", NotificationManager.IMPORTANCE_HIGH
            );

            channel2.setDescription("One Week until an EVENT begins!");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel2);

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel3 = new NotificationChannel(
                    CHANNEL_3_ID, "One Day Notification", NotificationManager.IMPORTANCE_HIGH
            );

            channel3.setDescription("One Day until an Event begins!");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel3);

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel4 = new NotificationChannel(
                    CHANNEL_4_ID, "One Hour Notification", NotificationManager.IMPORTANCE_HIGH
            );

            channel4.setDescription("One Hour Until Event Begins!");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel4);

        }

    }


}