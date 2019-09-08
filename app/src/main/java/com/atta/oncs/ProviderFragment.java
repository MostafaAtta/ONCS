package com.atta.oncs;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.atta.oncs.contracts.ProvidersContract;
import com.atta.oncs.model.Provider;
import com.atta.oncs.model.ProvidersAdapter;
import com.atta.oncs.presenter.ProvidersPresenter;

import java.util.ArrayList;


public class ProviderFragment extends Fragment implements ProvidersContract.View {

    int regionId, categoryId;

    String categoryName;

    ProgressDialog progressDialog;

    private RecyclerView recyclerView;

    ArrayList<Provider> mProviders;

    private ProvidersAdapter providersAdapter;

    private ProvidersPresenter providersPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_provider,container,false);

        regionId = ProviderFragmentArgs.fromBundle(getArguments()).getRId();
        categoryId = ProviderFragmentArgs.fromBundle(getArguments()).getCId();
        categoryName = ProviderFragmentArgs.fromBundle(getArguments()).getCategoryName();

        recyclerView = root.findViewById(R.id.providers_recyclerView);

        providersPresenter = new ProvidersPresenter(this);

        showProgress();

        providersPresenter.getProviders(categoryId, regionId);

        SwipeRefreshLayout swipeRefreshLayout = root.findViewById(R.id.swipe_layout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                showProgress();

                providersPresenter.getProviders(categoryId, regionId);


                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return root;
    }

    public void onResume(){
        super.onResume();

        // Set title bar
        ((MainActivity) getActivity())
                .setActionBarTitle(categoryName);

    }

    @Override
    public void showMessage(String error) {


        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showRecyclerView(ArrayList<Provider> providers) {

        mProviders = providers;

        providersAdapter = new ProvidersAdapter(getContext(), getActivity(), mProviders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(providersAdapter);

        hideProgress();
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

        if (progressDialog != null)
        progressDialog.dismiss();
    }
}
