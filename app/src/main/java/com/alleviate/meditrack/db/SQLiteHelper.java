package com.alleviate.meditrack.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Shirish Kadam on 26/6/17.
 * Logged in as felix.
 * www.shirishkadam.com
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String db_database = "MediTrack";
    private static final int db_version = 1;

    public static final String db_med_db = "Medicine";
    public static final String db_med_id = "Med_Id";            // Foreign Key
    public static final String db_med_name = "Med_Name";
    public static final String db_med_dose = "Med_Dose";
    public static final String db_med_quant = "Med_Quantity";
    public static final String db_med_deduct = "Med_Deduct";

    public static final String db_alarms = "Alarms";
    public static final String db_alarms_id = "Alarms_Id";
    public static final String db_alarm_med_id = "Alarms_Med_Id";
    public static final String db_alarms_time = "Alarms_Time";
    public static final String db_alarms_date = "Alarms_Date";
    public static final String db_alarms_freq = "Alarms_Frequency";
    public static final String db_alarms_session = "Alarms_Session";
    public static final String db_alarms_status = "Alarms_Status";

    public static final String db_logs= "Med_Logs";
    public static final String db_logs_id = "Log_Id";
    public static final String db_logs_logs = "Log_Logs";


    public SQLiteHelper(Context context) {
        super(context, db_database, null, db_version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String create_db_meds = "CREATE TABLE "+db_med_db+" ( "+db_med_id+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+db_med_name+" VARCHAR NOT NULL, " +
                ""+db_med_dose+" DOUBLE NOT NULL, "+db_med_quant+" DOUBLE NOT NULL, "+db_med_deduct+" DOUBLE NOT NULL)";

        String create_db_alarms = "CREATE TABLE "+db_alarms+" ( "+db_alarms_id+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+db_alarm_med_id+" INTEGER NOT NULL, " +
                ""+db_alarms_time+" VARCHAR NOT NULL, "+db_alarms_date+" VARCHAR NOT NULL,"+ db_alarms_freq +" VARCHAR NOT NULL, "+db_alarms_session+" VARCHAR NOT NULL, "+db_alarms_status+" VARCHAR NOT NULL)";


        sqLiteDatabase.execSQL(create_db_meds);
        sqLiteDatabase.execSQL(create_db_alarms);


        String create_db_logs = "CREATE TABLE "+db_logs+" ( "+db_logs_id+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+db_logs_logs+" VARCHAR NOT NULL )";
        sqLiteDatabase.execSQL(create_db_logs);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static void insert_log(String log, Context context) {

        SQLiteHelper db = new SQLiteHelper(context);
        SQLiteDatabase dbw = db.getWritableDatabase();

        ContentValues log_values = new ContentValues();
        log_values.put(db_logs_logs,log);

        dbw.insert(db_logs, null, log_values);

        dbw.close();db.close();
    }
}
