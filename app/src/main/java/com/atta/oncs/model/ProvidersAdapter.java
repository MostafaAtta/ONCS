package com.atta.oncs.model;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.atta.oncs.ProviderFragmentDirections;
import com.atta.oncs.R;

import java.util.List;

public class ProvidersAdapter extends RecyclerView.Adapter<ProvidersAdapter.MyViewHolder> {

    private Activity activity;

    private Context context;

    private List<Provider> providers;


    public ProvidersAdapter(Context context, Activity activity, List<Provider> providers) {
        this.context = context;
        this.activity = activity;
        this.providers = providers;
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
        final Provider provider = providers.get(position);

        myViewHolder.categoryName.setText(provider.getProviderName());
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
                        navigate(ProviderFragmentDirections.actionProviderFragmentToOrderFragment());

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

        return providers.size();
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
