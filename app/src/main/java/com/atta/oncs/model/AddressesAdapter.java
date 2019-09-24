package com.atta.oncs.model;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.atta.oncs.ui.NewAddressActivity;
import com.atta.oncs.R;

import java.util.List;

public class AddressesAdapter extends RecyclerView.Adapter<AddressesAdapter.MyViewHolder> {

    private Context mContext;

    private List<Address> addresses;

    private boolean inProfile;

    public AddressesAdapter(Context mContext, List<Address> properties, boolean inProfile) {
        this.mContext = mContext;
        this.addresses = properties;
        this.inProfile = inProfile;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView;

        if (inProfile){
            itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.profile_addresses_list_item, viewGroup, false);
        } else {
            itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.address_list_item, viewGroup, false);
        }

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        final Address address = addresses.get(i) ;

        final int id = address.getId();
        final String name = address.getAddressName();

        final String area = address.getArea();
        final String fullAddress = address.getFullAddress();

        myViewHolder.addressName.setText(name);
        if (!inProfile) {
            myViewHolder.fullAddress.setText(fullAddress);
            myViewHolder.area.setText(area);
        }

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NewAddressActivity.class);
                intent.putExtra("address", address);
                mContext.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return addresses.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView addressName, area, fullAddress;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            if(inProfile) {
                addressName = itemView.findViewById(R.id.address_name);
            }else {

                addressName = itemView.findViewById(R.id.address_name_txt);
                area = itemView.findViewById(R.id.area_txt);
                fullAddress = itemView.findViewById(R.id.full_address_txt);
            }
        }
    }

    public void removeItem(int position) {
        addresses.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(Address item, int position) {
        addresses.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }


    public List<Address> getList(){
        return addresses;
    }
}
