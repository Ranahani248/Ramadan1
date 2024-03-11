package com.example.ramadan1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
public class AlarmReceiver extends BroadcastReceiver {
    final String TAG = "AlarmMe";
    private static final String VIBRATION_SWITCH_KEY = "vibration_switch_key";


    @Override
    public void onReceive(Context context, Intent intent) {

        Intent newIntent = new Intent(context, Notification.class);
        Alarm alarm = new Alarm(context);
        alarm.fromIntent(intent);
        alarm.toIntent(newIntent);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        Log.i(TAG, "AlarmReceiver.onReceive('" + alarm.getTitle() + "')");
        context.startActivity(newIntent);
        // Call the method to show the notification when the alarm time is complete
        NotificationHandler.showAlarmNotification(context);
    }
}
