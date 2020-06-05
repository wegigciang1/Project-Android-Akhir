package com.example.easyhealthy.ui.tipsManfaat;

import android.app.AlertDialog;
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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.easyhealthy.R;
import com.example.easyhealthy.interfaces.FirebaseLoadListenerInterface;
import com.example.easyhealthy.interfaces.SubGroupOnClickInterface;
import com.example.easyhealthy.ui.tipsManfaat.Model.ItemData;
import com.example.easyhealthy.ui.tipsManfaat.Model.ItemGroup;
import com.example.easyhealthy.ui.tipsManfaat.ViewHolder.ItemDataViewHolder;
import com.example.easyhealthy.ui.tipsManfaat.ViewHolder.ItemGroupViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TipsManfaatFragment extends Fragment{

    //coba reycle view 2
    FirebaseDatabase database;
    RecyclerView recyclerView;
    FirebaseRecyclerAdapter<ItemGroup, ItemGroupViewHolder> adapterGroup;
    FirebaseRecyclerAdapter<ItemData, ItemDataViewHolder> adapterData;
    RecyclerView.LayoutManager manager;


    AlertDialog dialog;
    FirebaseLoadListenerInterface firebaseLoadListenerInterface;

    RecyclerView my_reycycler_view;

    DatabaseReference myData;

    //steps
    private TextView textSteps;
    private double MagnitudePrevious =0;
    private  Integer stepcount=0;
    public ImageView itemImage;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_tipsmanfaat, container, false);

        //view
        my_reycycler_view = (RecyclerView) root.findViewById( R.id.recycle_view_list_home );
        my_reycycler_view.setHasFixedSize( true );
        my_reycycler_view.setLayoutManager( new LinearLayoutManager( getActivity() ) );



        //steps
        //coba reycleview 2
        manager = new LinearLayoutManager(  getActivity() );
        database = FirebaseDatabase.getInstance();
        myData = database.getReference("TipsManfaat");
        //ragu
        recyclerView = root.findViewById( R.id.recycle_view_list_home );
        recyclerView.setLayoutManager( manager );
        FirebaseRecyclerOptions<ItemGroup> options = new FirebaseRecyclerOptions.Builder<ItemGroup>()
                .setQuery( myData,ItemGroup.class )
                .build();

        //adapter reycycleview 2
        adapterGroup = new FirebaseRecyclerAdapter<ItemGroup, ItemGroupViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ItemGroupViewHolder itemGroupViewHolder, int i, @NonNull final ItemGroup itemGroup) {
                itemGroupViewHolder.headerTitle.setText( itemGroup.getHeaderTitle() );
                FirebaseRecyclerOptions<ItemData> option2 = new FirebaseRecyclerOptions.Builder<ItemData>()
                        .setQuery( myData.child(itemGroup.getCategoryId()).child( "data" ),ItemData.class)
                        .build();

                adapterData = new FirebaseRecyclerAdapter<ItemData, ItemDataViewHolder>(option2) {
                    @Override
                    protected void onBindViewHolder(@NonNull ItemDataViewHolder itemDataViewHolder, int i, @NonNull final ItemData itemData) {
                        itemDataViewHolder.judul.setText( itemData.getJudul() );

                        String url = itemData.getImage();
                        if (url != "") {
                            Glide
                                    .with(getContext()) // get context of Fragment
                                    .load(url)
                                    .into(itemDataViewHolder.image);
                        }

                        itemDataViewHolder.SubGroupOnClickInterface( new SubGroupOnClickInterface() {
                            @Override
                            public void onClick(View view, boolean isLongPressed) {
                                Intent intent = new Intent( getActivity(), DataDisplay.class );
                                intent.putExtra( "judul", itemData.getJudul() );
                                //String tamp = itemData.getVideo();
                                intent.putExtra( "video", itemData.getVideo() );
                                intent.putExtra( "detail",itemData.getDetail() );
                                intent.putExtra( "label",itemGroup.getCategoryId() );
                                startActivity( intent );
                            }
                        } );

                    }

                    @NonNull
                    @Override
                    public ItemDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View v2 = LayoutInflater.from( getActivity() )
                                .inflate( R.layout.layout_item_home,parent,false );
                        return new ItemDataViewHolder( v2 );
                    }
                };
                adapterData.startListening();
                adapterData.notifyDataSetChanged();
                itemGroupViewHolder.group_recyclerView.setAdapter( adapterData );

            }

            @NonNull
            @Override
            public ItemGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v1 = LayoutInflater.from( getActivity() )
                        .inflate( R.layout.layout_group_home,parent,false );
                return new ItemGroupViewHolder( v1 );
            }
        };
        adapterGroup.startListening();
        adapterGroup.notifyDataSetChanged();
        recyclerView.setAdapter( adapterGroup );



        //steps init
        textSteps = (TextView) root.findViewById( R.id.tvSteps );
        SensorManager sensorManager = (SensorManager) getActivity().getSystemService( Context.SENSOR_SERVICE );
        Sensor sensor = sensorManager.getDefaultSensor( Sensor.TYPE_ACCELEROMETER );

        //listener sensor
        SensorEventListener stepDetector = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event != null){
                    float x_accleration = event.values[0];
                    float y_accleration = event.values[1];
                    float z_accleration = event.values[2];

                    double Magnitude = Math.sqrt( x_accleration*x_accleration + y_accleration*y_accleration + z_accleration*z_accleration);
                    double Magnitudedelta = Magnitude - MagnitudePrevious;
                    MagnitudePrevious =Magnitude;
                    if (Magnitudedelta > 6){
                        stepcount++;
                    }
                    textSteps.setText( stepcount.toString() );
                                    }

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        sensorManager.registerListener( stepDetector, sensor, SensorManager.SENSOR_DELAY_NORMAL );

        return root;
    }

    public void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = this.getActivity().getPreferences( Context.MODE_PRIVATE );
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putInt( "stepcount", stepcount );
        editor.apply();
    }

    public void onStop() {
        super.onStop();
        SharedPreferences sharedPreferences = this.getActivity().getPreferences( Context.MODE_PRIVATE );
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putInt( "stepcount", stepcount );
        editor.apply();
    }

    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = this.getActivity().getPreferences( Context.MODE_PRIVATE );
        stepcount = sharedPreferences.getInt( "stepcount" , 0);
    }

}
