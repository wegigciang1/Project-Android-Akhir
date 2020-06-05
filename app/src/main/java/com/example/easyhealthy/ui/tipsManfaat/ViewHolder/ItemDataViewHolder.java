package com.example.easyhealthy.ui.tipsManfaat.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyhealthy.R;
import com.example.easyhealthy.interfaces.SubGroupOnClickInterface;

public class ItemDataViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {

    public TextView judul;
    public ImageView image;
    public SubGroupOnClickInterface subGroupOnClickInterface;

    public ItemDataViewHolder(@NonNull View itemView) {
        super( itemView );
        judul = itemView.findViewById( R.id.tvTitle );
        image = itemView.findViewById( R.id.itemImage );
        itemView.setOnClickListener( this );
    }

    @Override
    public void onClick(View v) {
        subGroupOnClickInterface.onClick( v,false );

    }

    public void  SubGroupOnClickInterface(SubGroupOnClickInterface subGroupOnClickInterface){
        this.subGroupOnClickInterface = subGroupOnClickInterface;
    }
}
