package com.alleviate.meditrack.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alleviate.meditrack.AddMedsActivity;
import com.alleviate.meditrack.DebugActivity;
import com.alleviate.meditrack.MainActivity;
import com.alleviate.meditrack.R;
import com.alleviate.meditrack.constants.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {


    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View frag_view = inflater.inflate(R.layout.fragment_user, container, false);

        CardView cv_user = (CardView) frag_view.findViewById(R.id.user_card);
        cv_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        RelativeLayout rl_sos_settings = (RelativeLayout) frag_view.findViewById(R.id.sos_settings);
        rl_sos_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

}
