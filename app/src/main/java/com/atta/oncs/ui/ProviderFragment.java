package com.atta.oncs.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.atta.oncs.R;
import com.atta.oncs.contracts.ProviderContract;
import com.atta.oncs.model.Order;
import com.atta.oncs.model.OrdersAdapter;
import com.atta.oncs.model.SessionManager;
import com.atta.oncs.presenter.ProviderPresenter;

import java.util.ArrayList;

public class ProviderFragment extends Fragment implements ProviderContract.View {


    private ProgressDialog progressDialog;

    private RecyclerView recyclerView;

    private ArrayList<Order> mOrders;

    private OrdersAdapter ordersAdapter;

    private ProviderPresenter providerPresenter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_provider, container, false);

        recyclerView = root.findViewById(R.id.order_list);

        providerPresenter = new ProviderPresenter(this);

        showProgress();
        providerPresenter.getOrders(SessionManager.getInstance(getContext()).getUserId());

        SwipeRefreshLayout swipeRefreshLayout = root.findViewById(R.id.order_refresh);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                showProgress();
                providerPresenter.getOrders(SessionManager.getInstance(getContext()).getUserId());

                swipeRefreshLayout.setRefreshing(false);
            }
        });
        return root;
    }


    @Override
    public void showMessage(String error) {


        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showRecyclerView(ArrayList<Order> orders) {

        mOrders = orders;

        ordersAdapter = new OrdersAdapter( getActivity(), mOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(ordersAdapter);

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