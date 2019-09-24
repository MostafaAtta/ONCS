package com.atta.oncs.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.atta.oncs.R;
import com.atta.oncs.contracts.HomeContract;
import com.atta.oncs.model.APIUrl;
import com.atta.oncs.model.Category;
import com.atta.oncs.model.CategoryAdapter;
import com.atta.oncs.model.FacilityAdapter;
import com.atta.oncs.model.Region;
import com.atta.oncs.model.SessionManager;
import com.atta.oncs.presenter.HomePresenter;
import com.google.android.material.tabs.TabLayout;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment implements HomeContract.View, AdapterView.OnItemSelectedListener {

    public static final String ACTION_UPDATE_FRAGMENT = "action_location_updated";

    private RecyclerView recyclerView;

    private ArrayList<Category> mCategories;

    private CategoryAdapter categoryAdapter;

    private FacilityAdapter facilityAdapter;

    private HomePresenter homePresenter;

    private Spinner regionsSpinner;

    private ArrayAdapter<String> regionsAdapter;

    private ArrayList<Region> mRegions;

    private ArrayList<String> regionsName;

    private int selectedRegion, selectedTab;

    private SwipeRefreshLayout swipeRefreshLayout;

    private ProgressDialog progressDialog;

    private SliderLayout sliderLayout;

    private TabLayout tabLayout;

    //private ViewPager viewPager;

    //private ViewFragmentPagerAdapter adapter;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = root.findViewById(R.id.recyclerView);

        regionsSpinner = root.findViewById(R.id.regions);


        sliderLayout = root.findViewById(R.id.slider);
        sliderLayout.setIndicatorAnimation(SliderLayout.Animations.FILL);
        sliderLayout.setScrollTimeInSec(3);


        homePresenter = new HomePresenter(this);


        // Find the view pager that will allow the user to swipe between fragments
        //viewPager = root.findViewById(R.id.viewpager);

        //viewPager.setRotationY(180);
        // Create an adapter that knows which fragment should be shown on each page
        //adapter = new ViewFragmentPagerAdapter(getActivity().getSupportFragmentManager(), getContext());

        // Set the adapter onto the view pager
        //viewPager.setAdapter(adapter);

        tabLayout = root.findViewById(R.id.sliding_tabs);

        // Connect the tab layout with the view pager. This will
        //   1. Update the tab layout when the view pager is swiped
        //   2. Update the view pager when a tab is selected
        //   3. Set the tab layout's tab names with the view pager's adapter's titles
        //      by calling onPageTitle()
        //tabLayout.setupWithViewPager(viewPager);

        String[] titles = getContext().getResources().getStringArray(R.array.categories);

        List<String> mTitles = Arrays.asList(titles);

        tabLayout.addTab(tabLayout.newTab().setText(mTitles.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(mTitles.get(1)));
        tabLayout.addTab(tabLayout.newTab().setText(mTitles.get(2)));
        getRegions(1);
        selectedTab = 1;
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                getCategories(tab.getPosition() + 1);
                selectedTab = tab.getPosition() + 1;
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

                if (tab.getPosition() != selectedTab-1) {
                    getCategories(tab.getPosition() + 1);
                    selectedTab = tab.getPosition() + 1;
                }
            }
        });


        swipeRefreshLayout = root.findViewById(R.id.swipe);

        swipeRefreshLayout.setOnRefreshListener(
                () -> {

                    getCategories(selectedTab);

                    swipeRefreshLayout.setRefreshing(false);
                }
        );

        //getActivity().sendBroadcast(new Intent(ACTION_UPDATE_FRAGMENT));
        return root;
    }

    private void getCategories(int position) {


        if (isInternetAvailable()) {

            if (mCategories != null) {
                mCategories.clear();
            }
            showProgress();
            homePresenter.getCategories(SessionManager.getInstance(getContext()).getOrderRegionId(), position);




        }else {
            showMessage("check your internet connection");
        }
    }

    private void getRegions(int position) {


        if (isInternetAvailable()) {

            setFlipperView();
            if (mCategories != null) {
                mCategories.clear();
            }
            showProgress();
            homePresenter.getRegions(SessionManager.getInstance(getContext()).getOrderRegionId(), position);




        }else {
            showMessage("check your internet connection");
        }
    }


    @Override
    public void showMessage(String error) {

        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showRecyclerView(ArrayList<Category> categories) {


        mCategories = categories;
        switch (selectedTab){
            case 1:
            case 2:
                categoryAdapter = new CategoryAdapter(getContext(), getActivity(), mCategories, selectedTab);
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
                recyclerView.setAdapter(categoryAdapter);

                break;

            case 3:
                facilityAdapter = new FacilityAdapter(getContext(), getActivity(), mCategories, selectedTab);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(facilityAdapter);

                break;
        }
        hideProgress();
    }


    @Override
    public void setSpinner(ArrayList<Region> regions) {

        mRegions = regions;

        regionsName = new ArrayList<>();

        for (Region region : mRegions) {
            regionsName.add(region.toString());
        }

        regionsAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, regionsName);
        regionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        regionsSpinner.setAdapter(regionsAdapter);
        regionsSpinner.setOnItemSelectedListener(this);

        if (!SessionManager.getInstance(getContext()).getUserRegionName().isEmpty()){

            int selectedItem = regionsName.indexOf(SessionManager.getInstance(getContext()).getUserRegionName());

            regionsSpinner.setSelection(selectedItem);
            SessionManager.getInstance(getContext()).setOrderregionId(mRegions.get(selectedItem).getId());
        }else {

            if (SessionManager.getInstance(getContext()).getUserRegionId() != 0) {
                homePresenter.getUserRegion(SessionManager.getInstance(getContext()).getUserRegionId());

            }
        }
    }

    public void updateUserRegion(String regionName){

        int selectedItem = regionsName.indexOf(regionName);

        regionsSpinner.setSelection(selectedItem);


        SessionManager.getInstance(getContext()).setOrderregionId(mRegions.get(selectedItem).getId());

        SessionManager.getInstance(getContext()).setUserRegionName(regionName);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

        selectedRegion = mRegions.get(position).getId();


        SessionManager.getInstance(getContext()).setOrderregionId(selectedRegion);

        homePresenter.getCategories(SessionManager.getInstance(getContext()).getOrderRegionId(), selectedTab);

        //getActivity().sendBroadcast(new Intent(ACTION_UPDATE_FRAGMENT));

        //adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();

        //adapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

        getActivity().sendBroadcast(new Intent(ACTION_UPDATE_FRAGMENT));
    }

    @Override
    public void showProgress() {

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }



    @Override
    public void setFlipperView() {


        String[] urls = new String[]{
                APIUrl.Images_BASE_URL + "/food.jpeg",
                APIUrl.Images_BASE_URL + "/pizza.jpg",
                APIUrl.Images_BASE_URL + "/fruits.jpg",
                APIUrl.Images_BASE_URL + "/candy.jpg"

        };

        String[] tags = new String[]{
                "Food",
                "Pizza",
                "Fruits",
                "Candy"
        };

        for (int i = 0; i <= 3; i++) {

            SliderView sliderView = new SliderView(getContext());


            sliderView.setImageUrl(urls[i]);

            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
            sliderView.setDescription(tags[i]);
            final int finalI = i;
            sliderView.setOnSliderClickListener(sliderView1 ->
                    Toast.makeText(getContext(), "This is slider " + (finalI + 1), Toast.LENGTH_SHORT).show());

            //at last add this view in your layout :
            sliderLayout.addSliderView(sliderView);
        }


    }

    private boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

}