package com.atta.oncs.model;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.atta.oncs.R;
import com.atta.oncs.ui.ProvidersFragmentDirections;

import java.util.List;

public class FacilitiesAdapter extends RecyclerView.Adapter<FacilitiesAdapter.MyViewHolder> {

    private Activity activity;

    private List<Facility> facilities;

    public FacilitiesAdapter(Activity activity, List<Facility> facilities) {

        this.activity = activity;
        this.facilities = facilities;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.providers_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int position) {


            //TODO comment name and uncomment category
            Facility facility = facilities.get(position);

            myViewHolder.categoryName.setText(facility.getName());
/*
        Picasso.get()
                .load(provider.getCategoryIcon())
                .resize(120, 120)
                .centerCrop()
                .into(myViewHolder.categoryIcon);*

                /
 */

            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Navigation.findNavController(activity, R.id.nav_host_fragment).
                            navigate(ProvidersFragmentDirections.actionProviderFragmentToFacilityFragment(facility));

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
            return facilities.size();

    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView categoryIcon;
        TextView categoryName;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryIcon = itemView.findViewById(R.id.flag);
            categoryName = itemView.findViewById(R.id.name);



        }
    }


}
