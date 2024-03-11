package com.example.ramadan1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NimazTimeFragment()).commit();


//        startService(new Intent(this, AlarmService.class));



        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                 if (itemId == R.id.menu_prayer) {

                     getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NimazTimeFragment()).commit();
                     return true;
                 } else if (itemId == R.id.menu_compass) {
                     getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new QiblaDirectionFragment()).commit();
                     return true;
                 } else if (itemId == R.id.menu_home) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment2()).commit();
                    return true;
                } else if (itemId == R.id.menu_Sehri_time) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SehriIftarFragment()).commit();
                    return true;
                } else if (itemId == R.id.menu_calendar) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingFragment()).commit();
                    return true;
                }

                return false;
            }
        });
    }
}