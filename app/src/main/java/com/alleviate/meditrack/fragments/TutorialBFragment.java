package com.alleviate.meditrack.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alleviate.meditrack.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TutorialBFragment extends Fragment {


    public TutorialBFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tutorial_b, container, false);
    }

}
