package com.cektrend.cekwarteg.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cektrend.cekwarteg.DetailWartegActivity;
import com.cektrend.cekwarteg.R;
import com.cektrend.cekwarteg.data.DataMenuOwner;
import com.cektrend.cekwarteg.data.DataWarteg;

import java.util.ArrayList;

public class DetailWartegAdapter extends RecyclerView.Adapter<DetailWartegAdapter.WartegViewHolder> {
    private ArrayList<DataWarteg> datalist;
    private DetailWartegAdapter.ItemClickListener mClickListener;
    private Context context;
    private Activity parentActivity;


    public DetailWartegAdapter(ArrayList<DataMenuOwner> listWarteg, Context mCtx,   Activity parentActivity) {
        this.datalist = datalist;
        this.context = context;
        this.parentActivity = parentActivity;
    }


    @NonNull
    @Override
    public WartegViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_best_menu, parent, false);
        return new WartegViewHolder(view);
    }



    public void onBindViewHolder(@NonNull DetailWartegAdapter.WartegViewHolder holder, int position) {
        holder.menuName.setText(datalist.get(position).getName());
        Glide.with(context)
                .load(datalist.get(position).getPhoto_profile())
                .apply(new RequestOptions().override(200, 100))
                .into(holder.imgMenu);

    }



    public int getItemCount() {
        return (datalist != null) ? datalist.size() : 0;
    }

    public class WartegViewHolder extends RecyclerView.ViewHolder {
        TextView menuName;
        ImageView imgMenu;
        TextView description;

        WartegViewHolder(@NonNull View itemView) {
            super(itemView);
            menuName = itemView.findViewById(R.id.title);
            imgMenu= itemView.findViewById(R.id.img_menu);
            description = itemView.findViewById(R.id.description);
        }

        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    void setClickListener(DetailWartegAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    String getItem(int id) {
        return datalist.get(id).getName();
    }
}
