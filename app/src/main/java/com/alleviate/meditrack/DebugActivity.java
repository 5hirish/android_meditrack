package com.alleviate.meditrack;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
                        cur.getDouble(cur.getColumnIndex(SQLiteHelper.db_med_dose))+" - ("+
                        cur.getInt(cur.getColumnIndex(SQLiteHelper.db_med_morning))+" | "+
                        cur.getInt(cur.getColumnIndex(SQLiteHelper.db_med_noon))+" | "+
                        cur.getInt(cur.getColumnIndex(SQLiteHelper.db_med_evening))+") - "+
                        cur.getDouble(cur.getColumnIndex(SQLiteHelper.db_med_quant))+" - "+
                        cur.getDouble(cur.getColumnIndex(SQLiteHelper.db_med_deduct))+" \n "
                    );

                    // boolean value = cursor.getInt(boolean_column_index) > 0;

                }while (cur.moveToNext());
            }cur.close();
        }dbr.close();db.close();
    }

    private void execute_alarm_manager(Context context) {


    }

}
