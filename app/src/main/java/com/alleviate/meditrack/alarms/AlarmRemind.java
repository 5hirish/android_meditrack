package com.alleviate.meditrack.alarms;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.alleviate.meditrack.DashboardActivity;
import com.alleviate.meditrack.R;
import com.alleviate.meditrack.constants.Constants;

/**
 * Created by felix on 4/7/16.
 * Created at Alleviate.
 * shirishkadam.com
 */
public class AlarmRemind extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        /*int pid = intent.getIntExtra(Constants.reminder_alarm_id_key, 0);

        if (pid == 0) {

            Log.d("DayMan:ReminderAlarm", "Remind Alarm Failed Id " + pid);
            SQLiteHelper.insert_log("DayMan:ReminderAlarm - Remind Alarm Failed at - "+ Calendar.getInstance().getTime(),context);

        }*/

        remindAlarmNotify(context, Constants.reminder_alarm_id);
        /*if (Constants.debug_flag) {
            SQLiteHelper.insert_log("DayMan:ReminderAlarm - Remind Alarm Invoked at - " + Calendar.getInstance().getTime(), context);
        }*/

    }

    private void remindAlarmNotify(Context context, int pid) {

        NotificationCompat.Builder nb = new NotificationCompat.Builder(context);
        nb.setAutoCancel(true);
        nb.setSmallIcon(R.drawable.ic_notification);
        nb.setContentTitle("Medi : Plan your medicines ahead...");
        nb.setContentText("View your schedule for the day");

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
        nm.notify(Constants.reminder_alarm_id, nb.build());

    }
}
