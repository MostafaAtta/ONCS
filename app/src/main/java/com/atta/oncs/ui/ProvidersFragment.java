package com.atta.oncs.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.atta.oncs.R;
import com.atta.oncs.contracts.ProvidersContract;
import com.atta.oncs.model.FacilitiesAdapter;
import com.atta.oncs.model.Facility;
import com.atta.oncs.model.Provider;
import com.atta.oncs.model.ProvidersAdapter;
import com.atta.oncs.presenter.ProvidersPresenter;

import java.util.ArrayList;


public class ProvidersFragment extends Fragment implements ProvidersContract.View {

    int regionId, categoryId, index;

    String categoryName;

    ProgressDialog progressDialog;

    private RecyclerView recyclerView;

    ArrayList<Provider> mProviders;

    ArrayList<Facility> mFacilities;

    private ProvidersAdapter providersAdapter;

    private FacilitiesAdapter facilitiesAdapter;

    private ProvidersPresenter providersPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_providers,container,false);

        regionId = ProvidersFragmentArgs.fromBundle(getArguments()).getRId();
        categoryId = ProvidersFragmentArgs.fromBundle(getArguments()).getCId();
        categoryName = ProvidersFragmentArgs.fromBundle(getArguments()).getCategoryName();
        index = ProvidersFragmentArgs.fromBundle(getArguments()).getIndex();

        recyclerView = root.findViewById(R.id.providers_recyclerView);

        providersPresenter = new ProvidersPresenter(this);

        showProgress();


        switch (index){
            case 1:
            case 2:

                providersPresenter.getProviders(categoryId, regionId);
                break;
            case 3:

                providersPresenter.getFacilities(categoryId, regionId);
                break;


        }
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This callback will only be called when MyFragment is at least Started.


        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(ProvidersFragmentDirections.actionProviderFragmentToNavHome());
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        // The callback can be enabled or disabled here or in handleOnBackPressed()
    }

    public void onResume(){
        super.onResume();

        // Set title bar
        ((MainActivity) getActivity())
                .setActionBarTitle(categoryName);

    }
/*

    public boolean onBackPressed() {

        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(ProviderFragmentDirections.actionProviderFragmentToNavHome());
        return true;
    }
*/

    @Override
    public void showMessage(String error) {


        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showRecyclerView(ArrayList<Provider> providers) {

        mProviders = providers;

        providersAdapter = new ProvidersAdapter( getActivity(), mProviders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(providersAdapter);

        hideProgress();
    }

    @Override
    public void showFacilityRecyclerView(ArrayList<Facility> facilities) {

        mFacilities = facilities;

        facilitiesAdapter = new FacilitiesAdapter(getActivity(), mFacilities);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(facilitiesAdapter);

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
