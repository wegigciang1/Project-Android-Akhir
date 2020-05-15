package com.example.easyhealthy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyhealthy.R;

public class ItemGroupHomeAdapter extends RecyclerView.Adapter<ItemGroupHomeAdapter.MyviewHolder> {

    private Context context;
    //private List<ItemGroup> datalist;

//    public ItemGroupHomeAdapter(Context context, List<ItemGroup> datalist) {
//        this.context = context;
//        this.datalist = datalist;
//    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from( context ).inflate( R.layout.layout_group_home,parent,false );
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

//    @Override
//    public int getItemCount() {
//        return (datalist != null ? datalist.size() : 0);
//    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        TextView item_title;
        RecyclerView recyclerView_item_list;
        Button btn_more;

        public MyviewHolder(@NonNull View itemView) {
            super( itemView );
            item_title = (TextView)itemView.findViewById( R.id.itemTitle );
            btn_more = (Button)itemView.findViewById( R.id.btnMoreHome );
            recyclerView_item_list = (RecyclerView)itemView.findViewById( R.id.recycle_view_home );
        }
    }


}
