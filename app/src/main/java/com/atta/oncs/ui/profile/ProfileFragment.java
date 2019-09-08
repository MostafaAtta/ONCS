package com.atta.oncs.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.atta.oncs.AddressesActivity;
import com.atta.oncs.R;
import com.atta.oncs.contracts.ProfileContract;
import com.atta.oncs.model.SessionManager;
import com.atta.oncs.model.User;
import com.atta.oncs.presenter.ProfilePresenter;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment implements ProfileContract.View {

    TextView addressesTv;

    ImageView profileImage;

    ProfilePresenter profilePresenter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        addressesTv = root.findViewById(R.id.add_addresses);

        addressesTv.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), AddressesActivity.class);
            getActivity().startActivity(intent);
        });

        profileImage = root.findViewById(R.id.profile_iamge);

        profilePresenter = new ProfilePresenter(this);

        profilePresenter.getProfile(SessionManager.getInstance(getContext()).getUserId());

        return root;
    }

    @Override
    public void showMessage(String error) {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showProfile(User user) {


        Picasso.get()
                .load(user.getProfileImage())
                .resize(120, 120)
                .centerCrop()
                .into(profileImage);

    }
}