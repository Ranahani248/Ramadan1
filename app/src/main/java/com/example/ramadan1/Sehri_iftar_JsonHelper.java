package com.example.ramadan1;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Sehri_iftar_JsonHelper {
        public static String loadJSONFromAsset(Context context, String filename) {
            String json;
            try {
                InputStream is = context.getAssets().open(filename);
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                json = new String(buffer, StandardCharsets.UTF_8);
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }
            return json;
        }

        public static List<SehriIftarModel> parseSehriIftarData(String jsonString) {
            List<SehriIftarModel> sehriIftarList = new ArrayList<>();
            try {
                JSONArray jsonArray = new JSONArray(jsonString);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int id = jsonObject.getInt("id");
                    String sehriTime = jsonObject.getString("Sehri");
                    String iftarTime = jsonObject.getString("Iftar");
                    String date = jsonObject.getString("date");
                    String day = jsonObject.getString("day");

                    SehriIftarModel model = new SehriIftarModel(id, sehriTime, iftarTime, date, day);
                    sehriIftarList.add(model);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return sehriIftarList;
        }
}
