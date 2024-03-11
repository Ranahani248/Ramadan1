package com.example.ramadan1;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    Switch notification_off_on_Switch;
    RelativeLayout pre_time_alarm_layout, custom_time_alarm_layout;
    private final List<soundsModel> dataList1 = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private static final String VIBRATION_SWITCH_KEY = "vibration_switch_key";

    private Switch vibrationSwitch;
    private Vibrator vibrator;
    private String sehriTime;
    private String iftariTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        // Enable the Up button in the ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);

        }
        String fajrTime = getIntent().getStringExtra("FajrTime");
        String dhuhrTime = getIntent().getStringExtra("DhuhrTime");
        String asrTime = getIntent().getStringExtra("AsrTime");
        String maghribTime = getIntent().getStringExtra("MaghribTime");
        String ishaTime = getIntent().getStringExtra("IshaTime");

        // Retrieve data from Intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            sehriTime = extras.getString("sehriTime", "");
            iftariTime = extras.getString("iftariTime", "");
        }
        sharedPreferences = getPreferences(Context.MODE_PRIVATE);

        // Initialize UI elements
        pre_time_alarm_layout = findViewById(R.id.pre_time_alarm_layout);
        custom_time_alarm_layout = findViewById(R.id.custom_time_alarm_layout);
        TextView english_date1 = findViewById(R.id.english_date1);
        TextView islamic_date = findViewById(R.id.Islamic_date);
        vibrationSwitch = findViewById(R.id.vibration_off);
        notification_off_on_Switch = findViewById(R.id.notification_off_on_Switch);
        vibrationSwitch.setChecked(loadVibrationState());
        vibrationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            saveVibrationState(isChecked);
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(NotificationActivity.this).edit();
            editor.putBoolean("vibrate_pref", isChecked);
            editor.apply();
        });
        english_date1.setText(DateHelper.getCurrentDateEnglish());
        islamic_date.setText(DateHelper.getCurrentDateIslamic());

        RecyclerView soundRecyclerView = findViewById(R.id.sound_recyclerView);
        soundRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        populateDataList();
        notification_adaptor adapter = new notification_adaptor(dataList1);
        soundRecyclerView.setAdapter(adapter);

        pre_time_alarm_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sehriTime != null || iftariTime != null || fajrTime != null || dhuhrTime != null || asrTime != null || maghribTime != null || ishaTime != null) {
                    // At least one of them is not null, open the bottom fragment
                    Bundle bundle = new Bundle();
                    bundle.putString("sehriTime", sehriTime);
                    bundle.putString("iftariTime", iftariTime);
                    bundle.putString("fajrTime", fajrTime);
                    bundle.putString("dhuhrTime", dhuhrTime);
                    bundle.putString("asrTime", asrTime);
                    bundle.putString("maghribTime", maghribTime);
                    bundle.putString("ishaTime", ishaTime);

                    NotificationTimeFragment notificationTimeFragment = new NotificationTimeFragment();
                    notificationTimeFragment.setArguments(bundle);

                    // Use the getSupportFragmentManager() to show a bottom fragment
                    notificationTimeFragment.show(getSupportFragmentManager(), notificationTimeFragment.getTag());
                } else {
                    // All times are null, handle accordingly
                    // You can show a message or perform another action
                    // For example, show a Toast message
                    Toast.makeText(NotificationActivity.this, "All times are null", Toast.LENGTH_SHORT).show();
                }
            }
        });

        custom_time_alarm_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });
    }

    private void populateDataList() {
        dataList1.add(new soundsModel(0, "Alarm Sound 1"));
        dataList1.add(new soundsModel(1, "Alarm Sound 2"));
        dataList1.add(new soundsModel(2, "Alarm Sound 3"));
        dataList1.add(new soundsModel(3, "Alarm Sound 4"));
        dataList1.add(new soundsModel(4, "Alarm Sound 5"));
    }

    private void showTimePicker() {
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Save the selected time in SharedPreferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("alarmHour", hourOfDay);
                        editor.putInt("alarmMinute", minute);
                        editor.apply();

                        // Set the alarm
                        setAlarm(hourOfDay, minute);
                    }
                },
                hour,
                minute,
                true
        );
        timePickerDialog.show();
    }

    @SuppressLint({"WakelockTimeout", "ScheduleExactAlarm"})
    private void setAlarm(int hour, int minute) {
        Calendar alarmTime = Calendar.getInstance();
        alarmTime.set(Calendar.HOUR_OF_DAY, hour);
        alarmTime.set(Calendar.MINUTE, minute);
        alarmTime.set(Calendar.SECOND, 0);

        if (alarmTime.before(Calendar.getInstance())) {
            alarmTime.add(Calendar.DAY_OF_MONTH, 1);


        }

        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE);

        // Acquire a WakeLock to wake up the device
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK |
                        PowerManager.ACQUIRE_CAUSES_WAKEUP,
                "myapp:AlarmWakeLock"
        );
        wakeLock.acquire();
        try {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            // Use setExact to ensure precise timing, and setAlarmClock to show on the lock screen
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                AlarmManager.AlarmClockInfo alarmClockInfo =
                        new AlarmManager.AlarmClockInfo(alarmTime.getTimeInMillis(), pendingIntent);
                alarmManager.setAlarmClock(alarmClockInfo, pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), pendingIntent);
            }
        } finally {
            // Release the WakeLock after scheduling the alarm
            if (wakeLock.isHeld()) {
                wakeLock.release();
            }
        }
    }
    private void saveVibrationState(boolean isVibrationOn) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(VIBRATION_SWITCH_KEY, isVibrationOn);
        editor.apply();
    }

    private boolean loadVibrationState() {
        return sharedPreferences.getBoolean(VIBRATION_SWITCH_KEY, true); // Default to true if not found
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle the Up button click
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
