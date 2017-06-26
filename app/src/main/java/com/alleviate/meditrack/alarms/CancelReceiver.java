package com.alleviate.meditrack.alarms;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import com.alleviate.meditrack.R;
import com.alleviate.meditrack.constants.Constants;
import com.alleviate.meditrack.db.SQLiteHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by felix on 24/6/16.
 * Created at Alleviate.
 * shirishkadam.com
 */
public class CancelReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        int med_id = intent.getIntExtra(Constants.in_notify_med_id, 0);
        int alarm_id = intent.getIntExtra(Constants.in_notify_alarm_id, 0);
        String med_freq = intent.getStringExtra(Constants.in_notify_med_freq);
        String alarm_date = intent.getStringExtra(Constants.in_notify_alarm_date);

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(alarm_id);

        PendingIntent alarm_pendingIntent = PendingIntent.getBroadcast(context, alarm_id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.cancel(alarm_pendingIntent);

        if (Constants.debug_flag){
            SQLiteHelper.insert_log("DayMan:Alarm - Alarm Canceled ("+alarm_id+") at - "+ Calendar.getInstance().getTime(), context);
        }

        // action_forward(med_freq, alarm_id, alarm_date, context);

    }

    private void action_forward(String arepeat, int alarm_id, String adate, Context context) {

        Calendar task_fwd_cal = Calendar.getInstance();
        SimpleDateFormat std_date =  new SimpleDateFormat(Constants.date_std);

        try {

            Date task_date = std_date.parse(adate);
            task_fwd_cal.setTime(task_date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        switch (arepeat) {

            case Constants.meds_freq_daily:

                task_fwd_cal.add(Calendar.DATE, 1);
                break;

            case Constants.meds_freq_weekly:

                task_fwd_cal.add(Calendar.DATE, 7);
                break;
        }

        String task_forward_date = std_date.format(task_fwd_cal.getTime());

        ContentValues forward_values = new ContentValues();
        forward_values.put(SQLiteHelper.db_alarms_date, task_forward_date);
        forward_values.put(SQLiteHelper.db_alarms_status, Constants.db_alarm_status_enabled);


        SQLiteHelper db = new SQLiteHelper(context);
        SQLiteDatabase dbw = db.getWritableDatabase();

        dbw.update(SQLiteHelper.db_alarms, forward_values, SQLiteHelper.db_alarms_id+" = ? ", new String[]{""+alarm_id});

        dbw.close();
        db.close();

    }
}
