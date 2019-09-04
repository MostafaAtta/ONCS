package com.atta.oncs.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.atta.oncs.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    private Context mContext;

    private List<Category> categories;


    public CategoryAdapter(Context mContext, List<Category> categories) {
        this.mContext = mContext;
        this.categories = categories;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int position) {

        //TODO comment name and uncomment category
        final Category category = categories.get(position);


        myViewHolder.categoryName.setText(category.getName());
        //myViewHolder.categoryIcon.setImageResource(category.getImage());


        myViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                /*Intent intent = new Intent(mContext, CurrencyConverterActivity.class);
                intent.putExtra("category", category);
                mContext.startActivity(intent);*/


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
        LinearLayout linearLayout;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryIcon = itemView.findViewById(R.id.flag);
            categoryName = itemView.findViewById(R.id.name);
            linearLayout = itemView.findViewById(R.id.linear);



        }
    }


}
