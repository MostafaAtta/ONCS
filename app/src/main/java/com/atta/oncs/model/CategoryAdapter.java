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

import com.atta.oncs.R;
import com.atta.oncs.ui.home.HomeFragmentDirections;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    private Activity activity;

    private Context context;

    private List<Category> categories;


    public CategoryAdapter(Context context, Activity activity, List<Category> categories) {
        this.context = context;
        this.activity = activity;
        this.categories = categories;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_grid_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int position) {

        //TODO comment name and uncomment category
        final Category category = categories.get(position);


        myViewHolder.categoryName.setText(category.getName());

        Picasso.get()
                .load(category.getCategoryIcon())
                .resize(120, 120)
                .centerCrop()
                .into(myViewHolder.categoryIcon);

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int cId = category.getId();

                int rId = SessionManager.getInstance(context).getOrderRegionId();

                String categoryName= category.getName();


                Navigation.findNavController(activity, R.id.nav_host_fragment).
                        navigate(HomeFragmentDirections.actionNavHomeToProviderFragment2(rId, cId,  categoryName));

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

        ImageView categoryIcon;
        TextView categoryName;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryIcon = itemView.findViewById(R.id.flag);
            categoryName = itemView.findViewById(R.id.name);



        }
    }


}
