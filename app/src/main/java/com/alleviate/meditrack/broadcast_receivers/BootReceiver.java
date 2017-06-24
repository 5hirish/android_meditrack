package com.alleviate.meditrack.broadcast_receivers;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.alleviate.meditrack.alarms.AlarmRemind;
import com.alleviate.meditrack.alarms.AlarmSetter;
import com.alleviate.meditrack.constants.Constants;

import java.util.Calendar;

public class BootReceiver extends BroadcastReceiver{

    Context receviercontext;

    @Override
    public void onReceive(Context context, Intent intent) {

        receviercontext = context;

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")
                || intent.getAction().equals("android.intent.action.QUICKBOOT_POWERON")
                || intent.getAction().equals("android.intent.action.REBOOT")) {

            set_parent_alarm(context);

            //set_protocol_alarm(context);

            /*SharedPreferences parent_alarm_sp = context.getSharedPreferences(Constants.dayman_preferences, Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = parent_alarm_sp.edit();
            SimpleDateFormat df = new SimpleDateFormat(Constants.date_std);
            String parent_alarm_date = df.format(new Date());
            editor.putString(Constants.parent_alarm_date_key, parent_alarm_date);

            editor.apply();*/

            Log.d("Medi:BootMessage", "Boot Complete");
            Log.d("Medi:GeoFence","GeoFence set after Boot");

            /*if (Constants.debug_flag){
                SQLiteHelper.insert_log("DayMan:BootMessage - Boot Complete - " + Calendar.getInstance().getTime(), context);
            }*/
        }
    }

    private void set_parent_alarm(Context context) {

        int parent_id = Constants.parent_alarm_id;

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);

        Intent parent_intent = new Intent(context, AlarmSetter.class);
        //parent_intent.putExtra(Constants.parent_alarm_id_key, parent_id);

        PendingIntent parent_pending_intent = PendingIntent.getBroadcast(context, parent_id, parent_intent, 0);
        AlarmManager parent_alarm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        parent_alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, parent_pending_intent);

        Log.d("Medi:ParentAlarm", "Parent Alarm Set After Boot Id " + parent_id);

        /*if (Constants.debug_flag){
            SQLiteHelper.insert_log("DayMan:ParentAlarm - Parent Alarm Set After Boot at - "+ Calendar.getInstance().getTime(),context);
        }*/

    }

    /*private void set_protocol_alarm(Context context) {

        int parent_id = Constants.protocol_alarm_id;

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 1);
        cal.set(Calendar.MINUTE, 0);

        Intent parent_intent = new Intent(context, AlarmProtocol.class);
        //parent_intent.putExtra(Constants.protocol_alarm_id_key, parent_id);

        PendingIntent parent_pending_intent = PendingIntent.getBroadcast(context, parent_id, parent_intent, 0);
        AlarmManager parent_alarm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        parent_alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, parent_pending_intent);

        Log.d("DayMan:ProtocolAlarm", "Protocol Alarm Set Id " + parent_id);

        /*if (Constants.debug_flag){
            SQLiteHelper.insert_log("DayMan:ProtocolAlarm - Alarm ("+parent_id+") created - "+ Calendar.getInstance().getTime(),context);
        }*/

    //}
}
