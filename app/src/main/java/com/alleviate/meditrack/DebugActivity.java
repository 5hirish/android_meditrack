package com.alleviate.meditrack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.alleviate.meditrack.constants.Constants;
import com.alleviate.meditrack.db.SQLiteHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DebugActivity extends AppCompatActivity {

    TextView debug_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        debug_info = (TextView)findViewById(R.id.debug_info);

        show_primary_data();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_debug, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){

            supportFinishAfterTransition();

        } else if (id == R.id.action_medicines){

            show_primary_data();

        } else if (id == R.id.action_alarms){

            show_time_data();

        } else if (id == R.id.action_log){

            show_log_data();

        } else if (id == R.id.action_parent_alarm){

            execute_alarm_manager(getApplicationContext());

            show_time_data();

        }

        return true;
    }

    private void show_log_data() {
        SQLiteHelper db = new SQLiteHelper(getApplicationContext());
        SQLiteDatabase dbr =db.getReadableDatabase();

        debug_info.setText(SQLiteHelper.db_logs+"\n\n");

        Cursor cur = dbr.rawQuery("SELECT * FROM "+SQLiteHelper.db_logs+" ORDER BY "+SQLiteHelper.db_logs_id+" DESC", null);

        if(cur != null){
            if (cur.moveToFirst()){
                do{
                    debug_info.append(
                            cur.getInt(cur.getColumnIndex(SQLiteHelper.db_logs_id))+") "+
                                    cur.getString(cur.getColumnIndex(SQLiteHelper.db_logs_logs))+"\n"
                    );

                }while (cur.moveToNext());
            }cur.close();
        }dbr.close();db.close();
    }

    private void show_time_data() {
        SQLiteHelper db = new SQLiteHelper(getApplicationContext());
        SQLiteDatabase dbr =db.getReadableDatabase();

        debug_info.setText(SQLiteHelper.db_alarms+"\n\n");

        Cursor cur = dbr.rawQuery("SELECT * FROM "+SQLiteHelper.db_alarms, null);

        if(cur != null){
            if (cur.moveToFirst()){
                do{
                    debug_info.append(
                        cur.getInt(cur.getColumnIndex(SQLiteHelper.db_alarms_id))+") ["+
                            cur.getInt(cur.getColumnIndex(SQLiteHelper.db_alarm_med_id))+"] - "+
                            cur.getString(cur.getColumnIndex(SQLiteHelper.db_alarms_time))+" - "+
                            cur.getString(cur.getColumnIndex(SQLiteHelper.db_alarms_date))+" - "+
                            cur.getString(cur.getColumnIndex(SQLiteHelper.db_alarms_freq))+" - "+
                            cur.getString(cur.getColumnIndex(SQLiteHelper.db_alarms_session))+" - "+
                            cur.getString(cur.getColumnIndex(SQLiteHelper.db_alarms_status))+"\n"
                    );

                }while (cur.moveToNext());
            }cur.close();
        }dbr.close();db.close();
    }

    private void show_primary_data() {
        SQLiteHelper db = new SQLiteHelper(getApplicationContext());
        SQLiteDatabase dbr =db.getReadableDatabase();

        debug_info.setText(SQLiteHelper.db_med_db+"\n\n");

        Cursor cur = dbr.rawQuery("SELECT * FROM "+SQLiteHelper.db_med_db, null);

        if(cur != null){
            if (cur.moveToFirst()){
                do{
                    debug_info.append(
                        cur.getInt(cur.getColumnIndex(SQLiteHelper.db_med_id))+") "+
                        cur.getString(cur.getColumnIndex(SQLiteHelper.db_med_name))+" - "+
                        cur.getDouble(cur.getColumnIndex(SQLiteHelper.db_med_dose))+" - "+
                        cur.getDouble(cur.getColumnIndex(SQLiteHelper.db_med_quant))+" - "+
                        cur.getDouble(cur.getColumnIndex(SQLiteHelper.db_med_deduct))+" \n "
                    );

                    // boolean value = cursor.getInt(boolean_column_index) > 0;

                }while (cur.moveToNext());
            }cur.close();
        }dbr.close();db.close();
    }

    private void execute_alarm_manager(Context context) {

        SQLiteHelper db = new SQLiteHelper(context);
        SQLiteDatabase dbw =db.getWritableDatabase();

        String[] columns = {
                SQLiteHelper.db_alarms_id,
                SQLiteHelper.db_alarms_time,
                SQLiteHelper.db_alarms_date,
                SQLiteHelper.db_alarms_freq,
                SQLiteHelper.db_alarms_status
        };

        Cursor cur = dbw.query(SQLiteHelper.db_alarms, columns, null, null, null, null, null);

        if(cur != null){
            if (cur.moveToFirst()){
                do{

                    int alarm_id = cur.getInt(cur.getColumnIndex(SQLiteHelper.db_alarms_id));
                    String alarm_time = cur.getString(cur.getColumnIndex(SQLiteHelper.db_alarms_time));
                    String alarm_date = cur.getString(cur.getColumnIndex(SQLiteHelper.db_alarms_date));
                    String alarm_repeat = cur.getString(cur.getColumnIndex(SQLiteHelper.db_alarms_freq));
                    String alarm_status = cur.getString(cur.getColumnIndex(SQLiteHelper.db_alarms_status));

                    SimpleDateFormat std_date = new SimpleDateFormat(Constants.date_std);

                    Calendar cal_meds_date = Calendar.getInstance();                                                       // Meds Date Parse
                    Date meds_date = new Date();
                    try {
                        meds_date = std_date.parse(alarm_date);
                    } catch (ParseException e) {e.printStackTrace();}
                    cal_meds_date.setTime(meds_date);

                    Calendar cal_today = Calendar.getInstance();                                                        // Today Date Parse
                    String date_today = std_date.format(cal_today.getTime());
                    Date today_date = new Date();
                    try {
                        today_date = std_date.parse(date_today);
                    } catch (ParseException e) {e.printStackTrace();}
                    cal_today.setTime(today_date);

                    if (cal_today.equals(cal_meds_date)) {

                        Log.d("Medi:Alarm","Set Alarm for "+alarm_id);

                        if (alarm_status.equals(Constants.db_alarm_status_enabled)) {

                            //set_alarm (context, alarm_id, alarm_time);
                        }

                    } else {

                        switch (alarm_repeat){

                            case Constants.meds_freq_daily:

                                if (cal_today.after(cal_meds_date)) {

                                    cal_meds_date.set(Calendar.DATE, cal_today.get(Calendar.DATE));
                                }

                                if (cal_today.equals(cal_meds_date)) {

                                    //set_alarm (context, alarm_id, alarm_time);

                                }

                                Log.d("Medi:Check", cal_today.getTime()+" with "+cal_meds_date.getTime());

                                update_data(dbw, cal_meds_date, alarm_id);

                                break;

                            case Constants.meds_freq_weekly:

                                if (cal_today.after(cal_meds_date)) {
                                    do {
                                        cal_meds_date.add(Calendar.DATE, 7);

                                    } while (cal_today.after(cal_meds_date));
                                }

                                if (cal_today.equals(cal_meds_date)) {

                                    //set_alarm (context, alarm_id, alarm_time);

                                }

                                update_data(dbw, cal_meds_date, alarm_id);

                                break;
                        }
                    }

                }while (cur.moveToNext());
            }cur.close();
        }dbw.close();db.close();
    }

    private void update_data(SQLiteDatabase dbw, Calendar cal_task_date, int alarm_id) {

        SimpleDateFormat std_date = new SimpleDateFormat(Constants.date_std);

        String dtask_date = std_date.format(cal_task_date.getTime());

        ContentValues update_alarm = new ContentValues();
        update_alarm.put(SQLiteHelper.db_alarms_date, dtask_date);
        update_alarm.put(SQLiteHelper.db_alarms_status, Constants.db_alarm_status_enabled);

        dbw.update(SQLiteHelper.db_alarms, update_alarm, SQLiteHelper.db_alarms_id+" = ?", new String[]{alarm_id+""});

    }


}
