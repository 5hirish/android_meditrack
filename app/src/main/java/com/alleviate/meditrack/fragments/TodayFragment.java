package com.alleviate.meditrack.fragments;

import android.content.Context;
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
import com.alleviate.meditrack.models.MedicineInfo;

import java.util.ArrayList;

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

        ArrayList<MedicineInfo> medicineInfos = new ArrayList<MedicineInfo>();

        for(int i = 0; i < 10; i ++){
            medicineInfos.add(new MedicineInfo("CP4-"+i, 1.0, "03:00"));
        }
        rvadpter = new TodayMedicineAdapter(getActivity(), medicineInfos);
        rv.setAdapter(rvadpter);

        rv.setItemAnimator(new DefaultItemAnimator());
        return frag_view;
    }
}
