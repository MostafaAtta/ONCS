package com.atta.oncs;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atta.oncs.contracts.OrderContract;
import com.atta.oncs.model.ImagesAdapter;
import com.atta.oncs.model.SessionManager;
import com.atta.oncs.presenter.OrderPresenter;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class OrderFragment extends Fragment implements View.OnClickListener, OrderContract.View {

    private View root;

    private TextInputEditText orderText;

    private String order;

    private int pId;

    private TextView addImageTxt, addRecordTxt;
    private Chronometer chronometer;
    private ImageView imageViewRecord, imageViewPlay, imageViewStop, addImage;
    private ConstraintLayout recordLayout, imageLayout;
    private SeekBar seekBar;
    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;
    private String voiceFileName = null;
    private int lastProgress = 0;
    private Handler mHandler = new Handler();
    private int RECORD_AUDIO_REQUEST_CODE =123 ;
    private boolean isPlaying = false;

    private File voiceFile;
    private RequestBody voiceRequestBody;
    private MultipartBody.Part voiceFileUpload;

    private Button addBtn;

    private List<Bitmap> imagesBitmap;
    private List<String> imagesString, imagesName;

    private RecyclerView recyclerView;

    private OrderPresenter orderPresenter;

    private ProgressDialog progressDialog;


    ArrayList<File> files;

    ArrayList<RequestBody> requestBody;
    ArrayList<MultipartBody.Part> fileupload ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_order, container, false);

        pId = OrderFragmentArgs.fromBundle(getArguments()).getPId();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getPermissionToRecordAudio();
        }

        initViews();

        imagesBitmap = new ArrayList<>();
        imagesName = new ArrayList<>();
        imagesString = new ArrayList<>();



        orderPresenter = new OrderPresenter(this);

        return root;
    }


    private void initViews() {

        chronometer = root.findViewById(R.id.chronometerTimer);
        chronometer.setBase(SystemClock.elapsedRealtime());
        imageViewRecord = root.findViewById(R.id.imageViewRecord);
        imageViewStop = root.findViewById(R.id.imageViewStop);
        imageViewPlay = root.findViewById(R.id.imageViewPlay);
        seekBar = root.findViewById(R.id.seekBar);
        addImage = root.findViewById(R.id.add_image);
        recyclerView = root.findViewById(R.id.images_recycler);
        addImageTxt = root.findViewById(R.id.addImageTextView);
        addRecordTxt = root.findViewById(R.id.addRecordTextView);
        recordLayout = root.findViewById(R.id.recording_layout);
        imageLayout = root.findViewById(R.id.image_layout);
        addBtn = root.findViewById(R.id.add_order);

        orderText = root.findViewById(R.id.editText);

        imageViewRecord.setOnClickListener(this);
        imageViewStop.setOnClickListener(this);
        imageViewPlay.setOnClickListener(this);
        addImage.setOnClickListener(this);
        addImageTxt.setOnClickListener(this);
        addRecordTxt.setOnClickListener(this);
        addBtn.setOnClickListener(this);

        String addImageString = getResources().getString(R.string.add_image_to_order);
        SpannableString addImageContent = new SpannableString(addImageString);
        addImageContent.setSpan(new UnderlineSpan(), 0, addImageString.length(), 0);
        addImageTxt.setText(addImageContent);


        String addRecordString = getResources().getString(R.string.add_record_to_order);
        SpannableString addRecordContent = new SpannableString(addRecordString);
        addRecordContent.setSpan(new UnderlineSpan(), 0, addRecordString.length(), 0);
        addRecordTxt.setText(addRecordContent);

    }

    @Override
    public void onClick(View view) {

        if( view == imageViewRecord ){
            prepareForRecording();
            startRecording();
        }else if( view == imageViewStop ){
            prepareForStop();
            stopRecording();
        }else if( view == imageViewPlay ){
            if( !isPlaying && voiceFileName != null ){
                isPlaying = true;
                startPlaying();
            }else{
                isPlaying = false;
                stopPlaying();
            }
        }else if (view == addImage) {
            getImages2();
        }else if (view == addRecordTxt){
            recordLayout.setVisibility(View.VISIBLE);
        }else if (view == addImageTxt){
            imageLayout.setVisibility(View.VISIBLE);
        }else if (view == addBtn){
            order = orderText.getText().toString();
            if (validate()) {
                showProgress();
                orderPresenter.addOrder(SessionManager.getInstance(getContext()).getUserId(), pId, order, fileupload != null, voiceFileUpload != null,
                        voiceFileUpload, fileupload);
            }
        }

    }


    private boolean validate() {

        boolean valid = true;

        if(order.isEmpty()){

            orderText.setError("ادحل الطلب الخاص بك");
            orderText.requestFocus();
            valid = false;
        }else {
            orderText.setError(null);
        }



        return valid;
    }



    public void getImages2(){
        ImagePicker.create(this)
                .returnMode(ReturnMode.NONE) // set whether pick and / or camera action should return immediate result or not.
                .folderMode(true) // folder mode (false by default)
                .toolbarFolderTitle("Folder") // folder selection title
                .toolbarImageTitle("Tap to select") // image selection title
                .toolbarArrowColor(Color.BLACK) // Toolbar 'up' arrow color
                .multi() // multi mode (default mode)
                .limit(3) // max images can be selected (99 by default)
                .showCamera(true) // show camera or not (true by default)
                .enableLog(false) // disabling log
                .start();
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // Get a list of picked images
            List<Image> imageList = ImagePicker.getImages(data);

            files = new ArrayList<>();

            requestBody = new ArrayList<>();

            fileupload = new ArrayList<>();

            if (!imageList.isEmpty()|| imageList != null){


                if (imagesBitmap.size() == 3){
                    imagesBitmap.clear();
                    imagesName.clear();

                    files.clear();
                    requestBody.clear();
                    fileupload.clear();
                }
                for (int i = 0; i < imageList.size(); i++){
                    imagesBitmap.add(getBitmapFromPath(imageList.get(i).getPath()));
                    imagesName.add(imageList.get(i).getName());
                    getStringImage(getBitmapFromPath(imageList.get(i).getPath()));

                    String imgPath = imageList.get(i).getPath();
                    File currentFile = new File(imageList.get(i).getPath());

                    String filename = "filename" + (i+1);
                    files.add(currentFile);
                    requestBody.add(RequestBody.create(MediaType.parse("image/*"), files.get(i)));
                    fileupload.add(MultipartBody.Part.createFormData(filename, files.get(i).getName(), requestBody.get(i)));

                }

                if (imagesBitmap.size() == 3){
                    addImage.setVisibility(View.GONE);
                }
                ImagesAdapter myAdapter = new ImagesAdapter(getContext(), imagesBitmap);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                recyclerView.setAdapter(myAdapter);
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private Bitmap getBitmapFromPath(String filePath) {

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(filePath,bmOptions);
        return bitmap;

    }

    public String getStringImage(Bitmap bm){
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,ba);
        byte[] imageByte = ba.toByteArray();
        String encode = Base64.encodeToString(imageByte,Base64.DEFAULT);
        return encode;
    }



    private void prepareForStop() {
        //TransitionManager.beginDelayedTransition(linearLayoutRecorder);
        imageViewRecord.setVisibility(View.VISIBLE);
        imageViewStop.setVisibility(View.GONE);
        imageViewPlay.setVisibility(View.VISIBLE);
        seekBar.setVisibility(View.VISIBLE);
    }



    private void prepareForRecording() {
        //TransitionManager.beginDelayedTransition(linearLayoutRecorder);
        imageViewRecord.setVisibility(View.INVISIBLE);
        imageViewStop.setVisibility(View.VISIBLE);
        imageViewPlay.setVisibility(View.VISIBLE);
        seekBar.setVisibility(View.VISIBLE);
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
        chronometer.stop();

    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);


        File root = android.os.Environment.getExternalStorageDirectory();
        File file = new File(root.getAbsolutePath() + "/ONCS/Audios");
        if (!file.exists()) {
            file.mkdirs();
        }

        voiceFileName =  root.getAbsolutePath() + "/ONCS/Audios/" + String.valueOf(System.currentTimeMillis() + ".3gp");
        Log.d("filename", voiceFileName);


        mRecorder.setOutputFile(voiceFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        try {
            mRecorder.prepare();
            mRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        lastProgress = 0;
        seekBar.setProgress(0);
        stopPlaying();
        // making the imageview a stop button
        //starting the chronometer
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }


    private void stopRecording() {

        try{
            mRecorder.stop();
            mRecorder.release();
        }catch (Exception e){
            e.printStackTrace();
        }
        mRecorder = null;
        //starting the chronometer
        chronometer.stop();
        chronometer.setBase(SystemClock.elapsedRealtime());
        //showing the play button
        Toast.makeText(getContext(), "Recording saved successfully.", Toast.LENGTH_SHORT).show();




        voiceFile = new File(voiceFileName);
        voiceRequestBody = RequestBody.create(MediaType.parse("audio/*"), voiceFile);
        voiceFileUpload = MultipartBody.Part.createFormData("filevoicename", voiceFile.getName(), voiceRequestBody);

    }


    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(voiceFileName);
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
        seekUpdation();
        chronometer.start();


        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                imageViewPlay.setImageResource(R.drawable.ic_play);
                isPlaying = false;
                chronometer.stop();
            }
        });



        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if( mPlayer!=null && fromUser ){
                    mPlayer.seekTo(progress);
                    chronometer.setBase(SystemClock.elapsedRealtime() - mPlayer.getCurrentPosition());
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

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            seekUpdation();
        }
    };

    private void seekUpdation() {
        if(mPlayer != null){
            int mCurrentPosition = mPlayer.getCurrentPosition() ;
            seekBar.setProgress(mCurrentPosition);
            lastProgress = mCurrentPosition;
        }
        mHandler.postDelayed(runnable, 100);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getPermissionToRecordAudio() {
        // 1) Use the support library version ContextCompat.checkSelfPermission(...) to avoid
        // checking the build version since Context.checkSelfPermission(...) is only available
        // in Marshmallow
        // 2) Always check for permission (even if permission has already been granted)
        // since the user can revoke permissions at any time through Settings
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {

            // The permission is NOT already granted.
            // Check if the user has been asked about this permission already and denied
            // it. If so, we want to give more explanation about why the permission is needed.
            // Fire off an async request to actually get the permission
            // This will show the standard permission request dialog UI
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    RECORD_AUDIO_REQUEST_CODE);

        }
    }

    // Callback with the request from calling requestPermissions(...)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        // Make sure it's our original READ_CONTACTS request
        if (requestCode == RECORD_AUDIO_REQUEST_CODE) {
            if (grantResults.length == 3 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED){

                //Toast.makeText(this, "Record Audio permission granted", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(getContext(), "You must give permissions to use this app. App is exiting.", Toast.LENGTH_SHORT).show();
                getActivity().finishAffinity();
            }
        }

    }


    @Override
    public void showMessage(String error) {

        if (getContext().getPackageName() !=  null) {
            Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
        }
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
        progressDialog.dismiss();
    }


    @Override
    public void navigateToMain(){

        hideProgress();
        Intent intent = new Intent(getContext(), MainActivity.class);
        getActivity().startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }
}
