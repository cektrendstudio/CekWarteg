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
import com.cektrend.cekwarteg.model.DataWarteg;

import java.util.ArrayList;

public class ListWartegAdapter extends RecyclerView.Adapter<ListWartegAdapter.WartegViewHolder> {
    ArrayList<DataWarteg> listWarteg;
    private final Context mCtx;
    private final Activity parentActivity;

    public ListWartegAdapter(ArrayList<DataWarteg> listWarteg, Context mCtx, Activity parentActivity) {
        this.listWarteg = listWarteg;
        this.mCtx = mCtx;
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
        holder.bind(listWarteg.get(position));
    }

    @Override
    public int getItemCount() {
        return this.listWarteg.size();
    }

    public class WartegViewHolder extends RecyclerView.ViewHolder {
        TextView wartegName1, wartegName2;
        ImageView imgWarteg1, imgWarteg2;

        WartegViewHolder(@NonNull View itemView) {
            super(itemView);
            wartegName1 = itemView.findViewById(R.id.warteg_name1);
            wartegName2 = itemView.findViewById(R.id.warteg_name2);
            imgWarteg1 = itemView.findViewById(R.id.img_warteg1);
            imgWarteg2 = itemView.findViewById(R.id.img_warteg2);
        }

        void bind(final DataWarteg dataWarteg) {
            wartegName1.setText(dataWarteg.getName());
            wartegName2.setText(dataWarteg.getName());
            Glide.with(itemView.getContext())
                    .load(dataWarteg.getPhoto_profile())
                    .apply(new RequestOptions().override(50, 57))
                    .into(imgWarteg1);
            Glide.with(itemView.getContext())
                    .load(dataWarteg.getPhoto_profile())
                    .apply(new RequestOptions().override(50, 57))
                    .into(imgWarteg2);
        }

    }

}
