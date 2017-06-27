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
import com.alleviate.meditrack.db.SQLiteHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences parent_alarm_sp = getSharedPreferences(Constants.sp_medi_file, MODE_PRIVATE);
        String first_install = parent_alarm_sp.getString(Constants.sp_medi_first_install, Constants.sp_medi_first_install_Yes);
        String parent_alarm_date = parent_alarm_sp.getString(Constants.sp_parent_alarm_date_key, Constants.sp_alarm_reset_no);
        String first_install_tutorial = parent_alarm_sp.getString(Constants.sp_first_install_tutorial, Constants.sp_first_install_tutorial_Yes);

        SimpleDateFormat std_date = new SimpleDateFormat(Constants.date_std);
        String today_date = std_date.format(new Date());


        if (first_install.equals(Constants.sp_medi_first_install_Yes)) {

            set_parent_alarm();
            parent_alarm_sp.edit().putString(Constants.sp_medi_first_install, Constants.sp_medi_first_install_No).apply();
        }

        if (!parent_alarm_date.equals(today_date) && !first_install.equals(Constants.sp_medi_first_install_Yes)) {

            set_parent_alarm();
            parent_alarm_sp.edit().putString(Constants.sp_parent_alarm_date_key, today_date).apply();
        }

        /*if (first_install_tutorial.equals(Constants.sp_first_install_tutorial_Yes)){

            parent_alarm_sp.edit().putString(Constants.sp_first_install_tutorial, Constants.sp_first_install_tutorial_No).apply();

            Intent in_back = new Intent(MainActivity.this, DashboardActivity.class);
            startActivity(in_back);

            Intent in = new Intent(MainActivity.this, TutorialActivity.class);
            startActivity(in);
            finish();

        } else {

            Intent in = new Intent(MainActivity.this, DashboardActivity.class);
            startActivity(in);
            finish();
        }*/

        Intent in = new Intent(MainActivity.this, TutorialActivity.class);
        startActivity(in);
        finish();


        /*Intent in = new Intent(MainActivity.this, DashboardActivity.class);
        startActivity(in);
        finish();*/
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
        if (Constants.debug_flag) {
            SQLiteHelper.insert_log("Medi:ParentAlarm - Alarm (" + parent_id + ") created - " + Calendar.getInstance().getTime(), getApplicationContext());
        }
    }
}
