package com.cektrend.cekwarteg.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cektrend.cekwarteg.R;
import com.cektrend.cekwarteg.data.DataMenuOwner;
import com.cektrend.cekwarteg.model.DataWarteg;

import java.util.ArrayList;

public class ListWartegAdapter extends RecyclerView.Adapter<ListWartegAdapter.WartegViewHolder> {
    private ArrayList<DataWarteg> datalist;
    private DataMenuOwnerAdapter.ItemClickListener mClickListener;
    private Context context;
    private Activity parentActivity;

    public ListWartegAdapter(ArrayList<DataWarteg> listWarteg, Context mCtx, Activity parentActivity) {
        this.datalist = listWarteg;
        this.context = mCtx;
        this.parentActivity = parentActivity;
    }

    @NonNull
    @Override
    public ListWartegAdapter.WartegViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_warteg, parent, false);
        return new WartegViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListWartegAdapter.WartegViewHolder holder, int position) {
        holder.wartegName.setText(datalist.get(position).getName());
        Glide.with(context)
                .load(datalist.get(position).getPhoto_profile())
                .apply(new RequestOptions().override(200, 100))
                .into(holder.imgWarteg);
    }

    @Override
    public int getItemCount() {
        return (datalist != null) ? datalist.size() : 0;
    }

    public class WartegViewHolder extends RecyclerView.ViewHolder {
        TextView wartegName;
        ImageView imgWarteg;

        WartegViewHolder(@NonNull View itemView) {
            super(itemView);
            wartegName = itemView.findViewById(R.id.warteg_name);
            imgWarteg = itemView.findViewById(R.id.img_warteg);
        }

        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

    }
    void setClickListener(DataMenuOwnerAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}
