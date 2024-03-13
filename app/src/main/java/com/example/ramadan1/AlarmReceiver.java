package com.example.ramadan1;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Alarm alarm = new Alarm(context);
        alarm.fromIntent(intent);

        if (NotificationActivity.areNotificationsEnabled(context)) {
            // Notifications are enabled, show the alarm notification
            NotificationHandler.showAlarmNotification(context);
        } else {
            // Notifications are disabled, you may choose to handle it differently
            Log.i("AlarmReceiver", "Notifications are disabled");
        }
        // Start the Notification activity
        Intent newIntent = new Intent();
        newIntent.setClass(context, NotificationActivity.class);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(newIntent);
    }
}