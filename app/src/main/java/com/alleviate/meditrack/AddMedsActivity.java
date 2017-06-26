package com.alleviate.meditrack;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.alleviate.meditrack.alarms.AlarmReceiver;
import com.alleviate.meditrack.constants.Constants;
import com.alleviate.meditrack.db.SQLiteHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddMedsActivity extends AppCompatActivity {

    String db_meds_name, db_meds_dose_freq;
    double db_meds_prescription, db_meds_quantity, db_meds_deducts;

    String[] db_meds_time = new String[3];
    boolean db_meds_dose_session[] = new boolean[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meds);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final EditText et_med_name = (EditText)findViewById(R.id.medicine_name);
        final TextView tv_med_dose_freq = (TextView)findViewById(R.id.med_freq);
        final CheckBox cb_med_dose_morning = (CheckBox)findViewById(R.id.cb_morning);
        final CheckBox cb_med_dose_noon = (CheckBox)findViewById(R.id.cb_noon);
        final CheckBox cb_med_dose_night = (CheckBox)findViewById(R.id.cb_evening);
        final TextView tv_med_dose_morning_time = (TextView)findViewById(R.id.morning_time);
        final TextView tv_med_dose_noon_time = (TextView)findViewById(R.id.noon_time);
        final TextView tv_med_dose_night_time = (TextView)findViewById(R.id.evening_time);
        final TextView tv_med_dose_quant = (TextView)findViewById(R.id.dose_quant);
        final EditText et_med_dose_total = (EditText)findViewById(R.id.total_dose);
        final TextView tv_debug_info = (TextView)findViewById(R.id.debug_01);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat time_12hr = new SimpleDateFormat(Constants.time_12hr);
        SimpleDateFormat time_24hr = new SimpleDateFormat(Constants.time_24hr);
        SimpleDateFormat date_std = new SimpleDateFormat(Constants.date_std);

        tv_med_dose_morning_time.setText(init_time(calendar, time_12hr, 9));
        tv_med_dose_noon_time.setText(init_time(calendar, time_12hr, 13));
        tv_med_dose_night_time.setText(init_time(calendar, time_12hr, 20));

        cb_med_dose_night.setChecked(true);                                     // Night Does is default.
        db_meds_dose_freq = getString(R.string.meds_freq_daily);
        db_meds_quantity = 10;
        db_meds_prescription = 1;
        db_meds_deducts = 1;
        et_med_dose_total.setText(String.valueOf(db_meds_quantity));

        tv_med_dose_freq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                prep_dialogue_dose_freq(tv_med_dose_freq).show();
            }
        });

        tv_med_dose_morning_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                prep_dialogue_time_picker(9, tv_med_dose_morning_time).show();
            }
        });

        tv_med_dose_noon_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                prep_dialogue_time_picker(13, tv_med_dose_noon_time).show();
            }
        });

        tv_med_dose_night_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                prep_dialogue_time_picker(20, tv_med_dose_night_time).show();
            }
        });


        tv_med_dose_quant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prep_dialogue_dose_prescription(tv_med_dose_quant).show();
            }
        });
        //spi_med_dose_quant

        FloatingActionButton fab_add_med = (FloatingActionButton) findViewById(R.id.fab_add_new_meds);
        fab_add_med.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String str_med_name = et_med_name.getText().toString();
                db_meds_name = str_med_name.replaceAll("'","\'\'");

                db_meds_quantity = Double.parseDouble(et_med_dose_total.getText().toString());
                db_meds_deducts = 0;

                try {

                    SimpleDateFormat sdf_std = new SimpleDateFormat(Constants.time_std);
                    SimpleDateFormat sdf_12hr = new SimpleDateFormat(Constants.time_12hr);

                    db_meds_time[0] = sdf_std.format(sdf_12hr.parse(tv_med_dose_morning_time.getText().toString()));
                    db_meds_time[1] = sdf_std.format(sdf_12hr.parse(tv_med_dose_noon_time.getText().toString()));
                    db_meds_time[2] = sdf_std.format(sdf_12hr.parse(tv_med_dose_night_time.getText().toString()));

                } catch (ParseException exp){
                    Log.d("Medi:Exception","Date Parsing exception - "+exp);

                }

                if (cb_med_dose_morning.isChecked()){
                    db_meds_dose_session[0] = true;
                    db_meds_deducts += db_meds_prescription;
                }

                if (cb_med_dose_noon.isChecked()) {
                    db_meds_dose_session[1] = true;
                    db_meds_deducts += db_meds_prescription;
                }

                if (cb_med_dose_night.isChecked()) {
                    db_meds_dose_session[2] = true;
                    db_meds_deducts += db_meds_prescription;
                }

                if (!cb_med_dose_morning.isChecked() && !cb_med_dose_noon.isChecked() && !cb_med_dose_night.isChecked()) {
                    db_meds_dose_session[0] = false;
                    db_meds_dose_session[1] = false;
                    db_meds_dose_session[2] = true;
                    db_meds_deducts = db_meds_prescription;

                }


                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf_std = new SimpleDateFormat(Constants.date_std);
                String db_alarm_date = sdf_std.format(cal.getTime());


                if (db_meds_name.equals("")){
                    Snackbar.make(view, "Medicine name is empty!", Snackbar.LENGTH_LONG).show();
                } else {

                    insert_medicine(db_meds_name, db_meds_dose_freq, db_meds_prescription, db_alarm_date, db_meds_quantity, db_meds_deducts);

                    Toast.makeText(getApplicationContext()," Reminder Set", Toast.LENGTH_SHORT).show();

                }

                tv_debug_info.setText("Name: "+db_meds_name+"\nFrequency: "+db_meds_dose_freq+"\nPrescription: "+db_meds_prescription+
                        "\nMorning: "+db_meds_time[0]+", Noon: "+db_meds_time[1]+", Night: "+db_meds_time[2]+"\nQuantity: "+db_meds_quantity+"\nDeducts:");

            }
        });
    }

    private void insert_medicine(String db_meds_name, String db_meds_dose_freq, double db_meds_prescription,
                                 String db_alarm_date, double db_meds_quantity, double db_meds_deducts) {

        SQLiteHelper db = new SQLiteHelper(getApplicationContext());
        SQLiteDatabase dbw = db.getWritableDatabase();

        ContentValues medicine_val = new ContentValues();
        medicine_val.put(SQLiteHelper.db_med_name, db_meds_name);
        medicine_val.put(SQLiteHelper.db_med_dose, db_meds_prescription);
        medicine_val.put(SQLiteHelper.db_med_quant, db_meds_quantity);
        medicine_val.put(SQLiteHelper.db_med_deduct, db_meds_deducts);

        long db_meds_id = dbw.insert(SQLiteHelper.db_med_db, null, medicine_val);


        for(int i = 0; i < 3; i++){
            if (db_meds_dose_session[i]){

                ContentValues alarms_val = new ContentValues();
                alarms_val.put(SQLiteHelper.db_alarm_med_id, (int) db_meds_id);
                alarms_val.put(SQLiteHelper.db_alarms_time, db_meds_time[i]);
                alarms_val.put(SQLiteHelper.db_alarms_date, db_alarm_date);
                alarms_val.put(SQLiteHelper.db_alarms_freq, db_meds_dose_freq);
                alarms_val.put(SQLiteHelper.db_alarms_session, Constants.db_dose_session[i]);
                alarms_val.put(SQLiteHelper.db_alarms_status, Constants.db_alarm_status_enabled);

                SimpleDateFormat std_time = new SimpleDateFormat(Constants.time_std);

                Calendar cal_meds_time_temp = Calendar.getInstance();                                                       // Meds Time Parse
                final Calendar cal_meds_time = Calendar.getInstance();

                try {
                    cal_meds_time_temp.setTime(std_time.parse(db_meds_time[i]));
                    cal_meds_time.set(Calendar.HOUR_OF_DAY, cal_meds_time_temp.get(Calendar.HOUR_OF_DAY));
                    cal_meds_time.set(Calendar.MINUTE, cal_meds_time_temp.get(Calendar.MINUTE));

                } catch (ParseException e) {e.printStackTrace();}

                final Calendar cal_now = Calendar.getInstance();                                                        // Today Time Parse

                Log.d("Medi:Check",cal_now.getTime() + " before " + cal_meds_time.getTime());

                final int pos = i;
                InsertAlarm insertAlarm = new InsertAlarm(new AlarmAsyncResponse() {
                    @Override
                    public void alarmInserted(Long db_alarm_id) {
                        if (cal_now.before(cal_meds_time)){

                            set_alarm(db_alarm_id, db_meds_time[pos], db_meds_dose_session[pos]);
                        }
                    }
                });

                insertAlarm.execute(alarms_val);
            }
        }

        dbw.close();
        db.close();

    }

    private String init_time(Calendar calendar, SimpleDateFormat time_12hr, int hour) {

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, 0);

        return time_12hr.format(calendar.getTime());
    }

    private AlertDialog prep_dialogue_dose_prescription(final TextView tv_med_dose_quant) {

        final ArrayAdapter meds_dose_prescription_adapter =
                ArrayAdapter.createFromResource(AddMedsActivity.this, R.array.meds_dose_prescription, R.layout.select_dialog_item);

        final AlertDialog.Builder meds_dose_pres_dialog = new AlertDialog.Builder(AddMedsActivity.this);
        meds_dose_pres_dialog.setCancelable(true);

        LayoutInflater dialog_inflater_pres = getLayoutInflater();
        final View dialog_view_pres = dialog_inflater_pres.inflate(R.layout.dialog_layout, null );

        TextView dialog_title_pres = (TextView) dialog_view_pres.findViewById(R.id.title);
        dialog_title_pres.setText(getString(R.string.select_meds_dose_prescription));
        ListView dialog_list_pres = (ListView) dialog_view_pres.findViewById(R.id.dialog_list);
        dialog_list_pres.setAdapter(meds_dose_prescription_adapter);

        meds_dose_pres_dialog.setView(dialog_view_pres);

        final AlertDialog select_med_pres = meds_dose_pres_dialog.create();

        dialog_list_pres.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        db_meds_prescription = 0.5;
                        break;
                    case 1:
                        db_meds_prescription = 1;
                        break;
                    case 2:
                        db_meds_prescription = 1.5;
                        break;
                    case 3:
                        db_meds_prescription = 2;
                        break;
                    default:
                        db_meds_prescription = 1;
                        break;
                }

                tv_med_dose_quant.setText(meds_dose_prescription_adapter.getItem(position).toString());

                select_med_pres.dismiss();

            }
        });

        return select_med_pres;

    }

    private TimePickerDialog prep_dialogue_time_picker(int hour, final TextView tv_med_dose_time) {

        final TimePickerDialog select_time_dialog = new TimePickerDialog(AddMedsActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int picked_hour, int picked_minute) {

                SimpleDateFormat time_12hr = new SimpleDateFormat(Constants.time_12hr);
                SimpleDateFormat std_time = new SimpleDateFormat(Constants.time_std);
                String meds_time = picked_hour+":"+picked_minute;

                try{

                    Date picked_time = std_time.parse(meds_time);
                    tv_med_dose_time.setText(time_12hr.format(picked_time));

                }catch (java.text.ParseException exp){
                    Log.d("Medi:Exception","Time Parsing exception - "+exp);
                }
            }
        }, hour, 0, false);

        return select_time_dialog;
    }

    private AlertDialog prep_dialogue_dose_freq(final TextView tv_med_dose_freq) {

        final ArrayAdapter meds_dose_freq_adapter = ArrayAdapter.createFromResource(AddMedsActivity.this, R.array.meds_dose_freq, R.layout.select_dialog_item);
        final ArrayList<Integer> weekdays = new ArrayList<Integer>();

        final AlertDialog.Builder meds_dose_freq_dialog = new AlertDialog.Builder(AddMedsActivity.this);
        meds_dose_freq_dialog.setCancelable(true);

        LayoutInflater dialog_inflater = getLayoutInflater();
        final View dialog_view_freq = dialog_inflater.inflate(R.layout.dialog_layout, null );

        TextView dialog_title_freq = (TextView) dialog_view_freq.findViewById(R.id.title);
        dialog_title_freq.setText(getString(R.string.select_meds_dose_freq));
        ListView dialog_list_freq = (ListView) dialog_view_freq.findViewById(R.id.dialog_list);
        dialog_list_freq.setAdapter(meds_dose_freq_adapter);

        meds_dose_freq_dialog.setView(dialog_view_freq);

        final AlertDialog select_dose_freq = meds_dose_freq_dialog.create();

        dialog_list_freq.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                db_meds_dose_freq = meds_dose_freq_adapter.getItem(position).toString();

                // Dialogue for displaying days of week //

                if (db_meds_dose_freq.equals(Constants.selected_weekdays)) {

                    weekdays.clear();

                    AlertDialog.Builder selected_weekdays_dialog = new AlertDialog.Builder(AddMedsActivity.this);
                    selected_weekdays_dialog.setTitle(getString(R.string.select_meds_weekdays));
                    selected_weekdays_dialog.setMultiChoiceItems(R.array.weekdays, null, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int position, boolean ischecked) {

                            if (ischecked) {
                                weekdays.add(position);

                            } else if (weekdays.contains(position)) {
                                weekdays.remove(Integer.valueOf(position));

                            }
                        }
                    });

                    selected_weekdays_dialog.setPositiveButton(getString(R.string.dialogue_ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            int size = weekdays.size();
                            //Snackbar.make(getView(), size+" Weekdays selected", Snackbar.LENGTH_LONG).show();
                        }
                    });

                    selected_weekdays_dialog.setNegativeButton(getString(R.string.dialogue_cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            weekdays.clear();
                            db_meds_dose_freq = getString(R.string.meds_freq_daily);
                        }
                    });

                    AlertDialog weekday = selected_weekdays_dialog.create();
                    weekday.show();
                }

                tv_med_dose_freq.setText(db_meds_dose_freq);

                select_dose_freq.dismiss();
            }
        });

        return select_dose_freq;
    }

    interface AlarmAsyncResponse {
        void alarmInserted(Long db_alarm_id);
    }

    private class InsertAlarm extends AsyncTask<ContentValues, Void, Long> {

        AlarmAsyncResponse alarm_response_handler = null;

        InsertAlarm(AlarmAsyncResponse asyncResponse) {
            alarm_response_handler = asyncResponse;                     // Assigning call back interface through constructor
        }

        @Override
        protected Long doInBackground(ContentValues... contentValues) {

            SQLiteHelper db = new SQLiteHelper(getApplicationContext());
            SQLiteDatabase dbw = db.getWritableDatabase();

            long resid = dbw.insert(SQLiteHelper.db_alarms, null, contentValues[0]);

            dbw.close();
            db.close();

            return resid;
        }

        @Override
        protected void onPostExecute(Long db_alarm_id) {

            Log.d("Medi:Database", "Alarm Inserted " + db_alarm_id);

            alarm_response_handler.alarmInserted(db_alarm_id);
        }
    }

    private void set_alarm(long db_alarm_id, String db_meds_time, boolean db_meds_dose_session) {

        int alarm_id = (int) db_alarm_id;

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

        Intent alarm_intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        alarm_intent.putExtra(Constants.in_alarm_id, alarm_id);

        PendingIntent alarm_pending_intent = PendingIntent.getBroadcast(getApplicationContext(), alarm_id, alarm_intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, alarm_time.getTimeInMillis(), 0, alarm_pending_intent);

        Log.d("Medi:Alarm","Set Alarm : "+alarm_id+" at "+ alarm_time.getTime());

        if (Constants.debug_flag){
            SQLiteHelper.insert_log("Medi:Alarm - Alarm Set ("+alarm_id+") at - "+ alarm_time.getTime(), getApplicationContext());
        }

    }
}
