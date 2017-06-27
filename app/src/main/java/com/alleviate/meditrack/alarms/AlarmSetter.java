package com.alleviate.meditrack.alarms;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.alleviate.meditrack.DashboardActivity;
import com.alleviate.meditrack.R;
import com.alleviate.meditrack.constants.Constants;
import com.alleviate.meditrack.db.SQLiteHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by felix on 18/6/16.
 * Created at Alleviate.
 * shirishkadam.com
 */
public class AlarmSetter extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (Constants.debug_flag){
            parentAlarmNotify(context, Constants.parent_alarm_id);            // For Debug Only...!!
        }

        execute_alarm_manager(context);

        SharedPreferences parent_alarm_sp = context.getSharedPreferences(Constants.sp_medi_file, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = parent_alarm_sp.edit();
        SimpleDateFormat df = new SimpleDateFormat(Constants.date_std);
        String parent_alarm_date = df.format(new Date());
        editor.putString(Constants.sp_parent_alarm_date_key, parent_alarm_date);
        editor.commit();

        if (Constants.debug_flag) {
            SQLiteHelper.insert_log("Medi:ParentAlarm - Parent Alarm Invoked at - " + Calendar.getInstance().getTime(), context);
        }
        Log.d("Medi:ParentAlarm", "Parent Alarm Invoked Id " + Constants.parent_alarm_id);


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

                            set_alarm (context, alarm_id, alarm_time);
                        }

                    } else {

                        switch (alarm_repeat){

                            case Constants.meds_freq_daily:

                                if (cal_today.after(cal_meds_date)) {

                                    cal_meds_date.set(Calendar.DATE, cal_today.get(Calendar.DATE));
                                }

                                if (cal_today.equals(cal_meds_date)) {

                                    set_alarm (context, alarm_id, alarm_time);

                                }

                                update_data(dbw, cal_meds_date, alarm_id);

                                break;

                            case Constants.meds_freq_weekly:

                                if (cal_today.after(cal_meds_date)) {
                                    do {
                                        cal_meds_date.add(Calendar.DATE, 7);

                                    } while (cal_today.after(cal_meds_date));
                                }

                                if (cal_today.equals(cal_meds_date)) {

                                    set_alarm (context, alarm_id, alarm_time);

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

    private void set_alarm(Context context, int alarm_id, String db_meds_time) {

        SimpleDateFormat sdf_std = new SimpleDateFormat(Constants.time_std);
        Calendar calendar = Calendar.getInstance();

        try{

            Date date_alarm_time = sdf_std.parse(db_meds_time);
            calendar.setTime(date_alarm_time);

        } catch (ParseException exp){
            Log.d("Medi:Exception","Time Parsing exception - "+exp);
        }

        Calendar alarm_time = Calendar.getInstance();
        alarm_time.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
        alarm_time.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));

        Intent alarm_intent = new Intent(context, AlarmReceiver.class);
        alarm_intent.putExtra(Constants.in_alarm_id, alarm_id);

        PendingIntent alarm_pending_intent = PendingIntent.getBroadcast(context, alarm_id, alarm_intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, alarm_time.getTimeInMillis(), 0, alarm_pending_intent);

        Log.d("Medi:Alarm","Set Alarm : "+alarm_id+" at "+ alarm_time.getTime());

        if (Constants.debug_flag){
            SQLiteHelper.insert_log("Medi:Alarm - Alarm Set ("+alarm_id+") at - "+ alarm_time.getTime(), context);
        }

    }

    private void parentAlarmNotify(Context context, int pid) {

        NotificationCompat.Builder nb= new NotificationCompat.Builder(context);
        nb.setAutoCancel(true);
        nb.setSmallIcon(R.drawable.ic_notification);
        nb.setContentTitle("Medi : Alarm Setter...");
        nb.setContentText("Setting alarms for today");

        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        nb.setSound(alert);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            nb.setCategory(Notification.CATEGORY_STATUS);
        }

        Intent notify_in = new Intent(context, DashboardActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, pid, notify_in, PendingIntent.FLAG_UPDATE_CURRENT);

        nb.setContentIntent(pendingIntent);

        NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(Constants.parent_alarm_id, nb.build());

    }
}
