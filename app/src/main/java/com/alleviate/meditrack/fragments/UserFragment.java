package com.alleviate.meditrack.fragments;


import android.app.AlertDialog;
import android.content.Intent;
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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alleviate.meditrack.AddMedsActivity;
import com.alleviate.meditrack.DebugActivity;
import com.alleviate.meditrack.R;
import com.alleviate.meditrack.constants.Constants;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {


    public UserFragment() {
        // Required empty public constructor
    }

    String user_gender = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View frag_view = inflater.inflate(R.layout.fragment_user, container, false);

        set_user_details(frag_view);

        RelativeLayout cv_user = (RelativeLayout) frag_view.findViewById(R.id.rl_user_details);

        final View temp_view = frag_view;

        cv_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                build_user_detail_dialogue(temp_view).show();

            }
        });

        RelativeLayout rl_sos_settings = (RelativeLayout) frag_view.findViewById(R.id.sos_settings);
        rl_sos_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                build_sos_detail_dialogue(temp_view).show();
            }
        });

        RelativeLayout rl_add = (RelativeLayout) frag_view.findViewById(R.id.add_medicine);
        rl_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent add_in = new Intent(getActivity(), AddMedsActivity.class);
                getActivity().startActivity(add_in);

            }
        });

        Button debug_btn = (Button) frag_view.findViewById(R.id.button_debug_info);
        if (Constants.debug_flag) {
            debug_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(getActivity(), DebugActivity.class);
                    getActivity().startActivity(in);
                }
            });
        } else {
            debug_btn.setVisibility(View.INVISIBLE);
        }

        return frag_view;
    }

    private AlertDialog build_sos_detail_dialogue(final View temp_view) {

        final AlertDialog.Builder user_details_adapter = new AlertDialog.Builder(getActivity());
        user_details_adapter.setCancelable(true);

        LayoutInflater dialog_inflater_pres = getActivity().getLayoutInflater();
        final View dialog_view_pres = dialog_inflater_pres.inflate(R.layout.dialog_layout_sos_detail, null );

        TextView dialog_title_pres = (TextView) dialog_view_pres.findViewById(R.id.title);
        dialog_title_pres.setText(R.string.edit_user_details);

        user_details_adapter.setView(dialog_view_pres);

        final AlertDialog select_med_pres = user_details_adapter.create();

        Button save_user = (Button) dialog_view_pres.findViewById(R.id.save_user);
        Button save_cancel = (Button) dialog_view_pres.findViewById(R.id.cancel);
        final EditText et_sos_name = (EditText) dialog_view_pres.findViewById(R.id.contact_name);
        final EditText et_sos_number = (EditText) dialog_view_pres.findViewById(R.id.contact_number);

        SharedPreferences parent_alarm_sp = getActivity().getSharedPreferences(Constants.sp_medi_file, MODE_PRIVATE);
        et_sos_name.setText(parent_alarm_sp.getString(Constants.sp_sos_name,""));
        et_sos_number.setText(parent_alarm_sp.getString(Constants.sp_sos_number, ""));

        save_user.setOnClickListener(new View.OnClickListener() {
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

                    set_user_details(temp_view);

                    select_med_pres.dismiss();
                }
            }
        });

        save_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_med_pres.dismiss();
            }
        });

        return select_med_pres;
    }

    private AlertDialog build_user_detail_dialogue(final View temp_view) {

        final AlertDialog.Builder user_details_adapter = new AlertDialog.Builder(getActivity());
        user_details_adapter.setCancelable(true);

        LayoutInflater dialog_inflater_pres = getActivity().getLayoutInflater();
        final View dialog_view_pres = dialog_inflater_pres.inflate(R.layout.dialog_layout_user_detail, null );

        TextView dialog_title_pres = (TextView) dialog_view_pres.findViewById(R.id.title);
        dialog_title_pres.setText(R.string.edit_user_details);

        user_details_adapter.setView(dialog_view_pres);

        final AlertDialog select_med_pres = user_details_adapter.create();

        Button save_user = (Button) dialog_view_pres.findViewById(R.id.save_user);
        Button save_cancel = (Button) dialog_view_pres.findViewById(R.id.cancel);
        final EditText et_user_name = (EditText) dialog_view_pres.findViewById(R.id.user_name);
        final EditText et_user_age = (EditText) dialog_view_pres.findViewById(R.id.user_age);
        Spinner sp_user_gender = (Spinner) dialog_view_pres.findViewById(R.id.user_gender);

        SharedPreferences parent_alarm_sp = getActivity().getSharedPreferences(Constants.sp_medi_file, MODE_PRIVATE);
        et_user_name.setText(parent_alarm_sp.getString(Constants.sp_user_name,""));
        et_user_age.setText(parent_alarm_sp.getInt(Constants.sp_user_age,0)+"");
        user_gender = parent_alarm_sp.getString(Constants.sp_user_gender,"");

        ArrayAdapter gender_adapter = ArrayAdapter.createFromResource(getActivity(), R.array.gender, R.layout.spinner_layout);
        sp_user_gender.setAdapter(gender_adapter);

        switch (user_gender) {
            case "Female":
                sp_user_gender.setSelection(0);
                break;
            case "Male":
                sp_user_gender.setSelection(1);
                break;
            case "Others":
                sp_user_gender.setSelection(3);
                break;
        }

        sp_user_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                user_gender = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                SharedPreferences parent_alarm_sp = getActivity().getSharedPreferences(Constants.sp_medi_file, MODE_PRIVATE);
                user_gender = parent_alarm_sp.getString(Constants.sp_user_gender,"");
            }
        });

        save_user.setOnClickListener(new View.OnClickListener() {
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

                    select_med_pres.dismiss();

                    set_user_details(temp_view);

                }
            }
        });

        save_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_med_pres.dismiss();
            }
        });

        return select_med_pres;
    }

    private void set_user_details(View frag_view) {

        SharedPreferences parent_alarm_sp = getActivity().getSharedPreferences(Constants.sp_medi_file, MODE_PRIVATE);

        TextView tv_user_name = (TextView) frag_view.findViewById(R.id.user_name);
        TextView tv_user_gender = (TextView) frag_view.findViewById(R.id.user_gender);
        TextView tv_user_age = (TextView) frag_view.findViewById(R.id.user_age);

        tv_user_name.setText(parent_alarm_sp.getString(Constants.sp_user_name,"John Doe"));
        tv_user_gender.setText(parent_alarm_sp.getString(Constants.sp_user_gender,"Male"));
        tv_user_age.setText(parent_alarm_sp.getInt(Constants.sp_user_age, 0) + " years");

        TextView tv_sos_contact = (TextView) frag_view.findViewById(R.id.sos_contact);

        tv_sos_contact.setText(parent_alarm_sp.getString(Constants.sp_sos_name, "Jane Doe"));

    }

}
