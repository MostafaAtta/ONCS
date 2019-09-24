package com.atta.oncs.ui;


import android.app.ProgressDialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atta.oncs.R;
import com.atta.oncs.contracts.OrderDetailsContract;
import com.atta.oncs.model.Order;
import com.atta.oncs.model.UrlImagesAdapter;
import com.atta.oncs.presenter.OrderDetailsPresenter;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderDetailsFragment extends Fragment implements View.OnClickListener, OrderDetailsContract.View {


    private Order order;

    private TextView orderIdTv, OrderDataTv, noImagesTv, noRecordTv;

    private TextInputEditText priceEditeText;

    private Button acceptBtn, declineBtn, commentBtn;

    private RecyclerView recyclerView;

    private ArrayList<String> images;

    private UrlImagesAdapter urlImagesAdapter;

    private SeekBar seekBar;
    private MediaPlayer mPlayer;
    private boolean isPlaying = false;
    private int lastProgress = 0;

    private ImageView imageViewPlay, imageViewStop;

    private View root;

    private OrderDetailsPresenter orderDetailsPresenter;

    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_order_details, container, false);


        orderDetailsPresenter = new OrderDetailsPresenter(this);

        order = OrderDetailsFragmentArgs.fromBundle(getArguments()).getOrder();

        initiateViews();
        if (order != null){

            setOrderDetails();
        }else {
            showProgress();
            orderDetailsPresenter.getOrder(OrderDetailsFragmentArgs.fromBundle(getArguments()).getOrderId());
        }

        return root;
    }

    private void initiateViews(){

        orderIdTv = root.findViewById(R.id.order_id_text);
        OrderDataTv = root.findViewById(R.id.order_text);
        priceEditeText = root.findViewById(R.id.price_tv);
        noImagesTv = root.findViewById(R.id.noImageTextView);
        noRecordTv = root.findViewById(R.id.no_recordTextView);

        recyclerView = root.findViewById(R.id.images_recycler);


        imageViewPlay = root.findViewById(R.id.imageViewPlay);
        seekBar = root.findViewById(R.id.seekBar);
        imageViewPlay.setOnClickListener(this);

        acceptBtn = root.findViewById(R.id.accept_button);
        declineBtn = root.findViewById(R.id.decline_button);
        commentBtn = root.findViewById(R.id.comment_button);

        acceptBtn.setOnClickListener(this);
        declineBtn.setOnClickListener(this);
        commentBtn.setOnClickListener(this);
    }

    private void setOrderDetails() {


        String orderId = getContext().getResources().getString(R.string.order_number) + " " + order.getId();

        orderIdTv.setText(orderId);

        OrderDataTv.setText(order.getRequestText());

        setImagesList();


        if (!order.getVoiceUrl().isEmpty()){
            imageViewPlay.setVisibility(View.VISIBLE);
            seekBar.setVisibility(View.VISIBLE);
            noRecordTv.setVisibility(View.GONE);
        }

    }

    private void setImagesList() {

        images = new ArrayList<>();
        for(int i = 1; i <= 3; i++){
            if (!order.getImageUrl(i).isEmpty()){
                images.add(order.getImageUrl(i));
            }
        }

        if (images.size() > 0){
            setImages();
        }
    }

    private void setImages() {
        noImagesTv.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);


        urlImagesAdapter = new UrlImagesAdapter( getActivity(), images);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(urlImagesAdapter);

    }

    @Override
    public void onClick(View view) {
        if( view == imageViewPlay ){
            if( !isPlaying && !order.getVoiceUrl().isEmpty() ){
                isPlaying = true;
                startPlaying();
            }else{
                isPlaying = false;
                stopPlaying();
            }
        }else if (view == acceptBtn){

            String price = priceEditeText.getText().toString();
            if (price.isEmpty()){
                priceEditeText.setError("برجاء ادخال اجمالي سعر الطلب");
                return;
            }

            orderDetailsPresenter.updateOrder(order.getId(), 1, "", price);
        }
    }


    private void stopPlaying() {
        try{
            mPlayer.release();
        }catch (Exception e){
            e.printStackTrace();
        }
        mPlayer = null;
        //showing the play button
        imageViewPlay.setImageResource(R.drawable.ic_play);
        //chronometer.stop();

    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mPlayer.setDataSource(order.getVoiceUrl());
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e("LOG_TAG", "prepare() failed");
        }
        //making the imageview pause button
        imageViewPlay.setImageResource(R.drawable.ic_pause);

        seekBar.setProgress(lastProgress);
        mPlayer.seekTo(lastProgress);
        seekBar.setMax(mPlayer.getDuration());
        seekUpdating();
        //chronometer.start();


        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                imageViewPlay.setImageResource(R.drawable.ic_play);
                isPlaying = false;
                //chronometer.stop();
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if( mPlayer!=null && fromUser ){
                    mPlayer.seekTo(progress);
                    //chronometer.setBase(SystemClock.elapsedRealtime() - mPlayer.getCurrentPosition());
                    lastProgress = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    private void seekUpdating() {
        if(mPlayer != null){
            int mCurrentPosition = mPlayer.getCurrentPosition() ;
            seekBar.setProgress(mCurrentPosition);
            lastProgress = mCurrentPosition;
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    public void showMessage(String error) {

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

        if (progressDialog != null){
            progressDialog.dismiss();
        }
    }

    @Override
    public void showOrder(Order order) {

        hideProgress();
        this.order = order;
        setOrderDetails();
    }

    @Override
    public void navigateToHome() {


        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).
                navigate(OrderDetailsFragmentDirections.actionOrderDetailsFragmentToNavProvider());
    }

    @Override
    public void updateOrder() {

    }
}
