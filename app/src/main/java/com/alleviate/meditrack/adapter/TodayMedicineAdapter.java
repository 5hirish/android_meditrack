package com.alleviate.meditrack.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alleviate.meditrack.R;
import com.alleviate.meditrack.constants.Constants;
import com.alleviate.meditrack.models.MedicineInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Shirish Kadam on 23/6/17.
 * Logged in as felix.
 * www.shirishkadam.com
 */

public class TodayMedicineAdapter extends RecyclerView.Adapter<TodayMedicineAdapter.ViewHolder> {

    ArrayList<MedicineInfo> medicineInfos;
    Context activity;

    public TodayMedicineAdapter(Context activity, ArrayList<MedicineInfo> medicineInfos) {
        this.activity = activity;
        this.medicineInfos = medicineInfos;
    }

    @Override
    public TodayMedicineAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medicine_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TodayMedicineAdapter.ViewHolder holder, int position) {

        double dosage = medicineInfos.get(position).medicine_dose;
        String str_dose = "", str_time = "";

        if (dosage == 0.5) {
            str_dose = "1/2 tablet";

        } else if (dosage == 1.0) {
            str_dose = "1 tablet";

        } else if (dosage == 1.5) {
            str_dose = "1 & 1/2 tablets";

        } else if (dosage == 2.0) {
            str_dose = "2 tablets";

        } else {
            str_dose = "1 tablet";
        }

        SimpleDateFormat sdf = new SimpleDateFormat(Constants.time_12hr);
        SimpleDateFormat sdf_std = new SimpleDateFormat(Constants.time_std);
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sdf_std.parse(medicineInfos.get(position).medicine_remind_time));
            str_time = sdf.format(cal.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        String meds_session = medicineInfos.get(position).medicine_session;

        ((TextView) holder.itemView.findViewById(R.id.medicine_name)).setText(medicineInfos.get(position).medicine_name);
        ((TextView) holder.itemView.findViewById(R.id.medicine_time)).setText(meds_session+": "+str_time);
        ((TextView) holder.itemView.findViewById(R.id.medicine_dose)).setText(str_dose);

        ((RelativeLayout) holder.itemView.findViewById(R.id.medicine_layout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return medicineInfos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
