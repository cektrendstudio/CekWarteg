package com.cektrend.cekwarteg.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import com.cektrend.cekwarteg.R;
import com.cektrend.cekwarteg.activity.DetailMenuActivity;
import com.cektrend.cekwarteg.data.DataMenuOwner;
import com.cektrend.cekwarteg.data.DataWarteg;

import java.util.ArrayList;

public class DetailWartegAdapter extends RecyclerView.Adapter<DetailWartegAdapter.WartegViewHolder> {
    private ArrayList<DataMenuOwner> datalist;
    private DetailWartegAdapter.ItemClickListener mClickListener;
    private Context context;
    private Activity parentActivity;


    public DetailWartegAdapter(ArrayList<DataMenuOwner> datalist, Context mCtx,   Activity parentActivity) {
        this.datalist = datalist;
        this.context = mCtx;
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
        holder.description.setText(datalist.get(position).getDescription());
        Glide.with(context)
                .load(datalist.get(position).getPhoto())
                .apply(new RequestOptions().override(200, 100))
                .into(holder.imgMenu);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detWarteg = new Intent(context, DetailMenuActivity.class);
                detWarteg.putExtra("id", String.valueOf(datalist.get(position).getId()));
                Log.d("TAG", "menuid : " + datalist.get(position).getId());
                detWarteg.putExtra("photo_profile", datalist.get(position).getPhoto());
                detWarteg.putExtra("name", datalist.get(position).getName());
                detWarteg.putExtra("description", datalist.get(position).getDescription());
                detWarteg.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(detWarteg);
            }
        });

    }



    public int getItemCount() {
        return (datalist != null) ? datalist.size() : 0;
    }

    public class WartegViewHolder extends RecyclerView.ViewHolder {
        TextView menuName;
        ImageView imgMenu;
        TextView description;
        ConstraintLayout layout;

        WartegViewHolder(@NonNull View itemView) {
            super(itemView);
            menuName = itemView.findViewById(R.id.title);
            imgMenu= itemView.findViewById(R.id.img_menu);

            description = itemView.findViewById(R.id.description);
            layout = itemView.findViewById(R.id.layout);
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
