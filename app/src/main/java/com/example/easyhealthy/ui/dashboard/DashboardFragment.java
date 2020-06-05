package com.example.easyhealthy.ui.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyhealthy.R;
import com.example.easyhealthy.ui.dashboard.Model_dashboard.DataBerat;
import com.example.easyhealthy.ui.dashboard.ViewHolder.DataBeratViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class DashboardFragment extends Fragment {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mFirebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference collref = mFirebaseFirestore.collection("Berat Badan");
    private String userID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();


    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter<DataBerat, DataBeratViewHolder> adapterGroup;

    private double MagnitudePrevious = 0;
    private Integer stepcount = 0;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

//        recyclerView = root.findViewById(R.id.recycle_view_tampil_berat);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        FirestoreRecyclerOptions<DataBerat> options = new FirestoreRecyclerOptions.Builder<DataBerat>()
//                .setQuery(collref, DataBerat.class)
//                .build();
//
//        adapterGroup = new FirebaseRecyclerAdapter<DataBerat, DataBeratViewHolder>(options) {
//
//
//            @Override
//            protected void onBindViewHolder(@NonNull DataBeratViewHolder dataBeratViewHolder, int i, @NonNull DataBerat dataBerat) {
//                dataBeratViewHolder.berat.setText(dataBerat.getBerat());
//                dataBeratViewHolder.tanggal.setText(String.valueOf(dataBerat.getTanggal()));
//            }
//
//            @NonNull
//            @Override
//            public DataBeratViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext()).inflate(
//                        R.layout.berat_item, parent, false
//                );
//                return new DataBeratViewHolder(view);
//            }
//
//
//        };
//        adapterGroup.startListening();
//        adapterGroup.notifyDataSetChanged();
//        recyclerView.setAdapter(adapterGroup);

        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CardView pilihStatistik = view.findViewById(R.id.pilihStatistik);
        CardView pilihInputRiwayat = view.findViewById(R.id.pilihInputRiwayat);
        CardView pilihKalori = view.findViewById(R.id.pilihKalori);
        final TextView langkahKaki = view.findViewById(R.id.langkahKaki);
        final RelativeLayout tampilkanInputRiwayat = view.findViewById(R.id.tampilkanInputRiwayat);
        final RelativeLayout tampilkanKalori = view.findViewById(R.id.tampilkanKalori);
        final LineChart tampilBerat = view.findViewById(R.id.tampilGrafikBerat);
        final EditText inputBeratHarian = view.findViewById(R.id.inputBeratHarian);
        Button btnInputBeratHarian = view.findViewById(R.id.btnInputBeratHarian);


        isiGrafik(collref, tampilBerat, userID);

        pilihStatistik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tampilBerat.setVisibility(View.VISIBLE);
                tampilkanInputRiwayat.setVisibility(View.INVISIBLE);
                tampilkanKalori.setVisibility(View.INVISIBLE);
            }
        });

        pilihInputRiwayat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tampilkanInputRiwayat.setVisibility(View.VISIBLE);
                tampilBerat.setVisibility(View.INVISIBLE);
                tampilkanKalori.setVisibility(View.INVISIBLE);

            }
        });

        pilihKalori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tampilkanKalori.setVisibility(View.VISIBLE);
                tampilkanInputRiwayat.setVisibility(View.INVISIBLE);
                tampilBerat.setVisibility(View.INVISIBLE);
            }
        });

        btnInputBeratHarian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tambahBerat(inputBeratHarian, userID, tampilBerat, collref);
            }
        });

        //steps init
        SensorManager sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //listener sensor
        SensorEventListener stepDetector = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event != null) {
                    float x_accleration = event.values[0];
                    float y_accleration = event.values[1];
                    float z_accleration = event.values[2];

                    double Magnitude = Math.sqrt(x_accleration * x_accleration + y_accleration * y_accleration + z_accleration * z_accleration);
                    double Magnitudedelta = Magnitude - MagnitudePrevious;
                    MagnitudePrevious = Magnitude;
                    if (Magnitudedelta > 6) {
                        stepcount++;
                    }
                    langkahKaki.setText(stepcount.toString());
                }

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        sensorManager.registerListener(stepDetector, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = this.getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putInt("stepcount", stepcount);
        editor.apply();
    }

    public void onStop() {
        super.onStop();
        SharedPreferences sharedPreferences = this.getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putInt("stepcount", stepcount);
        editor.apply();
    }

    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = this.getActivity().getPreferences(Context.MODE_PRIVATE);
        stepcount = sharedPreferences.getInt("stepcount", 0);
    }


    private void tambahBerat(EditText inputHarian, final String userId, final LineChart tampilBerat, final CollectionReference collref) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMM-yyyy", Locale.getDefault());
        Date date = new Date();
        Map<String, String> beratHarian = new HashMap<>();
        beratHarian.put("berat", inputHarian.getText().toString());
        beratHarian.put("tanggal", sdf.format(date));
        beratHarian.put("id", userId);

        collref
                .add(beratHarian)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(requireActivity(), "Berhasil Menambah Berat", Toast.LENGTH_SHORT).show();
                        isiGrafik(collref, tampilBerat, userId);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }

    private void isiGrafik(CollectionReference collref, final LineChart tampilBerat, String userId) {
        final ArrayList<Entry> dataSet = new ArrayList<>();
        collref.whereEqualTo("id", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int i = 0;
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                dataSet.add(new Entry(i, Integer.parseInt(String.valueOf(document.get("berat")))));
                                i++;
                            }
                            LineDataSet lineDataSet = new LineDataSet(dataSet, "Berat");
                            ArrayList<ILineDataSet> iLineDataSets = new ArrayList<>();
                            iLineDataSets.add(lineDataSet);
                            LineData lineData = new LineData(iLineDataSets);
                            tampilBerat.setData(lineData);
                            tampilBerat.invalidate();
                        }
                    }
                });

    }


}