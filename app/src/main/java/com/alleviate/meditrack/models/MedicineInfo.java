package com.alleviate.meditrack.models;

/**
 * Created by Shirish Kadam on 23/6/17.
 * Logged in as felix.
 * www.shirishkadam.com
 */

public class MedicineInfo {

    public String medicine_name, medicine_freq, medicine_remind_date, medicine_remind_time;
    public double medicine_dose;
    public int purchased_quant, deductible_quant;

    public MedicineInfo(String medicine_name, double medicine_dose, String medicine_remind_time){
        this.medicine_name = medicine_name;
        this.medicine_dose = medicine_dose;
        this.medicine_remind_time = medicine_remind_time;
    }

    public String getMedicine_name(){
        return medicine_name;
    }
}
