package com.example.ramadan1;

import static android.service.controls.ControlsProviderService.TAG;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NotificationFragment extends Fragment {
    RelativeLayout pre_time_alarm_layout,custom_time_alarm_layout;
    private final List<soundsModel> dataList1 = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    String sehriTime,iftariTime;
    private Switch vibrationSwitch;
    private Vibrator vibrator;
    public NotificationFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        populateDataList();
        pre_time_alarm_layout = view.findViewById(R.id.pre_time_alarm_layout);
        custom_time_alarm_layout = view.findViewById(R.id.custom_time_alarm_layout);
        TextView english_date1 = view.findViewById(R.id.english_date1);
        TextView islamic_date = view.findViewById(R.id.Islamic_date);
        vibrationSwitch = view.findViewById(R.id.vibration_off);
        // Get today's date in the English calendar


        sehriTime = getArguments() != null ? getArguments().getString("sehriTime", "") : "";
        iftariTime = getArguments() != null ? getArguments().getString("iftariTime", "") : "";
        Log.d(TAG, "onCreateView: sehriTime: " + sehriTime);
        Log.d(TAG, "onCreateView: iftariTime : " + iftariTime);

        String todayDateEnglish = DateHelper.getCurrentDateEnglish();
        english_date1.setText(todayDateEnglish);
        // Get today's date in the Islamic calendar
        String todayDateIslamic = DateHelper.getCurrentDateIslamic();
        islamic_date.setText(todayDateIslamic);

        RecyclerView soundRecyclerView = view.findViewById(R.id.sound_recyclerView);
        soundRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        sehriTime = getArguments() != null ? getArguments().getString("sehriTime", "") : "";
        iftariTime = getArguments() != null ? getArguments().getString("iftariTime", "") : "";
        Log.d(TAG, "onCreateView: sehriTime: " + sehriTime);
        Log.d(TAG, "onCreateView: iftariTime : " + iftariTime);

        pre_time_alarm_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sehriTime.isEmpty() || !iftariTime.isEmpty()) {
                    // At least one of them is not empty, open the bottom fragment
                    Bundle bundle = new Bundle();
                    bundle.putString("sehriTime", sehriTime);
                    bundle.putString("iftariTime", iftariTime);

                    NotificationTimeFragment notificationTimeFragment = new NotificationTimeFragment();
                    notificationTimeFragment.setArguments(bundle);

                    // Use the BottomSheetDialogFragment to show a bottom fragment
                    notificationTimeFragment.show(getParentFragmentManager(), notificationTimeFragment.getTag());
                } else {
                    // Both are empty, handle accordingly
                    // You can show a message or perform another action
                    // For example, show a Toast message
                    Toast.makeText(getContext(), "Both times are empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
        custom_time_alarm_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });
        notification_adaptor adapter = new notification_adaptor(dataList1);
        soundRecyclerView.setAdapter(adapter);
        return view;
    }

    private void populateDataList() {
        dataList1.add(new soundsModel(0,"Alarm Sound 1"));
        dataList1.add(new soundsModel(1,"Alarm Sound 2"));
        dataList1.add(new soundsModel(2,"Alarm Sound 3"));
        dataList1.add(new soundsModel(3,"Alarm Sound 4"));
        dataList1.add(new soundsModel(4,"Alarm Sound 5"));}


    private void showTimePicker() {
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getContext(),
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
    @SuppressLint("ScheduleExactAlarm")
    private void setAlarm(int hour, int minute) {
        // Get the current time
        Calendar currentTime = Calendar.getInstance();
        long currentTimeMillis = currentTime.getTimeInMillis();

        // Set the alarm time
        Calendar alarmTime = Calendar.getInstance();
        alarmTime.set(Calendar.HOUR_OF_DAY, hour);
        alarmTime.set(Calendar.MINUTE, minute);
        alarmTime.set(Calendar.SECOND, 0);

        // Check if the alarm time is in the past, if so, add one day
        if (alarmTime.before(currentTime)) {
            alarmTime.add(Calendar.DAY_OF_MONTH, 1);
        }
        // Calculate the time difference
        long timeDifference = alarmTime.getTimeInMillis() - currentTimeMillis;

        // Create an Intent for the BroadcastReceiver that will handle the alarm
        Intent alarmIntent = new Intent(getContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE);

        // Get the AlarmManager service and set the alarm
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

//            alarmManager.setExact(AlarmManager.RTC_WAKEUP, currentTimeMillis + timeDifference, pendingIntent);
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, currentTimeMillis + timeDifference, pendingIntent);

    }
}