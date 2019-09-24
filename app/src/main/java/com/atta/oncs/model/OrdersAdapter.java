package com.atta.oncs.model;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.atta.oncs.R;
import com.atta.oncs.ui.ProviderFragmentDirections;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.MyViewHolder> {

    private Activity activity;

    private List<Order> orders;

    public OrdersAdapter(Activity activity, List<Order> orders) {

        this.activity = activity;
        this.orders = orders;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.orders_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int position) {

        //TODO comment name and uncomment category
        final Order order = orders.get(position);

        myViewHolder.orderId.setText(String.valueOf(order.getId()));
        myViewHolder.orderDate.setText(order.getOrderDate());
        //myViewHolder.orderDate.setText("2019/09/17 08:30");

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Navigation.findNavController(activity, R.id.nav_host_fragment).
                        navigate(ProviderFragmentDirections.actionNavProviderToOrderDetailsFragment(order, order.getId()));

            }
        });



    }

    @Override
    public int getItemCount() {
/*
        if (categories == null){

            Toast.makeText(mContext, "No Orders Found", Toast.LENGTH_SHORT).show();

            return  categories == null ? 0 : categories.size();

        }*/

        return orders.size();

    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView orderId;
        TextView orderDate;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            orderDate = itemView.findViewById(R.id.order_date);
            orderId = itemView.findViewById(R.id.order_id);



        }
    }


}
