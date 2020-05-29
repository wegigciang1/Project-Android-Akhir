package com.example.easyhealthy.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.easyhealthy.AboutButtonActivity;
import com.example.easyhealthy.LoginActivity;
import com.example.easyhealthy.PasswordChangesButtonActivity;
import com.example.easyhealthy.ProfileButtonActivity;
import com.example.easyhealthy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {

    //untuk uid
    private FirebaseAuth firebaseAuth;
    private String userId;
    private FirebaseFirestore fStore;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        return view;
    }
    //akhir uid//

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final TextView namaText = view.findViewById(R.id.textViewNamaProfile);
        Button btnLogout = view.findViewById(R.id.btnLogoutProfile);
        Button btnProfile = view.findViewById(R.id.btnProfileProfile);
        Button btnChangePass = view.findViewById(R.id.btnChangePassProfile);
        Button btnAbout = view.findViewById(R.id.btnAboutProfile);
        Button btnCancelRencana = view.findViewById(R.id.btnCancelRencanaProfile);

        //get uid
        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        //akhir uid

        final ImageView fotoProfile = view.findViewById(R.id.imageViewFotoProfile);
        //profile

        fStore = FirebaseFirestore.getInstance();
        DocumentReference docRef = fStore.collection("Users").document(userId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String url = (String) document.get("FotoKey");
                        //Toast.makeText(requireActivity(), url, Toast.LENGTH_SHORT).show();
                        Glide
                                .with(getContext()) // get context of Fragment
                                .load(url)
                                .into(fotoProfile);
                        String name = (String) document.get("Nama");
                        namaText.setText(name);
                    } else {
                        Toast.makeText(requireActivity(), "Data Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(requireActivity(), "Task Gagal", Toast.LENGTH_SHORT).show();
                }
            }
        });



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
                Intent halProfile = new Intent(getActivity(), ProfileButtonActivity.class);
                startActivity(halProfile);
            }
        });
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent halChangePass = new Intent(getActivity(), PasswordChangesButtonActivity.class);
                startActivity(halChangePass);
            }
        });

        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent halAbout = new Intent(getActivity(), AboutButtonActivity.class);
                startActivity(halAbout);

            }
        });

        btnCancelRencana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent halProfile1 = new Intent(getActivity(), ProfileTestActivity.class);
                startActivity(halProfile1);
            }
        });

    }


}


