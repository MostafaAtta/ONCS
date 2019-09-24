package com.atta.oncs.ui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.atta.oncs.R;
import com.atta.oncs.model.Facility;

/**
 * A simple {@link Fragment} subclass.
 */
public class FacilityFragment extends Fragment {



    Facility facility;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View root = inflater.inflate(R.layout.fragment_facility, container, false);



        facility = FacilityFragmentArgs.fromBundle(getArguments()).getFacility();

        return root;
    }

}
