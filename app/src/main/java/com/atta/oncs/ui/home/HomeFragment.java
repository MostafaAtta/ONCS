package com.atta.oncs.ui.home;

import android.app.ProgressDialog;
import android.content.Context;
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
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.atta.oncs.R;
import com.atta.oncs.contracts.HomeContract;
import com.atta.oncs.model.APIUrl;
import com.atta.oncs.model.Category;
import com.atta.oncs.model.CategoryAdapter;
import com.atta.oncs.model.Region;
import com.atta.oncs.model.SessionManager;
import com.atta.oncs.presenter.HomePresenter;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements HomeContract.View, AdapterView.OnItemSelectedListener {

    private RecyclerView recyclerView;

    ArrayList<Category> mCategories;

    private CategoryAdapter categoryAdapter;

    private HomePresenter homePresenter;

    private Spinner regionsSpinner;

    private ArrayAdapter<String> regionsAdapter;

    private ArrayList<Region> mRegions;

    private ArrayList<String> regionsName;

    private int selectedRegion;

    private SwipeRefreshLayout swipeRefreshLayout;

    private ProgressDialog progressDialog;

    private SliderLayout sliderLayout;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = root.findViewById(R.id.recyclerView);

        regionsSpinner = root.findViewById(R.id.regions);


        sliderLayout = root.findViewById(R.id.slider);
        sliderLayout.setIndicatorAnimation(SliderLayout.Animations.FILL);
        sliderLayout.setScrollTimeInSec(3);


        homePresenter = new HomePresenter(this);


        if (isInternetAvailable()) {


            setFlipperView();

            if (mRegions == null || mCategories == null){


                showProgress();
                homePresenter.getRegions();

            }else {

                setSpinner(mRegions);
                showRecyclerView(mCategories);
            }


        }else {
            showMessage("check your internet connection");
        }

        swipeRefreshLayout = root.findViewById(R.id.swipe);

        swipeRefreshLayout.setOnRefreshListener(
                () -> {

                    if (isInternetAvailable()) {


                        showProgress();

                        homePresenter.getRegions();

                    }else {
                        showMessage("check your internet connection");
                    }

                    swipeRefreshLayout.setRefreshing(false);
                }
        );

        return root;
    }

    @Override
    public void showMessage(String error) {

        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showRecyclerView(ArrayList<Category> categories) {

        mCategories = categories;

        categoryAdapter = new CategoryAdapter(getContext(), getActivity(), mCategories);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        recyclerView.setAdapter(categoryAdapter);

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
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

        selectedRegion = mRegions.get(position).getId();


        SessionManager.getInstance(getContext()).setOrderregionId(selectedRegion);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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
        progressDialog.dismiss();
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