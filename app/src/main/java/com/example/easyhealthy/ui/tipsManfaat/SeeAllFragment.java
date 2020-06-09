package com.example.easyhealthy.ui.tipsManfaat;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.easyhealthy.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class SeeAllFragment extends Fragment {

    RecyclerView recyclerView;
    FirestoreRecyclerAdapter adapterGroup;
    FirebaseFirestore mFirebaseFirestore = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_see_all, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycle_view_see_all);


        Query query = mFirebaseFirestore.collection(getArguments().getString("data"));

        FirestoreRecyclerOptions<ItemData> options = new FirestoreRecyclerOptions.Builder<ItemData>()
                .setQuery(query, ItemData.class)
                .build();

        adapterGroup = new FirestoreRecyclerAdapter<ItemData, DataItemViewHolder>(options) {


            @Override
            protected void onBindViewHolder(@NonNull DataItemViewHolder holder, int position, @NonNull final ItemData model) {
                holder.namaMakanan.setText(model.getNama());
                holder.kalori.setText(model.getKalori());
                String tampung = model.getGambar();
                if (tampung != "") {
                    Glide
                            .with(getContext()) // get context of Fragment
                            .load(tampung)
                            .centerCrop()
                            .into(holder.gambarItem);
                }
                holder.cardviewItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent goToDetailItem = new Intent(getContext(), DetailItemActivity.class);
                        goToDetailItem.putExtra("detail", model.getDetail());
                        goToDetailItem.putExtra("video", model.getVideo());
                        goToDetailItem.putExtra("judul", model.getNama());
                        goToDetailItem.putExtra("kalori", model.getKalori());
                        goToDetailItem.putExtra("data", getArguments().getString("data"));
                        getContext().startActivity(goToDetailItem);
                    }
                });
            }

            @NonNull
            @Override
            public DataItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.see_all_item, parent, false);
                return new DataItemViewHolder(view);
            }

        };
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(adapterGroup);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapterGroup.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapterGroup.stopListening();
    }

    private class DataItemViewHolder extends RecyclerView.ViewHolder {
        TextView namaMakanan;
        TextView kalori;
        ImageView gambarItem;
        CardView cardviewItem;

        public DataItemViewHolder(@NonNull View itemView) {
            super(itemView);
            cardviewItem = itemView.findViewById(R.id.cardviewItem);
            gambarItem = itemView.findViewById(R.id.gambarItem);
            namaMakanan = itemView.findViewById(R.id.namaMakanan);
            kalori = itemView.findViewById(R.id.jumlahKalori);
        }
    }
}
