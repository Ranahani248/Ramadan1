package com.example.ramadan1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NotificationTimeFragment extends BottomSheetDialogFragment {
    private NumberPicker numberPicker;
    TextView time_TextView;
    private int accumulatedMinutes = 0; // Initialize this variable at the class level
int alarmCode = 7;
    private int initialTime; // Initially set time
    AppCompatButton confirm_alarm;
    ImageView plusButton,closeButton,minButton;
    String sehriTime,iftariTime , fajrTime, dhuhrTime, asrTime, maghribTime, ishaTime;
    public NotificationTimeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification_time, container, false);

        // Initialize UI components
        numberPicker = view.findViewById(R.id.editText);
        plusButton = view.findViewById(R.id.plus_button);
        minButton = view.findViewById(R.id.min_button);
        closeButton = view.findViewById(R.id.close);
        time_TextView = view.findViewById(R.id.time_TextView);
        confirm_alarm = view.findViewById(R.id.confirm_alarm);


        sehriTime = getArguments() != null ? getArguments().getString("sehriTime", "") : "";
        iftariTime = getArguments() != null ? getArguments().getString("iftariTime", "") : "";
        fajrTime = getArguments() != null ? getArguments().getString("fajrTime", "") : "";
        dhuhrTime = getArguments() != null ? getArguments().getString("dhuhrTime", "") : "";
        asrTime = getArguments() != null ? getArguments().getString("asrTime", "") : "";
        maghribTime = getArguments() != null ? getArguments().getString("maghribTime", "") : "";
        ishaTime = getArguments() != null ? getArguments().getString("ishaTime", "") : "";

        // Set up NumberPicker
        numberPicker.setMaxValue(120);
        numberPicker.setValue(0);
        numberPicker.setWrapSelectorWheel(false);

        numberPicker.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker picker, int scrollState) {
                if (scrollState == NumberPicker.OnScrollListener.SCROLL_STATE_IDLE) {
                    int value = picker.getValue();
                    // The scrolling has stopped
                    onNumberPickerScrollStopped(picker.getValue());
                }
            }
        });
        // Set click listeners for buttons
        plusButton.setOnClickListener(v -> incrementNumberPicker());
        minButton.setOnClickListener(v -> decrementNumberPicker());
        closeButton.setOnClickListener(v -> dismiss());

        if (!sehriTime.isEmpty()) {
            time_TextView.setText(sehriTime);
        } else if (!fajrTime.isEmpty()) {
            time_TextView.setText(fajrTime);
        } else if (!dhuhrTime.isEmpty()) {
            time_TextView.setText(dhuhrTime);
        } else if (!asrTime.isEmpty()) {
            time_TextView.setText(asrTime);
        } else if (!maghribTime.isEmpty()) {
            time_TextView.setText(maghribTime);
        } else if (!ishaTime.isEmpty()) {
            time_TextView.setText(ishaTime);
        } else if (!iftariTime.isEmpty()) {
            time_TextView.setText(iftariTime);
        } else {
            time_TextView.setText("No time available");
        }


        confirm_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String timeString = time_TextView.getText().toString();
                String[] timeParts = timeString.split(":");

                // Extract hours and minutes
                int hours = Integer.parseInt(timeParts[0]);
                int minutes = Integer.parseInt(timeParts[1].substring(0, 2));

                // Check for AM or PM
                if (timeString.contains("PM") && hours < 12) {
                    hours += 12;
                } else if (timeString.contains("AM") && hours == 12) {
                    hours = 0; // 12 AM should be 0 in 24-hour format
                }

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hours);
                calendar.set(Calendar.MINUTE, minutes);
                calendar.set(Calendar.SECOND, 0);

                AlarmHelper.setupAlarmWithVibration(getContext(), calendar, alarmCode);

                Toast.makeText(getContext(), "alam set at: "+time_TextView.getText().toString(), Toast.LENGTH_SHORT).show();

                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().remove(NotificationTimeFragment.this).commit();
                fragmentManager.popBackStack();
            }
        });
            return view;
    }

    private void incrementNumberPicker() {
        if (numberPicker.getValue() < numberPicker.getMaxValue()) {
            int minutesToAdd = 1;

            // Parse the current time to a Calendar object
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());

            try {
                Date date = sdf.parse(time_TextView.getText().toString());
                calendar.setTime(date);

                // Add minutes to the current time
                calendar.add(Calendar.MINUTE, minutesToAdd);

                // Format and set the new time on time_TextView
                String newTime = sdf.format(calendar.getTime());
                time_TextView.setText(newTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // Increase the value in NumberPicker
            numberPicker.setValue(numberPicker.getValue() + 1);
        }
    }


    // Method to handle NumberPicker scroll stop
    private void onNumberPickerScrollStopped(int finalValue) {
        // Add or subtract the current value from the accumulated minutes
        accumulatedMinutes = finalValue - initialTime;
        Toast.makeText(getContext(), ""+initialTime, Toast.LENGTH_SHORT).show();
        initialTime = finalValue;

        // Parse the current time to a Calendar object
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());

        try {
            Date date = sdf.parse(time_TextView.getText().toString());
            calendar.setTime(date);

            // Add accumulated minutes to the current time
            calendar.add(Calendar.MINUTE, accumulatedMinutes);

            // Format and set the new time on time_TextView
            String newTime = sdf.format(calendar.getTime());
            time_TextView.setText(newTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    private void decrementNumberPicker() {
        if (numberPicker.getValue() > numberPicker.getMinValue()) {
            int minutesToSubtract = 1; // Set the step size to 1 minute

            // Parse the current time to a Calendar object
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());

            try {
                Date date = sdf.parse(time_TextView.getText().toString());
                calendar.setTime(date);

                // Subtract the minutes from the current time
                calendar.add(Calendar.MINUTE, -minutesToSubtract);

                // Format and set the new time on time_TextView
                String newTime = sdf.format(calendar.getTime());
                time_TextView.setText(newTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            // Decrease the value in NumberPicker
            numberPicker.setValue(numberPicker.getValue() - 1);
        }
    }
}
