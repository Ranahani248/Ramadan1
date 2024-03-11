// NotificationHandler.java

package com.example.ramadan1;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationHandler {

    private static final String CHANNEL_ID = "YourChannelId";

    // Create notification channel (Call this method once)
    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Your Channel Name",
                    NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static void showAlarmNotification(Context context) {
        // Notification channel creation (Call this method once)
        createNotificationChannel(context);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(context.getApplicationContext(), MainActivity.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        PendingIntent activity = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("Anees Ramadan Alarm is on")
                .setContentText("Click Me")
                .setContentIntent(activity)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.baseline_notifications_active_24)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);


        Notification notificationPopup = notificationBuilder.build();
        notificationManager.notify((int) System.currentTimeMillis(), notificationPopup);
    }

    public static void showMissedAlarmNotification(Context context, Alarm alarm) {
        // Notification channel creation (Call this method once)
        createNotificationChannel(context);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(context.getApplicationContext(), MainActivity.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        PendingIntent activity = PendingIntent.getActivity(
                context,
                (int) alarm.getId(),
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext());
        Notification notification = builder
                .setSmallIcon(R.drawable.baseline_notifications_active_24)
                .setAutoCancel(true)
                .setContentTitle("Missed alarm: " + alarm.getTitle())
                .setContentText(getFormattedDate(alarm.getDate()))
                .setContentIntent(activity)
                .build();

        notificationManager.notify((int) alarm.getId(), notification);
    }

    private static String getFormattedDate(long date) {
        // Add your date formatting logic here
        return String.valueOf(date);
    }
}
