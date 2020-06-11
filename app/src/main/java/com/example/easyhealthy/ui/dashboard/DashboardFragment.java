package com.example.easyhealthy.ui.dashboard;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyhealthy.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class DashboardFragment extends Fragment {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mFirebaseFirestore = FirebaseFirestore.getInstance();
    DatePickerDialog dpd;
    int year, month, day;


    private RecyclerView recyclerView;
    private FirestoreRecyclerAdapter adapterGroup;

    private double MagnitudePrevious = 0;
    private Integer stepcount = 0;
    private CollectionReference collref = mFirebaseFirestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).collection("Berat Badan");
    private TextView tanggalKalori;
    private Calendar c;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);


        recyclerView = root.findViewById(R.id.recycle_view_tampil_berat);


        Query query = collref.orderBy("tanggal", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<DataBerat> options = new FirestoreRecyclerOptions.Builder<DataBerat>()
                .setQuery(query, DataBerat.class)
                .build();

        adapterGroup = new FirestoreRecyclerAdapter<DataBerat, DataBeratViewHolder>(options) {

            @NonNull
            @Override
            public DataBeratViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.berat_item, parent, false);
                return new DataBeratViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull DataBeratViewHolder holder, int position, @NonNull DataBerat model) {
                holder.berat.setText(model.getBerat());
                holder.tanggal.setText(model.getTanggal());
            }
        };
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapterGroup);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMM-yyyy", Locale.getDefault());
        Date date = new Date();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CardView pilihStatistik = view.findViewById(R.id.pilihStatistik);
        CardView pilihInputRiwayat = view.findViewById(R.id.pilihInputRiwayat);
        CardView pilihKalori = view.findViewById(R.id.pilihKalori);
        final TextView langkahKaki = view.findViewById(R.id.langkahKaki);
        final TextView eaten = view.findViewById(R.id.textViewEaten);
        final TextView kalori = view.findViewById(R.id.textViewKalori);
        final TextView burned = view.findViewById(R.id.textViewBurned);
        final TextView tanggalKalori = view.findViewById(R.id.tanggalKalori);
        final ConstraintLayout tampilkanInputRiwayat = view.findViewById(R.id.tampilkanInputRiwayat);
        final ConstraintLayout tampilkanKalori = view.findViewById(R.id.tampilkanKalori);
        final LineChart tampilBerat = view.findViewById(R.id.tampilGrafikBerat);
        final EditText inputBeratHarian = view.findViewById(R.id.inputBeratHarian);
        Button btnInputBeratHarian = view.findViewById(R.id.btnInputBeratHarian);
        Button btnPilihTanggal = view.findViewById(R.id.btnPilihTanggal);

        final ProgressBar progressBar = view.findViewById(R.id.progressBarInputRiwayat);

        final SimpleDateFormat sdf = new SimpleDateFormat("d-M-yyyy", Locale.getDefault());
        final Date date = new Date();

        tanggalKalori.setText(sdf.format(date));

        CollectionReference ambilKalori = mFirebaseFirestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).collection("Kalori");
        ambilKalori.whereEqualTo("tanggal", sdf.format(date)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        eaten.setText((String) document.get("eaten"));
                        kalori.setText((String) document.get("kaloriHarian"));
                        burned.setText((String) document.get("burned"));
                    }
                }
            }
        });

        final Intent intent = getActivity().getIntent();


        btnPilihTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                dpd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        tanggalKalori.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
                        CollectionReference ambilKalori = mFirebaseFirestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).collection("Kalori");
                        ambilKalori.whereEqualTo("tanggal", dayOfMonth + "-" + (month + 1) + "-" + year).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().isEmpty()) {
                                        DocumentReference docRef = mFirebaseFirestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid());
                                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document.exists()) {
                                                        kalori.setText((String) document.get("kaloriHarian"));
                                                        burned.setText("0");
                                                        eaten.setText("0");
                                                        Toast.makeText(getActivity(), (String) document.get("kaloriHarian"), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                        });

                                    } else {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            eaten.setText((String) document.get("eaten"));
                                            kalori.setText((String) document.get("kaloriHarian"));
                                            burned.setText((String) document.get("burned"));
                                        }
                                    }
                                }
                            }
                        });

                    }
                }, year, month, day);
                dpd.show();
            }
        });


        isiGrafik(collref, tampilBerat);

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
                progressBar.setVisibility(View.VISIBLE);
                tambahBerat(inputBeratHarian, collref, tampilBerat, progressBar);
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

    private void tambahBerat(final EditText inputHarian, final CollectionReference collref, final LineChart tampilBerat, final ProgressBar progressBar) {
        final SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMM-yyyy", Locale.getDefault());
        final Date date = new Date();

        collref
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {
                                Map<String, String> beratHarian = new HashMap<>();
                                beratHarian.put("berat", inputHarian.getText().toString());
                                beratHarian.put("tanggal", sdf.format(date));
                                DocumentReference docref = mFirebaseFirestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).collection("Berat Badan").document();
                                docref.set(beratHarian).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        isiGrafik(collref, tampilBerat);
                                        progressBar.setVisibility(View.INVISIBLE);
                                    }
                                });
                            } else {
                                collref.whereEqualTo("tanggal", sdf.format(date))
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        DocumentReference docref = mFirebaseFirestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).collection("Berat Badan").document(document.getId());
                                                        docref
                                                                .update("berat", inputHarian.getText().toString())
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        isiGrafik(collref, tampilBerat);
                                                                        progressBar.setVisibility(View.INVISIBLE);
                                                                    }
                                                                });
                                                    }
                                                }
                                            }
                                        });
                            }
                        }
                    }
                });

    }

    @Override
    public void onStart() {
        super.onStart();
        adapterGroup.startListening();
    }

    private void isiGrafik(CollectionReference collref, final LineChart tampilBerat) {
        final ArrayList<Entry> dataSet = new ArrayList<>();
        collref.orderBy("tanggal", Query.Direction.ASCENDING)
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

    @Override
    public void onStop() {
        super.onStop();
        SharedPreferences sharedPreferences = this.getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putInt("stepcount", stepcount);
        editor.apply();
        adapterGroup.stopListening();
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = this.getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putInt("stepcount", stepcount);
        editor.apply();
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = this.getActivity().getPreferences(Context.MODE_PRIVATE);
        stepcount = sharedPreferences.getInt("stepcount", 0);
    }

    private class DataBeratViewHolder extends RecyclerView.ViewHolder {
        TextView berat;
        TextView tanggal;

        public DataBeratViewHolder(@NonNull View itemView) {
            super(itemView);
            berat = itemView.findViewById(R.id.beratInputRecycle);
            tanggal = itemView.findViewById(R.id.tanggalInputRecycle);
        }
    }
}