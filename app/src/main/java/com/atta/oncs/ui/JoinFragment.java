package com.atta.oncs.ui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.atta.oncs.R;
import com.atta.oncs.contracts.JoinContract;
import com.atta.oncs.model.Category;
import com.atta.oncs.model.Region;
import com.atta.oncs.presenter.JoinPresenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class JoinFragment extends Fragment implements AdapterView.OnItemSelectedListener, JoinContract.View {

    private JoinPresenter joinPresenter;

    private Spinner regionsSpinner;

    private ArrayAdapter<String> regionsAdapter;

    private ArrayList<Region> mRegions;

    private ArrayList<String> regionsname;

    private int selectedRegion;


    private Spinner typesSpinner;

    private ArrayAdapter<String> typesAdapter;

    private List<String> types;

    private int selectedType;

    private Spinner categoriesSpinner;

    private ArrayAdapter<String> categoriesAdapter;

    private ArrayList<Category> mCategories;

    private ArrayList<String> categoriesname;

    private int selectedCategory;


    private List<String> hours;

    private ArrayAdapter<String> hoursAdapter;

    private Spinner fromSpinner;

    private String openingTime;


    private Spinner toSpinner;

    private String closeingTime;

    private CheckBox checkBox24Hours;

    private ConstraintLayout workingHoursLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_join, container, false);

        typesSpinner = root.findViewById(R.id.type);
        categoriesSpinner = root.findViewById(R.id.categories);
        regionsSpinner = root.findViewById(R.id.regions);
        fromSpinner = root.findViewById(R.id.from);
        toSpinner = root.findViewById(R.id.to);
        workingHoursLayout = root.findViewById(R.id.working_hours_layout);
        checkBox24Hours = root.findViewById(R.id.checkBox);

        checkBox24Hours.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    workingHoursLayout.setVisibility(View.GONE);
                }else {

                    workingHoursLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        joinPresenter = new JoinPresenter(this);

        joinPresenter.getRegions();

        setTypesSpinner();

        setWorkingHoursSpinner();

        return root;
    }

    private void setTypesSpinner() {



        String[] typesArray = getContext().getResources().getStringArray(R.array.types);

        this.types = Arrays.asList(typesArray);

        typesAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, types);
        typesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typesSpinner.setAdapter(typesAdapter);
        typesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0){
                    joinPresenter.getCategorie(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void setWorkingHoursSpinner() {



        String[] hoursArray = getContext().getResources().getStringArray(R.array.working_hours);

        this.hours = Arrays.asList(hoursArray);

        hoursAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, hours);
        hoursAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(hoursAdapter);
        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                openingTime = hours.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        toSpinner.setAdapter(hoursAdapter);
        toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                closeingTime = hours.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void showMessage(String error) {

    }

    @Override
    public boolean validate(String mobileNumber) {
        return false;
    }

    @Override
    public void setRegionsSpinner(ArrayList<Region> regions) {

        mRegions = regions;

        regionsname = new ArrayList<>();
        regionsname.add("اختار المنطقة الخاصة بك");

        for (Region region : mRegions) {
            regionsname.add(region.toString());
        }

        regionsAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, regionsname);
        regionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        regionsSpinner.setAdapter(regionsAdapter);
        regionsSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void setCategoriesSpinner(ArrayList<Category> categories) {

        mCategories = categories;

        categoriesname = new ArrayList<>();
        categoriesname.add("اختار الفئة الخاصة بك");

        for (Category category : mCategories) {
            categoriesname.add(category.toString());
        }

        categoriesAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, categoriesname);
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoriesSpinner.setAdapter(categoriesAdapter);
        categoriesSpinner.setOnItemSelectedListener(this);
    }
}
