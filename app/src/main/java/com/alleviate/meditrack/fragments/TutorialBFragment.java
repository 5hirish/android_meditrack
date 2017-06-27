package com.alleviate.meditrack.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.alleviate.meditrack.R;
import com.alleviate.meditrack.TutorialActivity;
import com.alleviate.meditrack.constants.Constants;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class TutorialBFragment extends Fragment {

    public TutorialBFragment() {
        // Required empty public constructor
    }

    String user_gender;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View frag_view = inflater.inflate(R.layout.fragment_tutorial_b, container, false);

        final EditText et_user_name = (EditText) frag_view.findViewById(R.id.user_name);
        final EditText et_user_age = (EditText) frag_view.findViewById(R.id.user_age);
        Spinner sp_user_gender = (Spinner) frag_view.findViewById(R.id.user_gender);
        Button save_details = (Button) frag_view.findViewById(R.id.save_user);

        ArrayAdapter gender_adapter = ArrayAdapter.createFromResource(getActivity(), R.array.gender, android.R.layout.simple_spinner_item);
        sp_user_gender.setAdapter(gender_adapter);

        sp_user_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                user_gender = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                user_gender = parent.getItemAtPosition(0).toString();
            }
        });

        save_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_name = et_user_name.getText().toString();
                int user_age = 0;
                if (!et_user_age.getText().toString().equals("")) {
                    user_age = Integer.parseInt(et_user_age.getText().toString());
                }

                if (user_name.equals("") || user_age == 0) {

                    Toast.makeText(getContext(),"Your details cannot be empty!",Toast.LENGTH_SHORT).show();

                } else {

                    SharedPreferences parent_alarm_sp = getActivity().getSharedPreferences(Constants.sp_medi_file, MODE_PRIVATE);
                    SharedPreferences.Editor sp_user_edit = parent_alarm_sp.edit();

                    sp_user_edit.putString(Constants.sp_user_name, user_name);
                    sp_user_edit.putInt(Constants.sp_user_age, user_age);
                    sp_user_edit.putString(Constants.sp_user_gender, user_gender);

                    sp_user_edit.commit();

                    TutorialActivity.flipPage();
                }
            }
        });


        return frag_view;
    }

}
