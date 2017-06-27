package com.alleviate.meditrack.fragments;


import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alleviate.meditrack.R;
import com.alleviate.meditrack.adapter.TodayMedicineAdapter;
import com.alleviate.meditrack.db.SQLiteHelper;
import com.alleviate.meditrack.models.MedicineInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllFragment extends Fragment {

    private SearchView searchView = null;
    private RecyclerView.Adapter rvadpter;
    ArrayList<MedicineInfo> medicineInfos;
    RecyclerView rv;


    public AllFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View frag_view = inflater.inflate(R.layout.fragment_all, container, false);
        rv = (RecyclerView)frag_view.findViewById(R.id.all_view);
        rv.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        rv.setHasFixedSize(true);

        RecyclerView.LayoutManager rvlayoutmanager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(rvlayoutmanager);

        medicineInfos = new ArrayList<MedicineInfo>();

        SQLiteHelper db = new SQLiteHelper(getActivity());
        SQLiteDatabase dbr = db.getReadableDatabase();

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        queryBuilder.setTables(SQLiteHelper.db_alarms +" INNER JOIN "+ SQLiteHelper.db_med_db +" ON "+SQLiteHelper.db_alarm_med_id+" = "+SQLiteHelper.db_med_id);

        Cursor cursor = queryBuilder.query(dbr, null, null, null, null, null, null);

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
                return med1.getMedicine_name().compareTo(med2.getMedicine_name());
            }
        });

        rvadpter = new TodayMedicineAdapter(getActivity(), medicineInfos);
        rv.setAdapter(rvadpter);

        rv.setItemAnimator(new DefaultItemAnimator());

        return frag_view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.dashboard_menu, menu);

        MenuItem search_meds = menu.findItem(R.id.action_all_search);
        searchView = (SearchView) MenuItemCompat.getActionView(search_meds);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Toast.makeText(getActivity(),"Submit",Toast.LENGTH_SHORT).show();

                search_Meds(medicineInfos, query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Toast.makeText(getActivity(),"Change",Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        MenuItemCompat.setOnActionExpandListener(search_meds, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                rvadpter = new TodayMedicineAdapter(getActivity(), medicineInfos);
                rv.setAdapter(rvadpter);
                return true;
            }
        });


        super.onCreateOptionsMenu(menu, inflater);

    }

    private void search_Meds(ArrayList<MedicineInfo> medicineInfos, String query) {

        ArrayList <MedicineInfo> medicine_results = new ArrayList<MedicineInfo>();
        for(MedicineInfo medicine: medicineInfos){
            if (medicine.medicine_name.toLowerCase().contains(query.toLowerCase())){    //Ignore Case
                medicine_results.add(medicine);
            }
        }

        Toast.makeText(getActivity(),"Size:"+medicine_results.size()+" Query:"+query,Toast.LENGTH_SHORT).show();

        rvadpter = new TodayMedicineAdapter(getActivity(), medicine_results);
        rv.setAdapter(rvadpter);

    }


}
