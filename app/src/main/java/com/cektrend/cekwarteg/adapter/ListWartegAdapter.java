package com.cektrend.cekwarteg.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cektrend.cekwarteg.DetailWartegActivity;
import com.cektrend.cekwarteg.R;
import com.cektrend.cekwarteg.data.DataWarteg;

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
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Warteg " + datalist.get(position).getName() + " Di Klik!", Toast.LENGTH_SHORT).show();
                Intent detWarteg = new Intent(context, DetailWartegActivity.class);
                detWarteg.putExtra("id", datalist.get(position).getId());
                detWarteg.putExtra("photo_profile", datalist.get(position).getPhoto_profile());
                detWarteg.putExtra("name", datalist.get(position).getName());
                detWarteg.putExtra("description", datalist.get(position).getDescription());
                detWarteg.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(detWarteg);
                parentActivity.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (datalist != null) ? datalist.size() : 0;
    }

    public class WartegViewHolder extends RecyclerView.ViewHolder {
        TextView wartegName;
        ImageView imgWarteg;
        ConstraintLayout layout;

        WartegViewHolder(@NonNull View itemView) {
            super(itemView);
            wartegName = itemView.findViewById(R.id.warteg_name);
            imgWarteg = itemView.findViewById(R.id.img_warteg);
            layout = itemView.findViewById(R.id.layout);
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
