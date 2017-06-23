package com.alleviate.meditrack;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.alleviate.meditrack.alarms.AlarmSetter;
import com.alleviate.meditrack.constants.Constants;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences parent_alarm_sp = getSharedPreferences("Medi", MODE_PRIVATE);
        String first_install = parent_alarm_sp.getString("FI", "Yes");

        if (first_install.equals("Yes")) {

            set_parent_alarm();
            parent_alarm_sp.edit().putString("FI", "No").apply();
        }

        Intent in = new Intent(MainActivity.this, DashboardActivity.class);
        startActivity(in);
        finish();
    }

    private void set_parent_alarm() {

        int parent_id = Constants.parent_alarm_id;

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);

        Intent parent_intent = new Intent(getApplicationContext(), AlarmSetter.class);
        //parent_intent.putExtra(Constants.parent_alarm_id_key, parent_id);

        PendingIntent parent_pending_intent = PendingIntent.getBroadcast(getApplicationContext(), parent_id, parent_intent, 0);
        AlarmManager parent_alarm = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);

        parent_alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, parent_pending_intent);

        Log.d("Medi:ParentAlarm", "Parent Alarm Set Id " + parent_id);
        /*if (Constants.debug_flag) {
            SQLiteHelper.insert_log("DayMan:ParentAlarm - Alarm (" + parent_id + ") created - " + Calendar.getInstance().getTime(), getApplicationContext());
        }*/
    }
}
