package com.atta.oncs.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.atta.oncs.R;
import com.atta.oncs.contracts.RegisterContract;
import com.atta.oncs.model.SessionManager;
import com.atta.oncs.model.User;
import com.atta.oncs.presenter.RegisterPresenter;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RegisterFragment extends Fragment implements RegisterContract.View {


    private SessionManager sessionManager;
    private ImageView imageView;
    private ProgressDialog progressDialog;

    private TextInputEditText usernameText, emailText;

    private Button register;

    int regionId, userId;

    private String mobileNumber, username, email;

    private File file;

    private RequestBody requestBody;
    private MultipartBody.Part fileupload ;
    private RequestBody filename ;

    private RegisterPresenter registerPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_regitser,container,false);

        regionId = RegisterFragmentArgs.fromBundle(getArguments()).getRegion();
        mobileNumber = RegisterFragmentArgs.fromBundle(getArguments()).getMobileNumber();
        userId = RegisterFragmentArgs.fromBundle(getArguments()).getUserId();

        usernameText = view.findViewById(R.id.name);
        emailText = view.findViewById(R.id.mail);


        registerPresenter = new RegisterPresenter(this);
        imageView = view.findViewById(R.id.profile_pic);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImages();
            }
        });

        register = view.findViewById(R.id.btnRegister);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                username = usernameText.getText().toString();

                email = emailText.getText().toString();

                if (validate()) {
                    registerPresenter.sendImage(mobileNumber, userId, regionId, fileupload, filename, username, email);
                }
            }
        });
        return view;
    }

    private boolean validate() {

        boolean valid = true;


        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";



        if (file == null) {
            showMessage("برجاء احتيار صورة لحسابك");
            valid = false;
        }

        if(username.isEmpty()){

            usernameText.setError("ادخل اسمك");
            usernameText.requestFocus();
            valid = false;
        }else {
            usernameText.setError(null);
        }


        if(email.isEmpty()){

            emailText.setError("ادحل البريد الالكتروني الخاص بك");
            emailText.requestFocus();
            valid = false;
        }else if(!email.matches(emailPattern)){

            emailText.setError("ادحل بريد الكتروني صحيح");
            emailText.requestFocus();
            valid = false;
        }else {
            emailText.setError(null);
        }



        return valid;
    }


    public void getImages(){
        ImagePicker.create(this)
                .returnMode(ReturnMode.NONE) // set whether pick and / or camera action should return immediate result or not.
                .folderMode(true) // folder mode (false by default)
                .toolbarFolderTitle("Folder") // folder selection title
                .toolbarImageTitle("Tap to select") // image selection title
                .toolbarArrowColor(Color.BLACK) // Toolbar 'up' arrow color
                .multi() // multi mode (default mode)
                .limit(1) // max images can be selected (99 by default)
                .showCamera(true) // show camera or not (true by default)
                .enableLog(false) // disabling log
                .start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // Get a list of picked images
            List<Image> imageList = ImagePicker.getImages(data);

            if (imageList != null) {
                if (!imageList.isEmpty()) {

                    for (int i = 0; i < imageList.size(); i++) {

                        file = new File(imageList.get(i).getPath());
                        imageView.setImageBitmap(getBitmapFromPath(imageList.get(i).getPath()));
                        requestBody = RequestBody.create(MediaType.parse("image/*"), file);
                        fileupload = MultipartBody.Part.createFormData("filename", file.getName(), requestBody);
                        filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

                    }

                }
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private Bitmap getBitmapFromPath(String filePath) {

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(filePath,bmOptions);
        return bitmap;

    }


    @Override
    public void showMessage(String error) {

        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
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
    public void navigateToMain(User user){

        sessionManager = new SessionManager(getContext());
        sessionManager.createLoginSession(user);
        Intent intent = new Intent(getContext(), MainActivity.class);
        getActivity().startActivity(intent);
        getActivity().finish();
    }

}
