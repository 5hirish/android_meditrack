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

/**
 * Created by felix on 18/6/16.
 * Created at Alleviate.
 * shirishkadam.com
 */
public class AlarmSetter extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        /*int pid = intent.getIntExtra(Constants.parent_alarm_id_key, 0);

        if (pid == 0) {

            Log.d("DayMan:ParentAlarm", "Parent Alarm Failed Id " + pid);
            SQLiteHelper.insert_log("DayMan:ParentAlarm - Parent Alarm Failed at - "+ Calendar.getInstance().getTime(),context);

        }*/

        if (Constants.debug_flag){
            parentAlarmNotify(context, Constants.parent_alarm_id);            // For Debug Only...!!
        }


        /*SharedPreferences parent_alarm_sp = context.getSharedPreferences(Constants.dayman_preferences, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = parent_alarm_sp.edit();
        SimpleDateFormat df = new SimpleDateFormat(Constants.date_std);
        String parent_alarm_date = df.format(new Date());
        editor.putString(Constants.parent_alarm_date_key, parent_alarm_date);

        editor.commit();
        if (Constants.debug_flag) {
            SQLiteHelper.insert_log("DayMan:ParentAlarm - Parent Alarm Invoked at - " + Calendar.getInstance().getTime(), context);
        }*/
        Log.d("Medi:ParentAlarm", "Parent Alarm Invoked Id " + Constants.parent_alarm_id);


    }

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

        /*TaskStackBuilder ts = TaskStackBuilder.create(context);
        ts.addParentStack(DashboardActivity.class);
        ts.addNextIntent(notify_in);*/

        //PendingIntent pendingIntent = ts.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, pid, notify_in, PendingIntent.FLAG_UPDATE_CURRENT);

        nb.setContentIntent(pendingIntent);

        NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(Constants.parent_alarm_id, nb.build());

    }
}
