package com.alleviate.meditrack;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.alleviate.meditrack.constants.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddMedsActivity extends AppCompatActivity {

    String db_meds_name, db_meds_dose_freq;
    int alarm_minutes, alarm_hour, alarm_day, alarm_month, alarm_year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meds);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText et_med_name = (EditText)findViewById(R.id.medicine_name);
        TextView tv_med_dose_freq = (TextView)findViewById(R.id.med_freq);
        CheckBox cb_med_dose_morning = (CheckBox)findViewById(R.id.cb_morning);
        CheckBox cb_med_dose_noon = (CheckBox)findViewById(R.id.cb_noon);
        CheckBox cb_med_dose_night = (CheckBox)findViewById(R.id.cb_evening);
        TextView tv_med_dose_morning_time = (TextView)findViewById(R.id.morning_time);
        TextView tv_med_dose_noon_time = (TextView)findViewById(R.id.noon_time);
        TextView tv_med_dose_night_time = (TextView)findViewById(R.id.evening_time);
        TextView tv_med_dose_quant = (TextView)findViewById(R.id.dose_quant);
        EditText et_med_dose_total = (EditText)findViewById(R.id.total_dose);
        final TextView tv_debug_info = (TextView)findViewById(R.id.debug_01);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat time_12hr = new SimpleDateFormat(Constants.time_12hr);
        SimpleDateFormat time_24hr = new SimpleDateFormat(Constants.time_24hr);
        SimpleDateFormat date_std = new SimpleDateFormat(Constants.date_std);

        tv_med_dose_morning_time.setText(init_time(calendar, time_12hr, 9));
        tv_med_dose_noon_time.setText(init_time(calendar, time_12hr, 13));
        tv_med_dose_night_time.setText(init_time(calendar, time_12hr, 20));


        tv_med_dose_freq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prep_dialogue_dose_freq().show();

            }
        });

        tv_med_dose_morning_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                prep_dialogue_time_picker(9).show();
            }
        });

        tv_med_dose_noon_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                prep_dialogue_time_picker(13).show();
            }
        });

        tv_med_dose_night_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                prep_dialogue_time_picker(20).show();
            }
        });


        tv_med_dose_quant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prep_dialogue_dose_prescription().show();
            }
        });
        //spi_med_dose_quant

        FloatingActionButton fab_add_med = (FloatingActionButton) findViewById(R.id.fab_add_new_meds);
        fab_add_med.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String str_med_name = et_med_name.getText().toString();
                db_meds_name = str_med_name.replaceAll("'","\'\'");

                if (db_meds_name.equals("")){
                    Snackbar.make(view, "Medicine name is empty!", Snackbar.LENGTH_LONG).show();
                } else {

                    //finish();
                }

                tv_debug_info.setText("Name:\nFrequency:\nPrescription:\nMorning:, Noon:, Night:\nQuantity:");

                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private String init_time(Calendar calendar, SimpleDateFormat time_12hr, int hour) {

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, 0);

        return time_12hr.format(calendar.getTime());
    }

    private AlertDialog prep_dialogue_dose_prescription() {

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

        meds_dose_pres_dialog.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return select_med_pres;

    }

    private TimePickerDialog prep_dialogue_time_picker(int hour) {

        final Calendar calendar = Calendar.getInstance();
        alarm_minutes = calendar.get(Calendar.MINUTE);
        alarm_hour = calendar.get(Calendar.HOUR_OF_DAY);

        final TimePickerDialog select_time_dialog = new TimePickerDialog(AddMedsActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int picked_hour, int picked_minute) {
                //alarm_hour = picked_hour;
                //alarm_minutes = picked_minute;

                SimpleDateFormat std_time = new SimpleDateFormat(Constants.time_std);
                //dtask_time = picked_hour+":"+picked_minute;

                /*try{

                    Date picked_time = std_time.parse(dtask_time);
                    vtask_time.setText(time_12hr.format(picked_time));

                }catch (java.text.ParseException exp){
                    Log.d("DayMan:Exception","Time Parsing exception - "+exp);
                }*/
            }
        }, hour, 0, false);

        return select_time_dialog;
    }

    private AlertDialog prep_dialogue_dose_freq() {

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
                            //db_meds_dose_freq = Constants.task_repeat_no;
                        }
                    });

                    AlertDialog weekday = selected_weekdays_dialog.create();
                    weekday.show();
                }

                select_dose_freq.dismiss();
            }
        });

        return select_dose_freq;
    }
}
