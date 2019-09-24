package com.atta.oncs.ui;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.atta.oncs.R;
import com.atta.oncs.contracts.CategoryContract;
import com.atta.oncs.model.Category;
import com.atta.oncs.model.CategoryAdapter;
import com.atta.oncs.model.FacilityAdapter;
import com.atta.oncs.model.SessionManager;
import com.atta.oncs.presenter.CategoryPresenter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment implements CategoryContract.View {

    private RecyclerView recyclerView;

    private ArrayList<Category> mCategories;

    private CategoryAdapter categoryAdapter;

    private FacilityAdapter facilityAdapter;

    private CategoryPresenter categoryPresenter;

    private SwipeRefreshLayout swipeRefreshLayout;

    private ProgressDialog progressDialog;

    private int index;

    private BroadcastReceiver mReceiver;

    private IntentFilter filter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_category, container, false);

        recyclerView = root.findViewById(R.id.recyclerView);
        if (getArguments() != null) {

            index = getArguments().getInt("category");
        }

        categoryPresenter = new CategoryPresenter(this);




        if (isInternetAvailable()) {


            //if (mCategories == null){


                //showProgress();
                categoryPresenter.getCategories(SessionManager.getInstance(getContext()).getOrderRegionId(), index+1);


           /* }else {

                showRecyclerView(mCategories);
            }
*/

        }else {
            showMessage("check your internet connection");
        }

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if ("action_location_updated".equals(intent.getAction())) {
                    if (isInternetAvailable()) {

                        //showProgress();
                        categoryPresenter.getCategories(SessionManager.getInstance(getContext()).getOrderRegionId(), index+1);


                    }else {
                        showMessage("check your internet connection");
                    }
                }
            }
        };
        filter = new IntentFilter("action_location_updated");


        getActivity().registerReceiver(mReceiver, filter);

        swipeRefreshLayout = root.findViewById(R.id.swipe);

        swipeRefreshLayout.setOnRefreshListener(
                () -> {

                    if (isInternetAvailable()) {


                        //showProgress();

                        categoryPresenter.getCategories(SessionManager.getInstance(getContext()).getOrderRegionId(), index+1);

                    }else {
                        showMessage("check your internet connection");
                    }

                    swipeRefreshLayout.setRefreshing(false);
                }
        );


        return root;

    }


    @Override
    public void onPause() {
        try {
            if (mReceiver != null) {
                getContext().unregisterReceiver(mReceiver);
            }
        } catch (IllegalArgumentException e) {
            Log.i("category fragment","epicReciver is already unregistered");
            mReceiver = null;
        }

        super.onPause();
    }



    @Override
    public void showMessage(String error) {

        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showRecyclerView(ArrayList<Category> categories) {
        //hideProgress();
        mCategories = categories;


        switch (index){
            case 0:
            case 1:
                categoryAdapter = new CategoryAdapter(getContext(), getActivity(), mCategories, index);
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
                recyclerView.setAdapter(categoryAdapter);

                break;

            case 2:
                facilityAdapter = new FacilityAdapter(getContext(), getActivity(), mCategories, index);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(facilityAdapter);

                break;
        }



        recyclerView.setRotationY(180);

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



    private boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

}
