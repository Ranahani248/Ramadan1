package com.example.ramadan1;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.VibrationEffect;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
// Import statements...

public class HomeFragment2 extends Fragment {

    TextView textView, sehriTime1, iftariTime1, sehriTime2, iftariTime2;
    LinearLayout sehrialarmLayout, iftarialarmLayout;
    SehriIftarModel model;
    Spinner select_city1;
    boolean issehriAlarmSet;
    boolean isiftariAlarmSet ;
    ImageView sehriTime_alarm , iftariTime_alarm;

    public HomeFragment2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home2, container, false);

        // Initialize UI components
        sehrialarmLayout = view.findViewById(R.id.alarm_Layout);
        iftarialarmLayout = view.findViewById(R.id.iftar_alarm_Layout);
        textView = view.findViewById(R.id.textView12);
        sehriTime1 = view.findViewById(R.id.sehriTime1);
        iftariTime1 = view.findViewById(R.id.iftariTime1);
        sehriTime2 = view.findViewById(R.id.sehriTime2);
        iftariTime2 = view.findViewById(R.id.iftariTime2);
        select_city1 = view.findViewById(R.id.select_city1);
        sehriTime_alarm = view.findViewById(R.id.sehriTime_alarm);
        iftariTime_alarm = view.findViewById(R.id.iftariTime);
        ViewPager viewPager = view.findViewById(R.id.viewPager);
        issehriAlarmSet = loadAlarmState("Sehri_Alarm");
        if(issehriAlarmSet){
            sehriTime_alarm.setImageResource(R.drawable.baseline_notifications_active_24);
        }else{
            sehriTime_alarm.setImageResource(R.drawable.baseline_notifications_none_24);

        }
        isiftariAlarmSet = loadAlarmState("Iftari_Alarm");

        if (isiftariAlarmSet) {
            iftariTime_alarm.setImageResource(R.drawable.baseline_notifications_active_24);
        } else {
            iftariTime_alarm.setImageResource(R.drawable.baseline_notifications_none_24);
        }

        // Prepare your texts and images
        List<String> texts = Arrays.asList("يَا حَيُّ يَا قَيُّومُ بِرَحْمَتِكَ أَسْتَغيثُ", "Text 2", "Text 3");
        List<Integer> imageIds = Arrays.asList(R.drawable.qibla, R.drawable.qibla, R.drawable.qibla);

        CustomPagerAdapter adapter2 = new CustomPagerAdapter(getContext(), texts, imageIds);
        viewPager.setAdapter(adapter2);


        String[] cities = getResources().getStringArray(R.array.pakistan_cities);
        TextView english_date1 = view.findViewById(R.id.english_date1);
        TextView islamic_date = view.findViewById(R.id.Islamic_date);

        // Get today's date in the English calendar
        String todayDateEnglish = DateHelper.getCurrentDateEnglish();
        english_date1.setText(todayDateEnglish);

        // Get today's date in the Islamic calendar
        String todayDateIslamic = DateHelper.getCurrentDateIslamic();
        islamic_date.setText(todayDateIslamic);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, cities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        select_city1.setAdapter(adapter);
        iftariTime_alarm.setOnClickListener(v -> {
            if (!isiftariAlarmSet) {
                String iftariTime = iftariTime2.getText().toString();

                String[] iftariTimeparts = iftariTime.split(":");
                int IftariHour = Integer.parseInt(iftariTimeparts[0]);
                int iftariMinute = Integer.parseInt(iftariTimeparts[1].split("\\s+")[0]);
                String amPm = iftariTimeparts[1].split("\\s+")[1];
                // Adjust hours for PM
                if (amPm.equalsIgnoreCase("PM") && IftariHour != 12) {
                    IftariHour += 12;
                } else if (amPm.equalsIgnoreCase("AM") && IftariHour == 12) {
                    IftariHour = 0;
                }
                Calendar iftariCalendar = Calendar.getInstance();
                iftariCalendar.set(Calendar.HOUR_OF_DAY, IftariHour);
                iftariCalendar.set(Calendar.MINUTE, iftariMinute);
                iftariCalendar.set(Calendar.SECOND, 0);

                // Add logs for debugging
                Log.d("AlarmDebug", "Iftari Calendar Time: " + iftariCalendar.getTime());

                AlarmHelper.setupAlarmWithVibration(requireContext(), iftariCalendar, 7);
                saveAlarmState("Iftari_Alarm", true);
                iftariTime_alarm.setImageResource(R.drawable.baseline_notifications_active_24);
                isiftariAlarmSet = true;
            } else {
                AlarmHelper.cancelAlarm(requireContext(), 7);
                saveAlarmState("Iftari_Alarm", false);
               iftariTime_alarm.setImageResource(R.drawable.baseline_notifications_none_24);
                isiftariAlarmSet = false;
            }
        });
        sehriTime_alarm.setOnClickListener(v ->
        {

            if (!issehriAlarmSet) {
                String SehriTime = sehriTime2.getText().toString();

                String[] sehriTimeParts = SehriTime.split(":");
                int sehriHour = Integer.parseInt(sehriTimeParts[0]);
                int sehriMinute = Integer.parseInt(sehriTimeParts[1].split("\\s+")[0]);
                String amPm = sehriTimeParts[1].split("\\s+")[1];
                // Adjust hours for PM
                if (amPm.equalsIgnoreCase("PM") && sehriHour != 12) {
                    sehriHour += 12;
                } else if (amPm.equalsIgnoreCase("AM") && sehriHour == 12) {
                    sehriHour = 0;
                }
                Calendar sehriCalendar = Calendar.getInstance();
                sehriCalendar.set(Calendar.HOUR_OF_DAY, sehriHour);
                sehriCalendar.set(Calendar.MINUTE, sehriMinute);
                sehriCalendar.set(Calendar.SECOND, 0);

                // Add logs for debugging
                Log.d("AlarmDebug", "Sehri Calendar Time: " + sehriCalendar.getTime());

                AlarmHelper.setupAlarmWithVibration(requireContext(), sehriCalendar, 6);
                saveAlarmState("Sehri_Alarm", true);
                sehriTime_alarm.setImageResource(R.drawable.baseline_notifications_active_24);
                issehriAlarmSet = true;
            } else {
                AlarmHelper.cancelAlarm(requireContext(), 6);
                saveAlarmState("Sehri_Alarm", false);
                sehriTime_alarm.setImageResource(R.drawable.baseline_notifications_none_24);
                issehriAlarmSet = false;
            }
        });

        String jsonString = Sehri_iftar_JsonHelper.loadJSONFromAsset(requireContext(), "json.json");
        List<SehriIftarModel> sehriIftarList = Sehri_iftar_JsonHelper.parseSehriIftarData(jsonString);

        if (!sehriIftarList.isEmpty()) {
            SehriIftarModel todaySehriIftar = sehriIftarList.get(0);
            sehriTime1.setText(todaySehriIftar.getSehriTime());
            iftariTime1.setText(todaySehriIftar.getIftarTime());
            sehriTime2.setText(todaySehriIftar.getSehriTime());
            iftariTime2.setText(todaySehriIftar.getIftarTime());
        }

        // Fragment transitions
        sehrialarmLayout.setOnClickListener(v -> sendNotification(sehriTime1.getText().toString(), null));
        iftarialarmLayout.setOnClickListener(v -> sendNotification(null, iftariTime1.getText().toString()));


//        AlarmHelper.setupAlarmWithVibration(getActivity(), model.getIftarCalendar());

        return view;
    }
    private void sendNotification(String sehriTime, String iftariTime) {
        Intent notificationIntent = new Intent(requireContext(), NotificationActivity.class);
        notificationIntent.putExtra("sehriTime", sehriTime);
        notificationIntent.putExtra("iftariTime", iftariTime);
        startActivity(notificationIntent);
    }
    private void saveAlarmState(String key, boolean isAlarmOn) {
        SharedPreferences preferences = requireActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, isAlarmOn);
        editor.apply();
    }
    private boolean loadAlarmState(String key) {
        SharedPreferences preferences = requireActivity().getPreferences(Context.MODE_PRIVATE);
        return preferences.getBoolean(key, false);
    }
}
