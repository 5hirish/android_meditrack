package com.alleviate.meditrack;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
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
        String reset_alarm = parent_alarm_sp.getString(Constants.sp_alarm_reset, Constants.sp_alarm_reset_no);
        String first_install_tutorial = parent_alarm_sp.getString(Constants.sp_first_install_tutorial, Constants.sp_first_install_tutorial_Yes);
        boolean power_save_mode = parent_alarm_sp.getBoolean(Constants.power_saver_mode, true);

        SimpleDateFormat std_date = new SimpleDateFormat(Constants.date_std);
        String today_date = std_date.format(new Date());

        String deviceName = android.os.Build.MODEL;
        String deviceMan = android.os.Build.MANUFACTURER;
        deviceMan = deviceMan.toLowerCase();

        Log.d("Medi:DeviceInfo", deviceName+"-"+deviceMan);
        if(power_save_mode){
            if (deviceMan.equals(Constants.asus_man) || deviceMan.equals(Constants.samsung_man) || deviceMan.equals(Constants.xiaomi_man) || deviceMan.equals(Constants.huawei_man)) {
                check_hardware_support();
            }
        }

        if (first_install.equals(Constants.sp_medi_first_install_Yes)) {

            set_parent_alarm();
            parent_alarm_sp.edit().putString(Constants.sp_medi_first_install, Constants.sp_medi_first_install_No).apply();
        }

        if (!parent_alarm_date.equals(today_date) && !first_install.equals(Constants.sp_medi_first_install_Yes)) {

            set_parent_alarm();
            parent_alarm_sp.edit().putString(Constants.sp_parent_alarm_date_key, today_date).apply();
        }

        if (reset_alarm.equals(Constants.sp_alarm_reset_yes)) {

            parent_alarm_sp.edit().putString(Constants.sp_alarm_reset, Constants.sp_alarm_reset_no).apply();

            set_parent_alarm();

            parent_alarm_sp.edit().putString(Constants.sp_parent_alarm_date_key, today_date).apply();

        }

        if (first_install_tutorial.equals(Constants.sp_first_install_tutorial_Yes)){

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
        }
    }

    private void check_hardware_support() {

        NotificationCompat.Builder notification_builder = new NotificationCompat.Builder(getApplicationContext());

        notification_builder.setSmallIcon(R.drawable.ic_settings_device);
        notification_builder.setAutoCancel(true);
        notification_builder.setStyle(new NotificationCompat.BigTextStyle().bigText(getString(R.string.power_saving_desp)));
        notification_builder.setCategory(Notification.CATEGORY_SYSTEM);
        notification_builder.setPriority(Notification.PRIORITY_HIGH);
        notification_builder.setVisibility(Notification.VISIBILITY_PUBLIC);


        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notification_builder.setSound(alert);

        notification_builder.setTicker("Settings");
        notification_builder.setContentTitle(getString(R.string.medi_power_saving));
        notification_builder.setContentText(getString(R.string.power_saving_desp));

        Intent notification_intent = new Intent(getApplicationContext(), CompatibilityActivity.class);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(getApplicationContext());
        taskStackBuilder.addParentStack(DashboardActivity.class);
        taskStackBuilder.addNextIntent(notification_intent);

        PendingIntent notification_pendingIntent = taskStackBuilder.getPendingIntent( 0,PendingIntent.FLAG_UPDATE_CURRENT);
        notification_builder.setContentIntent(notification_pendingIntent);

        NotificationManager notificationManager =(NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify( 0, notification_builder.build());


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

        if (Build.VERSION.SDK_INT >= 19) {

            parent_alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, parent_pending_intent);

        } else {

            parent_alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, parent_pending_intent);
        }

        Log.d("Medi:ParentAlarm", "Parent Alarm Set Id " + parent_id);
        if (Constants.debug_flag) {
            SQLiteHelper.insert_log("Medi:ParentAlarm - Alarm (" + parent_id + ") created - " + Calendar.getInstance().getTime(), getApplicationContext());
        }
    }
}
