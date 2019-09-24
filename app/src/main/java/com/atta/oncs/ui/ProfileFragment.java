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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atta.oncs.R;
import com.atta.oncs.contracts.ProfileContract;
import com.atta.oncs.model.Address;
import com.atta.oncs.model.AddressesAdapter;
import com.atta.oncs.model.Region;
import com.atta.oncs.model.SessionManager;
import com.atta.oncs.model.User;
import com.atta.oncs.presenter.ProfilePresenter;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ProfileFragment extends Fragment implements ProfileContract.View, View.OnClickListener, AdapterView.OnItemSelectedListener {

    private TextView addressesTv;

    private ImageView profileImage;

    private ProfilePresenter profilePresenter;

    private Button updateImage, updateProfile;

    private ProgressDialog progressDialog;

    private TextInputEditText nameEditText, mailEditText, phoneEditText;

    private File file;

    private RequestBody requestBody;
    private MultipartBody.Part fileupload ;
    private RequestBody filename ;


    private Spinner regionsSpinner;
    private ArrayAdapter<String> regionsAdapter;
    private ArrayList<Region> mRegions;
    private ArrayList<String> regionsName;
    private int selectedRegion;

    private String username, email;

    List<Address> addresses;

    RecyclerView recyclerView;

    TextView infoTxt;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        addressesTv = root.findViewById(R.id.add_addresses);

        addressesTv.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), AddressesActivity.class);
            getActivity().startActivity(intent);
        });

        profileImage = root.findViewById(R.id.profile_iamge);
        updateImage = root.findViewById(R.id.photo_update);
        updateProfile = root.findViewById(R.id.update);
        nameEditText = root.findViewById(R.id.name_tv);
        mailEditText = root.findViewById(R.id.email_tv);
        regionsSpinner = root.findViewById(R.id.regions_spinner);
        phoneEditText = root.findViewById(R.id.mobile_tv);
        recyclerView = root.findViewById(R.id.addresses_spinner);
        infoTxt = root.findViewById(R.id.info_txt);

        profileImage.setOnClickListener(this);
        updateImage.setOnClickListener(this);
        updateProfile.setOnClickListener(this);

        profilePresenter = new ProfilePresenter(this);

        profilePresenter.getProfile(SessionManager.getInstance(getContext()).getUserId());

        return root;
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
                        profileImage.setImageBitmap(getBitmapFromPath(imageList.get(i).getPath()));
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
    public void showProfile(User user) {

        if (user != null) {

            if (!user.getProfileImage().isEmpty()) {


            Picasso.get()
                    .load(user.getProfileImage())
                    .resize(120, 120)
                    .centerCrop()
                    .into(profileImage);

            }

            nameEditText.setText(user.getUsername());
            mailEditText.setText(user.getEmail());
            phoneEditText.setText(user.getPhone());

        }
    }

    @Override
    public void onClick(View view) {

        if (view == profileImage){
            getImages();
        }else if (view == updateImage){
            if (validateImage()) {
                profilePresenter.sendImage(SessionManager.getInstance(getContext()).getUserPhone(), fileupload, filename);
            }
        }else if (view == updateProfile){

            username = nameEditText.getText().toString();

            email = mailEditText.getText().toString();

            if (validate()) {

                profilePresenter.updateProfile(SessionManager.getInstance(getContext()).getUserPhone(),
                        SessionManager.getInstance(getContext()).getUserId(), selectedRegion, username, email);
            }
        }
    }


    @Override
    public void setSpinner(ArrayList<Region> regions) {

        mRegions = regions;

        regionsName = new ArrayList<>();

        for (Region region : mRegions) {
            regionsName.add(region.toString());
        }

        regionsAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, regionsName);
        regionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        regionsSpinner.setAdapter(regionsAdapter);
        regionsSpinner.setOnItemSelectedListener(this);


            int selectedItem = regionsName.indexOf(SessionManager.getInstance(getContext()).getUserRegionName());

            regionsSpinner.setSelection(selectedItem);
            SessionManager.getInstance(getContext()).setOrderregionId(mRegions.get(selectedItem).getId());

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        selectedRegion = mRegions.get(i).getId();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private boolean validate() {

        boolean valid = true;

        if(username.isEmpty()){

            nameEditText.setError("ادخل اسمك");
            nameEditText.requestFocus();
            valid = false;
        }else {
            nameEditText.setError(null);
        }


        if(email.isEmpty()){

            mailEditText.setError("ادحل البريد الايكتروني الخاص بك");
            mailEditText.requestFocus();
            valid = false;
        }else {
            mailEditText.setError(null);
        }



        return valid;
    }


    private boolean validateImage() {

        boolean valid = true;

        if (file == null) {
            showMessage("برجاء احتيار صورة لحسابك");
            valid = false;
        }



        return valid;
    }

    @Override
    public void navigateToMain(User user){

        SessionManager.getInstance(getContext()).upadteUser(user);
        Intent intent = new Intent(getContext(), MainActivity.class);
        getActivity().startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void showAddressesMessage() {

        recyclerView.setVisibility(View.GONE);

        infoTxt.setVisibility(View.VISIBLE);

        infoTxt.setText(getResources().getString(R.string.my_addresses_info));
    }

    @Override
    public void showAddresses(List<Address> mAddresses) {


        addresses= mAddresses;

        AddressesAdapter addressesAdapter = new AddressesAdapter(getContext(), addresses, true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(addressesAdapter);
    }


}