package com.atta.oncs.model;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.atta.oncs.R;
import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UrlImagesAdapter extends RecyclerView.Adapter<UrlImagesAdapter.MyViewHolder> {

    private Context mContext;

    private List<String> urls;


    public UrlImagesAdapter(Context mContext, List<String> urls) {
        this.mContext = mContext;
        this.urls = urls;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.images_list_item, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        final String url = urls.get(i) ;

        if (!url.isEmpty()) {
            Picasso.get()
                    .load(url)
                    .resize(120, 120)
                    .centerCrop()
                    .into(myViewHolder.imageView);


            ImagePopup imagePopup = new ImagePopup(mContext);
            imagePopup.setWindowHeight(800); // Optional
            imagePopup.setWindowWidth(800); // Optional
            imagePopup.setBackgroundColor(Color.BLACK);  // Optional
            imagePopup.setFullScreen(true); // Optional
            imagePopup.setHideCloseIcon(true);  // Optional
            imagePopup.setImageOnClickClose(true);
            imagePopup.initiatePopupWithPicasso(url);

            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Intent intent = new Intent(mContext, PropertyDetailsActivity.class);
                    imagePopup.viewPopup();
                }
            });
        }



    }


    @Override
    public int getItemCount() {
        return urls.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.images_recycler_item);
        }
    }
}
