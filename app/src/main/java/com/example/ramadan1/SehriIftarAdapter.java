package com.example.ramadan1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SehriIftarAdapter extends RecyclerView.Adapter<SehriIftarAdapter.ViewHolder> {

    private List<SehriIftarModel> dataList;


    public SehriIftarAdapter(List<SehriIftarModel> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sehri_iftari_time_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SehriIftarModel item = dataList.get(position);
        holder.id.setText(String.valueOf(item.getId()));
        holder.textViewSehriTime.setText(item.getSehriTime());
        holder.textViewIftarTime.setText(item.getIftarTime());
        holder.textViewDate.setText(item.getDate());
        holder.textViewDay.setText(item.getDay());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewSehriTime ,id;
        TextView textViewIftarTime;
        TextView textViewDate;
        TextView textViewDay;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            id =itemView.findViewById(R.id.idOfDays);
            textViewSehriTime = itemView.findViewById(R.id.sehriTime);
            textViewIftarTime = itemView.findViewById(R.id.iftariTime);
            textViewDate = itemView.findViewById(R.id.dateOfRamadan);
            textViewDay = itemView.findViewById(R.id.dayOfRamadan);
        }
    }
}
