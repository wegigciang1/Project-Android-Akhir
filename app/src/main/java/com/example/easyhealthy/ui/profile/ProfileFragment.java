package com.example.easyhealthy.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.easyhealthy.AboutButtonActivity;
import com.example.easyhealthy.LoginActivity;
import com.example.easyhealthy.PasswordChangesButtonActivity;
import com.example.easyhealthy.ProfileButtonActivity;
import com.example.easyhealthy.R;
import com.example.easyhealthy.RencanaActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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


        CardView btnProfile = view.findViewById(R.id.btnEditProfile);
        CardView btnGantiFoto = view.findViewById(R.id.btnChangePhotoProfile);
        CardView btnChangePass = view.findViewById(R.id.btnChangePassProfile);
        CardView btnAbout = view.findViewById(R.id.btnAboutProfile);
        CardView btnLogout = view.findViewById(R.id.btnLogout);
        CardView btnCancelRencana = view.findViewById(R.id.btnCancelRencanaProfile);

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
                        if (url != "") {
                            Glide
                                    .with(getContext()) // get context of Fragment
                                    .load(url)
                                    .circleCrop()

                                    .into(fotoProfile);
                        }
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
                DocumentReference washingtonRef = fStore.collection("Users").document(firebaseAuth.getCurrentUser().getUid());
                washingtonRef
                        .update("Rencana", "")
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });

                CollectionReference ambilKalori = fStore.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).collection("Kalori");
                ambilKalori
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        DocumentReference washingtonRef = fStore.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).collection("Kalori").document(document.getId());
                                        washingtonRef.delete();
                                    }
                                }
                            }
                        });

                CollectionReference ambilBerat = fStore.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).collection("Berat Badan");
                ambilBerat
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        DocumentReference washingtonRef = fStore.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).collection("Berat badan").document(document.getId());
                                        washingtonRef.delete();
                                        Intent goToHalRencana = new Intent(getActivity(), RencanaActivity.class);
                                        startActivity(goToHalRencana);
                                        getActivity().finish();
                                    }
                                }
                            }
                        });

            }
        });

        btnGantiFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent halProfile1 = new Intent(getActivity(), ProfileTestActivity.class);
                startActivity(halProfile1);
            }
        });

    }


}


