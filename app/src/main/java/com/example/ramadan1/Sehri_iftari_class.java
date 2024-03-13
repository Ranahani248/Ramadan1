package com.example.ramadan1;

import java.util.ArrayList;
import java.util.List;

public class Sehri_iftari_class {

    public String date;
   static List<Sehri_iftari_class> data = new ArrayList<>();
    public String Sehri;
    public String Iftari;
    public  Sehri_iftari_class(String date, String Sehri, String Iftari, String Day, int id) {
        this.date = date;
        this.Sehri = Sehri;
        this.Iftari = Iftari;
        this.Day = Day;
        this.id = id;
    }
    public String getDate() {
        return date;
    }

    public String getSehri() {
        return Sehri;
    }

    public String getIftari() {
        return Iftari;
    }

    public String getDay() {
        return Day;
    }

    public int getId() {
        return id;
    }

    public String Day;
    public int id;

}
