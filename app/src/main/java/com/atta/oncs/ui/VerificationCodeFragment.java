package com.atta.oncs.ui;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.atta.oncs.R;
import com.atta.oncs.contracts.VerifyContract;
import com.atta.oncs.model.Result;
import com.atta.oncs.model.SessionManager;
import com.atta.oncs.model.User;
import com.atta.oncs.presenter.VerifyPresenter;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import static android.app.Activity.RESULT_OK;

public class VerificationCodeFragment extends Fragment implements VerifyContract.View, View.OnClickListener {



    private static final int SMS_CONSENT_REQUEST = 2;  // Set to an unused request code
    private BroadcastReceiver smsVerificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
                Bundle extras = intent.getExtras();
                Status smsRetrieverStatus = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

                switch (smsRetrieverStatus.getStatusCode()) {
                    case CommonStatusCodes.SUCCESS:
                        // Get consent intent
                        Intent consentIntent = extras.getParcelable(SmsRetriever.EXTRA_CONSENT_INTENT);
                        try {
                            // Start activity to show consent dialog to user, activity must be started in
                            // 5 minutes, otherwise you'll receive another TIMEOUT intent
                            startActivityForResult(consentIntent, SMS_CONSENT_REQUEST);
                        } catch (ActivityNotFoundException e) {
                            // Handle the exception ...
                        }
                        break;
                    case CommonStatusCodes.TIMEOUT:

                        Toast.makeText(getContext(), "timeout", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        }
    };

    private SessionManager sessionManager;

    private EditText editText1, editText2, editText3, editText4, editText5;

    private Button confirm;

    private TextView text, resendCodeTxt;

    private int regionId, id;
    private int status = 0;
    private String mobileNumber;
    private String message;
    private String verificationCode;

    private User user;
    private VerifyPresenter verifyPresenter;

    private String token;

    private static final String TAG = "VerificationCode";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_verfication_code,container,false);

        regionId = VerificationCodeFragmentArgs.fromBundle(getArguments()).getRegionId();
        mobileNumber = VerificationCodeFragmentArgs.fromBundle(getArguments()).getMobileNumber();

        editText1 = view.findViewById(R.id.editText1);
        editText2 = view.findViewById(R.id.editText2);
        editText3 = view.findViewById(R.id.editText3);
        editText4 = view.findViewById(R.id.editText4);
        editText5 = view.findViewById(R.id.editText5);

        editText1.setOnClickListener(this);
        editText2.setOnClickListener(this);
        editText3.setOnClickListener(this);
        editText4.setOnClickListener(this);
        editText5.setOnClickListener(this);

        confirm = view.findViewById(R.id.btnRegister);
        text = view.findViewById(R.id.mobile_number);
        text.setText(mobileNumber);

        resendCodeTxt = view.findViewById(R.id.resend_code);

        moveToNextTV(editText1, editText2);
        moveToNextTV(editText2, editText3);
        moveToNextTV(editText3, editText4);
        moveToNextTV(editText4, editText5);
        moveToNextTV(editText5);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //navigateToRegisterFragment();
                checkCode();
            }
        });


        startSMSListener();

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        if (task.getResult() != null) {
                            token = task.getResult().getToken();

                        }

                    }
                });

        verifyPresenter = new VerifyPresenter(this);
        verifyPresenter.verifyMobile(mobileNumber, regionId, token);


        resendCodeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                verifyPresenter.verifyMobile(mobileNumber, regionId, token);
            }
        });
        return view;
    }
    private boolean validateTextViews() {

        boolean valid = true;

        if (editText1.getText().toString().isEmpty() || editText2.getText().toString().isEmpty() ||
                editText3.getText().toString().isEmpty() || editText4.getText().toString().isEmpty() ||
                editText5.getText().toString().isEmpty() ) {
            showMessage("برجأء ادخال كود مكون من 5 ارقام");
            valid = false;
        }
        return valid;
    }

    @Override
    public void onStop() {

        try {
            if (smsVerificationReceiver != null) {
                getContext().unregisterReceiver(smsVerificationReceiver);
            }
        } catch (IllegalArgumentException e) {
            Log.i("verification fragment","epicReciver is already unregistered");
            smsVerificationReceiver = null;
        }

        super.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case SMS_CONSENT_REQUEST:
                if (resultCode == RESULT_OK) {
                    // Get SMS message content
                    message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);

                    //textView.setText(message);
                    // Extract one-time code from the message and complete verification
                    // `sms` contains the entire text of the SMS message, so you will need
                    // to parse the string.
                    parseOneTimeCode(); // define this function

                    // send one time code to the server
                } else {
                    // Consent canceled, handle the error ...
                }
                break;

        }
    }


    public void moveToNextTV(final EditText et1, final EditText et2){
        et1.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(et1.getText().toString().length() == 1)     //size as per your requirement
                {
                    et2.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });
    }



    private void moveToNextTV(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(editText.getText().toString().length() == 1)     //size as per your requirement
                {

                    checkCode();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });
    }

    private void checkCode() {
        if (verificationCode != null) {
            if (validateTextViews()) {

                String code = editText1.getText().toString() + editText2.getText().toString() +
                        editText3.getText().toString() + editText4.getText().toString() +
                        editText5.getText().toString();

                confirmCode(code);
            }

        }
    }


    @Override
    public void startSMSListener(){

        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        getActivity().registerReceiver(smsVerificationReceiver, intentFilter);

        // Start listening for SMS User Consent broadcasts from senderPhoneNumber
        // The Task<Void> will be successful if SmsRetriever was able to start
        // SMS User Consent, and will error if there was an error starting.
        Task<Void> task = SmsRetriever.getClient(getContext()).startSmsUserConsent( null);

        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                //Toast.makeText(getContext(), "started", Toast.LENGTH_LONG).show();
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }



    private void parseOneTimeCode() {



        editText1.setText(message.substring(0,1));
        editText2.setText(message.substring(1,2));
        editText3.setText(message.substring(2,3));
        editText4.setText(message.substring(3,4));
        editText5.setText(message.substring(4));

        if(verificationCode != null){
            confirmCode();
        }

    }

    @Override
    public void saveUserDate(Result result){
        showMessage(result.getVerifyCode());
        verificationCode = result.getVerifyCode();
        status = result.getStatus();
        id = result.getId();
        if (status == 2) {
            user = result.getUser();
        }
        if (message != null){
            confirmCode();
        }
    }


    public void confirmCode(String code){
        if (code.equals(verificationCode)){

            showMessage("تم التحقق");

            if (status == 1){
                navigateToRegisterFragment();
            }else if (status == 2){
                sessionManager = new SessionManager(getContext());
                sessionManager.createLoginSession(user);
                navigateToMain();
            }
        }else {
            showMessage("كود خطأ يرجي المحاولة مرة اخري");
        }
    }

    public void confirmCode(){
        if (message.equals(verificationCode)){

            showMessage("تم التحقق");

            if (status == 1){
                navigateToRegisterFragment();
            }else if (status == 2){
                sessionManager = new SessionManager(getContext());
                sessionManager.createLoginSession(user);
                navigateToMain();
            }
        }else {
            showMessage("كود خطأ يرجي المحاولة مرة اخري");
        }
    }



    @Override
    public void showMessage(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
    }


    public void navigateToRegisterFragment() {

        Navigation.findNavController(getActivity(), R.id.myNavHostFragment).
                navigate(VerificationCodeFragmentDirections.actionVerificationCodeFragmentToRegisterFragment(mobileNumber, regionId, id));
    }

    public void navigateToMain(){

        Intent intent = new Intent(getContext(), MainActivity.class);
        getActivity().startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onClick(View view) {

        EditText editText = (EditText) view;

        editText.getText().clear();
    }
}
