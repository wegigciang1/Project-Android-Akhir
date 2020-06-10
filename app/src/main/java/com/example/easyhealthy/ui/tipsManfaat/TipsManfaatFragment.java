package com.example.easyhealthy.ui.tipsManfaat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.easyhealthy.R;

public class TipsManfaatFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_tipsmanfaat, container, false);

        CardView tampilkanBreakfast = root.findViewById(R.id.breakfeast);
        CardView tampilkanLunch = root.findViewById(R.id.lunch);
        CardView tampilkanDinner = root.findViewById(R.id.dinner);
        CardView tampilkanWorkout = root.findViewById(R.id.workout);

        tampilkanBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("data", "Breakfeast");
                Navigation.findNavController(root).navigate(R.id.action_navigation_home_to_seeAllFragment, bundle);
            }
        });

        tampilkanLunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("data", "Lunch");
                Navigation.findNavController(root).navigate(R.id.action_navigation_home_to_seeAllFragment, bundle);
            }
        });

        tampilkanDinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("data", "Dinner");
                Navigation.findNavController(root).navigate(R.id.action_navigation_home_to_seeAllFragment, bundle);
            }
        });

        tampilkanWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("data", "Workout");
                Navigation.findNavController(root).navigate(R.id.action_navigation_home_to_seeAllFragment, bundle);
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


}
