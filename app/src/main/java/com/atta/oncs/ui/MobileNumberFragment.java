package com.atta.oncs.ui;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.atta.oncs.R;
import com.atta.oncs.contracts.LoginContract;
import com.atta.oncs.model.Region;
import com.atta.oncs.model.SessionManager;
import com.atta.oncs.presenter.LoginPresenter;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class MobileNumberFragment extends Fragment implements LoginContract.View, AdapterView.OnItemSelectedListener {


    private static final int CREDENTIAL_PICKER_REQUEST = 1;  // Set to an unused request code
    private static final int RESOLVE_HINT = 1;

    private LoginPresenter loginPresenter;

    private Button login;

    private TextInputEditText textInputEditText;

    private Spinner regionsSpinner;

    private ArrayAdapter<String> regionsAdapter;

    private ArrayList<Region> mRegions;

    private ArrayList<String> regionsname;

    private int selectedRegion;

    private String selectedRegionName;

    private android.view.View view;

    private TextView termsTv;

    @Nullable
    @Override
    public android.view.View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_mobile_number,container,false);

        loginPresenter = new LoginPresenter(this);

        regionsSpinner = view.findViewById(R.id.regions);

        textInputEditText = view.findViewById(R.id.mobile);

        login = view.findViewById(R.id.btnLogin);

        termsTv = view.findViewById(R.id.terms);

        String mystring = getResources().getString(R.string.terms);
        SpannableString content = new SpannableString(mystring);
        content.setSpan(new UnderlineSpan(), 0, mystring.length(), 0);
        termsTv.setText(content);

        login.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {


                login.setEnabled(false);
                if (!validate(textInputEditText.getText().toString())) {
                    login.setEnabled(true);
                    return;
                }

                navigateToVerificationFragment();
                //loginPresenter.verifyMobile(textInputEditText.getText().toString(), selectedRegion);
            }
        });

        loginPresenter.getRegions();

        selectedRegion = 0;

        try {
            requestHint();
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }




        return view;
    }



    // Construct a request for phone numbers and show the picker
    private void requestHint() throws IntentSender.SendIntentException {
        HintRequest hintRequest = new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build();
        PendingIntent intent = Credentials.getClient(getContext()).getHintPickerIntent(hintRequest);
        startIntentSenderForResult(intent.getIntentSender(),
                RESOLVE_HINT, null, 0, 0, 0, null);



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CREDENTIAL_PICKER_REQUEST:
                // Obtain the phone number from the result
                if (resultCode == RESULT_OK) {
                    Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                    String phone = credential.getId();

                    phone = phone.substring(2);
                    textInputEditText.setText(phone);
                    //Toast.makeText(this, phone, Toast.LENGTH_LONG).show();
                    //<-- will need to process phone number string
                }else {
                    //Toast.makeText(this, "another", Toast.LENGTH_LONG).show();
                    textInputEditText.requestFocus();
                }
                break;


        }
    }


    @Override
    public void showMessage(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
    }


    public void navigateToVerificationFragment() {

        SessionManager.getInstance(getContext()).setUserRegionName(selectedRegionName);

        Navigation.findNavController(getActivity(), R.id.myNavHostFragment).
                navigate(MobileNumberFragmentDirections.actionMobileNumberFragmentToVerificationCodeFragment(textInputEditText.getText().toString(), selectedRegion));
    }


    @Override
    public boolean validate(String mobileNumber) {

        boolean valid = true;

        if (mobileNumber.length() != 11) {
            textInputEditText.setError("ادخل رقم الهاتف");
            valid = false;
        } else {
            textInputEditText.setError(null);
        }

        if(selectedRegion == 0){

            showMessage("اختار المنطقة الخاصة بك");
            valid = false;
        }






        return valid;
    }

    @Override
    public void setSpinner(ArrayList<Region> regions) {

        mRegions = regions;

        regionsname = new ArrayList<>();
        regionsname.add("اختار المنطقة الخاصة بك");

        for (Region region : mRegions) {
            regionsname.add(region.toString());
        }

        regionsAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, regionsname);
        regionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        regionsSpinner.setAdapter(regionsAdapter);
        regionsSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, android.view.View view, int position, long l) {

        if (position != 0){

            selectedRegion = mRegions.get(position - 1).getId();
            selectedRegion = mRegions.get(position - 1).getId();

        }else {
            selectedRegion = 0;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}
