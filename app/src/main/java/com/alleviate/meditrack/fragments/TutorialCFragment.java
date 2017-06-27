package com.alleviate.meditrack.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alleviate.meditrack.R;
import com.alleviate.meditrack.constants.Constants;

import static android.content.Context.MODE_PRIVATE;


public class TutorialCFragment extends Fragment {

    public TutorialCFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View frag_view = inflater.inflate(R.layout.fragment_tutorial_c, container, false);

        final EditText et_sos_name = (EditText) frag_view.findViewById(R.id.contact_name);
        final EditText et_sos_number = (EditText) frag_view.findViewById(R.id.contact_number);

        Button add_contact = (Button) frag_view.findViewById(R.id.save_sos);
        add_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sos_name = et_sos_name.getText().toString();
                String sos_num = et_sos_number.getText().toString();

                if (sos_name.equals("") || sos_num.equals("")) {

                    Toast.makeText(getContext(),"Please fill the SOS details.",Toast.LENGTH_SHORT).show();

                } else {

                    SharedPreferences parent_alarm_sp = getActivity().getSharedPreferences(Constants.sp_medi_file, MODE_PRIVATE);
                    SharedPreferences.Editor sp_user_edit = parent_alarm_sp.edit();

                    sp_user_edit.putString(Constants.sp_sos_name, sos_name);
                    sp_user_edit.putString(Constants.sp_sos_number, sos_num);

                    sp_user_edit.commit();

                    getActivity().finish();
                }

            }
        });



        return frag_view;
    }
}
