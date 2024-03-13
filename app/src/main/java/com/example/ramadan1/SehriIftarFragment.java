package com.example.ramadan1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SehriIftarFragment extends Fragment {
    Spinner cityList;
    TextView english_date1, Islamic_date;

    public SehriIftarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sehri_iftar, container, false);

        english_date1 = view.findViewById(R.id.english_date1);
        Islamic_date = view.findViewById(R.id.Islamic_date);
        // Get today's date in the English calendar
        String todayDateEnglish = DateHelper.getCurrentDateEnglish();
        english_date1.setText(todayDateEnglish);
        // Get today's date in the Islamic calendar
        String todayDateIslamic = DateHelper.getCurrentDateIslamic();
        Islamic_date.setText(todayDateIslamic);

        cityList = view.findViewById(R.id.citylist);
        String[] cities = getResources().getStringArray(R.array.pakistan_cities);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, cities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cityList.setAdapter(adapter);

        Sehri_iftar_JsonHelper jsonHelper = new Sehri_iftar_JsonHelper();

        MainActivity activity = (MainActivity) getActivity();
        assert activity != null;
        jsonHelper.updateData(getContext(), activity.currentLocation, new Sehri_iftar_JsonHelper.DataLoadListener() {
            @Override
            public void onDataLoaded(List<Sehri_iftari_class> sehriIftarList) {
                RecyclerView recyclerView = view.findViewById(R.id.recyclerView2);
                SehriIftarAdapter adapter1 = new SehriIftarAdapter(sehriIftarList);
                recyclerView.setAdapter(adapter1);
                recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            }
        });

        return view;
    }
}