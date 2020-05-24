package com.example.easyhealthy.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.easyhealthy.LoginActivity;
import com.example.easyhealthy.ProfileActivity;
import com.example.easyhealthy.R;
import com.google.firebase.auth.FirebaseAuth;

public class NotificationsFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnLogout = view.findViewById(R.id.btnLogoutProfile);
        Button btnProfile = view.findViewById(R.id.btnProfileProfile);
        Button btnChangePass = view.findViewById(R.id.btnChangePassProfile);
        Button btnAbout = view.findViewById(R.id.btnAboutProfile);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent halLogin = new Intent(getActivity(), LoginActivity.class);
                startActivity(halLogin);
                requireActivity().finish();
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent halProfile = new Intent(getActivity(), ProfileActivity.class);
                startActivity(halProfile);
                requireActivity().finish();
            }
        });
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }




    }


