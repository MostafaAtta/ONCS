package com.atta.oncs.model;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.atta.oncs.R;
import com.atta.oncs.ui.HomeFragmentDirections;

import java.util.List;

public class FacilityAdapter extends RecyclerView.Adapter<FacilityAdapter.MyViewHolder> {

    private Activity activity;

    private Context context;

    private List<Category> categories;

    private int index;

    public FacilityAdapter(Context context, Activity activity, List<Category> categories, int index) {
        this.context = context;
        this.activity = activity;
        this.categories = categories;
        this.index = index;
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
        final Category category = categories.get(position);

            myViewHolder.categoryName.setText(category.getName());


            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int cId = category.getId();

                    int rId = SessionManager.getInstance(context).getOrderRegionId();

                    String categoryName = category.getName();


                    Navigation.findNavController(activity, R.id.nav_host_fragment).
                            navigate(HomeFragmentDirections.actionNavHomeToProviderFragment2(rId, cId, index, categoryName));

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

        return categories.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView categoryName;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryName = itemView.findViewById(R.id.name);



        }
    }


}
