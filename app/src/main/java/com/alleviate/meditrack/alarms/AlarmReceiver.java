package com.alleviate.meditrack.alarms;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.alleviate.meditrack.DashboardActivity;
import com.alleviate.meditrack.R;
import com.alleviate.meditrack.constants.Constants;
import com.alleviate.meditrack.db.SQLiteHelper;

import java.util.Calendar;

/**
 * Created by felix on 24/6/16.
 * Created at Alleviate.
 * shirishkadam.com
 */
public class AlarmReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {

        int alarm_id = intent.getIntExtra(Constants.in_alarm_id, 0);
        if (Constants.debug_flag) {
            SQLiteHelper.insert_log("Medi:Alarm - Alarm (" + alarm_id + ") invoked - " + Calendar.getInstance().getTime(), context);
        }

        NotificationCompat.Builder notification_builder = new NotificationCompat.Builder(context);

        notification_builder.setSmallIcon(R.drawable.ic_notification);
        notification_builder.setAutoCancel(true);
        notification_builder.setCategory(Notification.CATEGORY_ALARM);
        notification_builder.setPriority(Notification.PRIORITY_HIGH);
        notification_builder.setVisibility(Notification.VISIBILITY_PUBLIC);

        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        notification_builder.setSound(alert);

        SQLiteHelper db = new SQLiteHelper(context);
        SQLiteDatabase dbr = db.getReadableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(SQLiteHelper.db_alarms +" INNER JOIN "+ SQLiteHelper.db_med_db +" ON "+SQLiteHelper.db_alarm_med_id+" = "+SQLiteHelper.db_med_id);
        Cursor cursor = queryBuilder.query(dbr, null, SQLiteHelper.db_alarms_id+" = ? ", new String[] { ""+alarm_id}, null, null, null);

        String med_name = "", med_dose = "", alarm_freq = "", alarm_date = "", alarm_status = "";
        int med_id = 0;

        if(cursor != null){
            while (cursor.moveToNext()){

                med_id = cursor.getInt(cursor.getColumnIndex(SQLiteHelper.db_med_id));
                med_name = cursor.getString(cursor.getColumnIndex(SQLiteHelper.db_med_name));
                med_dose = cursor.getString(cursor.getColumnIndex(SQLiteHelper.db_med_dose));

                alarm_freq = cursor.getString(cursor.getColumnIndex(SQLiteHelper.db_alarms_freq));
                alarm_date = cursor.getString(cursor.getColumnIndex(SQLiteHelper.db_alarms_date));
                alarm_status = cursor.getString(cursor.getColumnIndex(SQLiteHelper.db_alarms_status));

            }cursor.close();
        }
        dbr.close();
        db.close();

        notification_builder.setTicker(med_name);
        notification_builder.setContentTitle(med_name);

        notification_builder.setContentText("Dosage: "+med_dose+" tablets.");


        Intent notification_cancel_intent = new Intent(context, CancelReceiver.class);
        notification_cancel_intent.putExtra(Constants.in_notify_med_id, med_id);
        notification_cancel_intent.putExtra(Constants.in_notify_alarm_id, alarm_id);
        notification_cancel_intent.putExtra(Constants.in_notify_med_freq, alarm_freq);
        notification_cancel_intent.putExtra(Constants.in_notify_alarm_date, alarm_date);

        PendingIntent notification_cancel_pendingIntent = PendingIntent.getBroadcast(context, alarm_id, notification_cancel_intent,PendingIntent.FLAG_UPDATE_CURRENT);
        notification_builder.addAction(R.drawable.ic_done_24dp, context.getString(R.string.notify_done), notification_cancel_pendingIntent);

        Intent notification_intent = new Intent(context, DashboardActivity.class);

        PendingIntent notification_pendingIntent = PendingIntent.getActivity(context, alarm_id, notification_intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification_builder.setContentIntent(notification_pendingIntent);

        if (alarm_status.equals(Constants.db_alarm_status_enabled)) {

            NotificationManager notificationManager =(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(alarm_id, notification_builder.build());
        }

        set_alarm_inactive(alarm_id, context);

    }

    private void set_alarm_inactive(int alarm_id, Context context) {

        SQLiteHelper db = new SQLiteHelper(context);
        SQLiteDatabase dbr = db.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLiteHelper.db_alarms_status, Constants.db_alarm_status_disabled);

        dbr.update(SQLiteHelper.db_alarms, contentValues, SQLiteHelper.db_alarms_id +" = ? ", new String[] {alarm_id+""});
        dbr.close();
        db.close();
    }
}
