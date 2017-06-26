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
import android.database.sqlite.SQLiteQueryBuilder;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
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

        //execute_alarm_manager(context);

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

    /*private void execute_alarm_manager(Context context) {

        SQLiteHelper db = new SQLiteHelper(context);
        SQLiteDatabase dbw =db.getWritableDatabase();

        String[] columns = {
                SQLiteHelper.db_alarms_id,
                SQLiteHelper.db_alarm_med_id,
                SQLiteHelper.db_alarms_time,
                SQLiteHelper.db_alarms_date,
                SQLiteHelper.db_alarms_status
        };

        Cursor cur = dbw.query(SQLiteHelper.db_time, columns, null, null, null, null, null);

        if(cur != null){
            if (cur.moveToFirst()){
                do{

                    int alarm_id = cur.getInt(cur.getColumnIndex(SQLiteHelper.db_time_id));
                    String alarm_time = cur.getString(cur.getColumnIndex(SQLiteHelper.db_time_time));
                    String alarm_date = cur.getString(cur.getColumnIndex(SQLiteHelper.db_time_date));
                    String alarm_repeat = cur.getString(cur.getColumnIndex(SQLiteHelper.db_time_repeat));
                    String alarm_status = cur.getString(cur.getColumnIndex(SQLiteHelper.db_time_alarm_status));
                    String alarm_task_status = cur.getString(cur.getColumnIndex(SQLiteHelper.db_time_task_status));

                    SimpleDateFormat std_date = new SimpleDateFormat(Constants.date_std);

                    Calendar cal_task_date = Calendar.getInstance();                                                       // Task Date Parse
                    Date task_date = new Date();
                    try {
                        task_date = std_date.parse(alarm_date);
                    } catch (ParseException e) {e.printStackTrace();}
                    cal_task_date.setTime(task_date);

                    Calendar cal_today = Calendar.getInstance();                                                        // Today Date Parse
                    String date_today = std_date.format(cal_today.getTime());
                    Date today_date = new Date();
                    try {
                        today_date = std_date.parse(date_today);
                    } catch (ParseException e) {e.printStackTrace();}
                    cal_today.setTime(today_date);

                    if (cal_today.equals(cal_task_date)) {

                        Log.d("DayMan:Alarm","Set Alarm for "+alarm_id);

                        if (!alarm_task_status.equals(Constants.task_status_dumped) && !alarm_repeat.equals(Constants.task_repeat_daily)) {

                            set_alarm (context, alarm_id, alarm_time);

                            String dtask_status = Constants.task_status_pending;
                            ContentValues update_status = new ContentValues();
                            update_status.put(SQLiteHelper.db_time_task_status, dtask_status);

                            dbw.update(SQLiteHelper.db_time, update_status, SQLiteHelper.db_time_id+" = ?", new String[]{alarm_id+""});

                        }

                    }

                    String dtask_status = Constants.task_status_forward;        // Default

                    switch (alarm_repeat){

                        case Constants.task_repeat_daily:

                            if (cal_today.after(cal_task_date)) {

                                cal_task_date.set(Calendar.DATE, cal_today.get(Calendar.DATE));
                            }

                            if (cal_today.equals(cal_task_date)) {

                                dtask_status = Constants.task_status_pending;

                                set_alarm (context, alarm_id, alarm_time);

                            }

                            update_data(dbw, cal_task_date, alarm_id, dtask_status);

                            break;

                        case Constants.task_repeat_weekly:

                            if (cal_today.after(cal_task_date)) {
                                do {
                                    cal_task_date.add(Calendar.DATE, 7);

                                } while (cal_today.after(cal_task_date));
                            }

                            if (cal_today.equals(cal_task_date)) {

                                dtask_status = Constants.task_status_pending;
                            } else {

                                dtask_status = Constants.task_status_forward;
                            }

                            update_data(dbw, cal_task_date, alarm_id, dtask_status);

                            break;

                        case Constants.task_repeat_monthly:

                            if (cal_today.after(cal_task_date)) {
                                do {
                                    cal_task_date.add(Calendar.MONTH, 1);

                                } while (cal_today.after(cal_task_date));
                            }

                            if (cal_today.equals(cal_task_date)) {

                                dtask_status = Constants.task_status_pending;
                            } else {
                                dtask_status = Constants.task_status_forward;

                            }

                            update_data(dbw, cal_task_date, alarm_id, dtask_status);

                            break;
                    }


                }while (cur.moveToNext());
            }cur.close();
        }dbw.close();db.close();
    }*/

    private void parentAlarmNotify(Context context, int pid) {

        NotificationCompat.Builder nb= new NotificationCompat.Builder(context);
        nb.setAutoCancel(true);
        nb.setSmallIcon(R.drawable.ic_notification);
        nb.setContentTitle("Medi : Alarm Setter...");
        nb.setContentText("Setting alarms for today");

        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        nb.setSound(alert);
        nb.setCategory(Notification.CATEGORY_STATUS);

        Intent notify_in = new Intent(context, DashboardActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, pid, notify_in, PendingIntent.FLAG_UPDATE_CURRENT);

        nb.setContentIntent(pendingIntent);

        NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(Constants.parent_alarm_id, nb.build());

    }
}
