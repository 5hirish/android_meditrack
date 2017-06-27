package com.alleviate.meditrack.fragments;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alleviate.meditrack.R;
import com.alleviate.meditrack.adapter.TodayMedicineAdapter;
import com.alleviate.meditrack.constants.Constants;
import com.alleviate.meditrack.db.SQLiteHelper;
import com.alleviate.meditrack.models.MedicineInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

public class TodayFragment extends Fragment {

    private RecyclerView.Adapter rvadpter;

    public TodayFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View frag_view = inflater.inflate(R.layout.fragment_today, container, false);
        RecyclerView rv = (RecyclerView)frag_view.findViewById(R.id.today_view);
        rv.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        rv.setHasFixedSize(true);

        RecyclerView.LayoutManager rvlayoutmanager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(rvlayoutmanager);

        Calendar cal_today = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.date_std);
        String today_date = sdf.format(cal_today.getTime());

        ArrayList<MedicineInfo> medicineInfos = new ArrayList<MedicineInfo>();

        SQLiteHelper db = new SQLiteHelper(getActivity());
        SQLiteDatabase dbr = db.getReadableDatabase();

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        queryBuilder.setTables(SQLiteHelper.db_alarms +" INNER JOIN "+ SQLiteHelper.db_med_db +" ON "+SQLiteHelper.db_alarm_med_id+" = "+SQLiteHelper.db_med_id);

        Cursor cursor = queryBuilder.query(dbr, null, SQLiteHelper.db_alarms_date+" = ? ", new String[] {today_date}, null, null, null);

        //Toast.makeText(getActivity(),"Count "+cursor.getCount(),Toast.LENGTH_SHORT).show();

        if(cursor != null){
            while (cursor.moveToNext()){

                int med_id = cursor.getInt(cursor.getColumnIndex(SQLiteHelper.db_med_id));
                String med_name = cursor.getString(cursor.getColumnIndex(SQLiteHelper.db_med_name));
                double med_prescription = cursor.getDouble(cursor.getColumnIndex(SQLiteHelper.db_med_dose));
                String med_time = cursor.getString(cursor.getColumnIndex(SQLiteHelper.db_alarms_time));
                String alarm_session = cursor.getString(cursor.getColumnIndex(SQLiteHelper.db_alarms_session));

                medicineInfos.add(new MedicineInfo(med_name, med_prescription, med_time, alarm_session));

            }cursor.close();
        }

        dbr.close();
        db.close();

        Collections.sort(medicineInfos, new Comparator<MedicineInfo>() {
            @Override
            public int compare(MedicineInfo med1, MedicineInfo med2) {
                return med1.getMedicine_remind_time().compareTo(med2.getMedicine_remind_time());
            }
        });


        rvadpter = new TodayMedicineAdapter(getActivity(), medicineInfos);
        rv.setAdapter(rvadpter);

        rv.setItemAnimator(new DefaultItemAnimator());
        return frag_view;
    }
}
