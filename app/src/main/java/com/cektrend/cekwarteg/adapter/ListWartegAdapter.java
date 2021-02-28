package com.cektrend.cekwarteg.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.cektrend.cekwarteg.R;
import com.cektrend.cekwarteg.activity.DetailWartegActivity;
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
        holder.wartegDesc.setText(datalist.get(position).getDescription());
        Glide.with(context)
                .load(datalist.get(position).getPhoto_profile())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                }).error(R.drawable.ic_baseline_image_not_supported_24).diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(holder.imgWarteg);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detWarteg = new Intent(context, DetailWartegActivity.class);
                detWarteg.putExtra("id", datalist.get(position).getId());
                detWarteg.putExtra("photo_profile", datalist.get(position).getPhoto_profile());
                detWarteg.putExtra("name", datalist.get(position).getName());
                detWarteg.putExtra("description", datalist.get(position).getDescription());
                detWarteg.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(detWarteg);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (datalist != null) ? datalist.size() : 0;
    }

    public class WartegViewHolder extends RecyclerView.ViewHolder {
        TextView wartegName, wartegDesc;
        ImageView imgWarteg;
        ConstraintLayout layout;
        ProgressBar progressBar;

        WartegViewHolder(@NonNull View itemView) {
            super(itemView);
            wartegName = itemView.findViewById(R.id.warteg_name);
            imgWarteg = itemView.findViewById(R.id.img_warteg);
            wartegDesc = itemView.findViewById(R.id.tv_desc);
            layout = itemView.findViewById(R.id.layout);
            progressBar = itemView.findViewById(R.id.loading_img);
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

    public void setFilter(ArrayList<DataWarteg> filterList) {
        datalist = new ArrayList<>();
        datalist.addAll(filterList);
        notifyDataSetChanged();
    }

}
