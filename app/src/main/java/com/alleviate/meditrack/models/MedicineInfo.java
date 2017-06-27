package com.alleviate.meditrack.models;

import com.alleviate.meditrack.constants.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Shirish Kadam on 23/6/17.
 * Logged in as felix.
 * www.shirishkadam.com
 */

public class MedicineInfo {

    public String medicine_name, medicine_freq, medicine_remind_date, medicine_remind_time, medicine_session;
    public double medicine_dose;
    public int purchased_quant, deductible_quant;

    public MedicineInfo(String medicine_name, double medicine_dose, String medicine_remind_time, String alarm_session){
        this.medicine_name = medicine_name;
        this.medicine_dose = medicine_dose;
        this.medicine_remind_time = medicine_remind_time;
        this.medicine_session = alarm_session;
    }

    public String getMedicine_name(){
        return medicine_name;
    }

    public Date getMedicine_remind_time() {

        SimpleDateFormat sdt_date = new SimpleDateFormat(Constants.time_std);
        Date med_time = null;

        try {
            med_time = sdt_date.parse(this.medicine_remind_time);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return med_time;}
}
