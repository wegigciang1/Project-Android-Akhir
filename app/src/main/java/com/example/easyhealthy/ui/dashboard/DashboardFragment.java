package com.example.easyhealthy.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyhealthy.R;
import com.example.easyhealthy.adapter.DataBerat;
import com.example.easyhealthy.adapter.TampilRiwayatBeratAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DashboardFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore mFirebaseFirestore;
    private DocumentReference doc;
    private CollectionReference collref;
    private TampilRiwayatBeratAdapter tampilRiwayatBeratAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        doc = mFirebaseFirestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid());
        collref = mFirebaseFirestore.collection("Users");


        CardView pilihStatistik = view.findViewById(R.id.pilihStatistik);
        CardView pilihInputRiwayat = view.findViewById(R.id.pilihInputRiwayat);
        CardView pilihKalori = view.findViewById(R.id.pilihKalori);
        final RelativeLayout tampilkanInputRiwayat = view.findViewById(R.id.tampilkanInputRiwayat);
        final LineChart tampilBerat = view.findViewById(R.id.tampilGrafikBerat);
        final EditText inputBeratHarian = view.findViewById(R.id.inputBeratHarian);
        Button btnInputBeratHarian = view.findViewById(R.id.btnInputBeratHarian);

        isiGrafik(doc, tampilBerat);
        setUpRecycleView(view);

        pilihStatistik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tampilBerat.setVisibility(View.VISIBLE);
                tampilkanInputRiwayat.setVisibility(View.INVISIBLE);
            }
        });

        pilihInputRiwayat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tampilkanInputRiwayat.setVisibility(View.VISIBLE);
                tampilBerat.setVisibility(View.INVISIBLE);

            }
        });

        pilihKalori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnInputBeratHarian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tambahBerat(doc, inputBeratHarian);
            }
        });

    }

    private void setUpRecycleView(View view) {
        Query query = collref.orderBy("Berat Badan",Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<DataBerat> options = new FirestoreRecyclerOptions.Builder<DataBerat>()
                .setQuery(query,DataBerat.class)
                .build();

        tampilRiwayatBeratAdapter = new TampilRiwayatBeratAdapter(options);

        RecyclerView recyclerView = view.findViewById(R.id.recycle_view_tampil_berat);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(tampilRiwayatBeratAdapter);
    }

    private void tambahBerat(DocumentReference doc, EditText inputHarian) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMM-yyyy", Locale.getDefault());
        Date date = new Date();
        Map<String, String> beratHarian = new HashMap<>();
        beratHarian.put("Berat", inputHarian.getText().toString());
        beratHarian.put("Tanggal", sdf.format(date));

        doc.update("Berat Badan", FieldValue.arrayUnion(beratHarian))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(requireActivity(), "Berhasil", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(requireActivity(), "Gagal", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void isiGrafik(DocumentReference doc, final LineChart tampilBerat) {
        final ArrayList<Entry> dataSet = new ArrayList<>();

        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            private static final String TAG = "TAG";
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        ArrayList<Object> objectArrayList = (ArrayList<Object>) document.get("Berat Badan");

                        for (int i = 0; i < objectArrayList.size(); i++) {
                            Map<String, String> beratHarian = (Map<String, String>) objectArrayList.get(i);
                            dataSet.add(new Entry(i, Integer.parseInt(beratHarian.get("Berat"))));
                            beratHarian.clear();
                        }
                        LineDataSet lineDataSet = new LineDataSet(dataSet, "Berat");
                        ArrayList<ILineDataSet> iLineDataSets = new ArrayList<>();
                        iLineDataSets.add(lineDataSet);
                        LineData lineData = new LineData(iLineDataSets);
                        tampilBerat.setData(lineData);
                        tampilBerat.invalidate();

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        tampilRiwayatBeratAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        tampilRiwayatBeratAdapter.stopListening();
    }
}