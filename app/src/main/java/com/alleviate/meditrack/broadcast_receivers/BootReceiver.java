package com.alleviate.meditrack.broadcast_receivers;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import com.alleviate.meditrack.alarms.AlarmSetter;
import com.alleviate.meditrack.constants.Constants;
import com.alleviate.meditrack.db.SQLiteHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BootReceiver extends BroadcastReceiver{

    Context receviercontext;

    @Override
    public void onReceive(Context context, Intent intent) {

        receviercontext = context;

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")
                || intent.getAction().equals("android.intent.action.QUICKBOOT_POWERON")
                || intent.getAction().equals("android.intent.action.REBOOT")) {

            set_parent_alarm(context);

            SharedPreferences parent_alarm_sp = context.getSharedPreferences(Constants.sp_medi_file, Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = parent_alarm_sp.edit();
            SimpleDateFormat df = new SimpleDateFormat(Constants.date_std);
            String parent_alarm_date = df.format(new Date());
            editor.putString(Constants.sp_parent_alarm_date_key, parent_alarm_date);

            editor.apply();

            Log.d("Medi:BootMessage", "Boot Complete");

            if (Constants.debug_flag){
                SQLiteHelper.insert_log("Medi:BootMessage - Boot Complete - " + Calendar.getInstance().getTime(), context);
            }
        }
    }

    private void set_parent_alarm(Context context) {

        int parent_id = Constants.parent_alarm_id;

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);

        Intent parent_intent = new Intent(context, AlarmSetter.class);

        PendingIntent parent_pending_intent = PendingIntent.getBroadcast(context, parent_id, parent_intent, 0);
        AlarmManager parent_alarm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= 19) {

            parent_alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, parent_pending_intent);

        } else {

            parent_alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, parent_pending_intent);
        }

        Log.d("Medi:ParentAlarm", "Parent Alarm Set After Boot Id " + parent_id);

        if (Constants.debug_flag){
            SQLiteHelper.insert_log("Medi:ParentAlarm - Parent Alarm Set After Boot at - "+ Calendar.getInstance().getTime(),context);
        }

    }
}
