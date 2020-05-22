package com.example.easyhealthy.ui.home;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;



import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import com.example.easyhealthy.R;
import com.example.easyhealthy.adapter.ItemGroupHomeAdapter;
import com.example.easyhealthy.interfaces.FirebaseLoadListenerInterface;
import com.example.easyhealthy.ui.home.Model.ItemData;
import com.example.easyhealthy.ui.home.Model.ItemGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class HomeFragment extends Fragment implements FirebaseLoadListenerInterface {


    private HomeViewModel homeViewModel;

    AlertDialog dialog;
    FirebaseLoadListenerInterface firebaseLoadListenerInterface;

    RecyclerView my_reycycler_view;

    DatabaseReference myData;

    //coba coba
    List<ItemGroup> itemGroupList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        //view
        my_reycycler_view = (RecyclerView) root.findViewById( R.id.recycle_view_home );
        my_reycycler_view.setHasFixedSize( true );
        my_reycycler_view.setLayoutManager( new LinearLayoutManager( getActivity() ) );

        //coba coba
        ItemGroupHomeAdapter adapter = new ItemGroupHomeAdapter( getActivity(), itemGroupList );
        my_reycycler_view.setAdapter( adapter );

        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        //init
        myData = FirebaseDatabase.getInstance().getReference("manajemen-berat-badan");
        dialog = new SpotsDialog.Builder().setContext( getActivity() ).build();
        firebaseLoadListenerInterface = this;


        //load data
         getFirebaseData();

    }

    private void getFirebaseData() {
        dialog.show();
        myData.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<ItemGroup>  itemGroups = new ArrayList<>(  );
                for (DataSnapshot groupSnapshot: dataSnapshot.getChildren()){
                    ItemGroup itemGroup = new ItemGroup(  );
                    itemGroup.setHeaderTitle( groupSnapshot.child( "headerTitle" ).getValue(true).toString() );
                    GenericTypeIndicator <ArrayList<ItemData>> genericTypeIndicator = new GenericTypeIndicator<ArrayList<ItemData>>(){};
                    itemGroup.setListResep( groupSnapshot.child( "listResep" ).getValue( genericTypeIndicator ) );
                    itemGroups.add( itemGroup );

                }
                firebaseLoadListenerInterface.onFirebaseLoadSucces( itemGroups );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                firebaseLoadListenerInterface.onFirebaseLoadFailed( databaseError.getMessage() );
            }
        } );
    }

    @Override
    public void onFirebaseLoadSucces(List<ItemGroup> itemGroupList) {
        ItemGroupHomeAdapter adapter = new ItemGroupHomeAdapter( getActivity(), itemGroupList );
        my_reycycler_view.setAdapter( adapter );

        dialog.dismiss();
    }

    @Override
    public void onFirebaseLoadFailed(String message) {
        Toast.makeText( getActivity(), message, Toast.LENGTH_SHORT ).show();
        dialog.dismiss();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        return view;

    }

}
