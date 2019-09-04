package com.atta.oncs.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atta.oncs.R;
import com.atta.oncs.contracts.HomeContract;
import com.atta.oncs.model.Category;
import com.atta.oncs.model.CategoryAdapter;
import com.atta.oncs.model.Region;
import com.atta.oncs.model.SessionManager;
import com.atta.oncs.presenter.HomePresenter;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements HomeContract.View, AdapterView.OnItemSelectedListener {

    RecyclerView recyclerView;

    CategoryAdapter categoryAdapter;

    HomePresenter homePresenter;

    Spinner regionsSpinner;

    ArrayAdapter<String> regionsAdapter;

    ArrayList<Region> mRegions;

    ArrayList<String> regionsname;

    int selectedRegion;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = root.findViewById(R.id.recyclerView);

        regionsSpinner = root.findViewById(R.id.regions);

        homePresenter = new HomePresenter(this);

        homePresenter.getRegions();

        return root;
    }

    @Override
    public void showMessage(String error) {

    }

    @Override
    public void showRecyclerView(ArrayList<Category> categories) {

        categoryAdapter = new CategoryAdapter(getContext(), categories);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(categoryAdapter);
    }


    @Override
    public void setSpinner(ArrayList<Region> regions) {

        mRegions = regions;

        regionsname = new ArrayList<>();

        for (Region region : mRegions) {
            regionsname.add(region.toString());
        }

        regionsAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, regionsname);
        regionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        regionsSpinner.setAdapter(regionsAdapter);
        regionsSpinner.setOnItemSelectedListener(this);

        if (!SessionManager.getInstance(getContext()).getUserRegionName().isEmpty()){

            int selectedItem = regionsname.indexOf(SessionManager.getInstance(getContext()).getUserRegionName());

            regionsSpinner.setSelection(selectedItem);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

        if (position != 0){

            selectedRegion = mRegions.get(position ).getId();

        }else {
            selectedRegion = 0;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}