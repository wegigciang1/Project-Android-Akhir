package com.example.easyhealthy.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyhealthy.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class TampilRiwayatBeratAdapter extends FirestoreRecyclerAdapter<DataBerat, TampilRiwayatBeratAdapter.TampilRiwayatBeratHolder> {

    public TampilRiwayatBeratAdapter(@NonNull FirestoreRecyclerOptions<DataBerat> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull TampilRiwayatBeratHolder holder, int position, @NonNull DataBerat model) {
        holder.tanggal.setText(model.getTanggal());
        holder.berat.setText(model.getBerat());
    }

    @NonNull
    @Override
    public TampilRiwayatBeratHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.berat_item,parent,false
        );
        return null;
    }

    class TampilRiwayatBeratHolder extends RecyclerView.ViewHolder{
        TextView tanggal,berat;
        public TampilRiwayatBeratHolder(@NonNull View itemView) {
            super(itemView);

            tanggal = itemView.findViewById(R.id.tanggalInputRecycle);
            berat = itemView.findViewById(R.id.beratInputRecycle);
        }
    }
}
