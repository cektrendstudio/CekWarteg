package com.cektrend.cekwarteg.adapter;

import android.app.Activity;
import android.content.Context;
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
import com.cektrend.cekwarteg.DataMenuFragment;
import com.cektrend.cekwarteg.R;
import com.cektrend.cekwarteg.data.DataMenuOwner;

import java.util.ArrayList;

public class DataMenuOwnerAdapter extends RecyclerView.Adapter<DataMenuOwnerAdapter.ViewHolder> {
    private ArrayList<DataMenuOwner> datalist;
    private ItemClickListener mClickListener;
    private Context context;
    private Activity parentActivity;

    public DataMenuOwnerAdapter(ArrayList<DataMenuOwner> datalist, Context context, Activity parentActivity) {
        this.datalist = datalist;
        this.context = context;
        this.parentActivity = parentActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_row_data_menu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvMenuName.setText(datalist.get(position).getName());
        Glide.with(context)
                .load(datalist.get(position).getPhoto())
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
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(10)))
                .into(holder.imgPhotoMenu);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Menu " + datalist.get(position).getName() + " Di Klik!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (datalist != null) ? datalist.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvMenuName;
        ImageView imgPhotoMenu;
        ProgressBar progressBar;
        ConstraintLayout layout;
        private final Context context;

        ViewHolder(View itemView) {
            super(itemView);

            context = itemView.getContext();
            tvMenuName = itemView.findViewById(R.id.tv_menu_name);
            imgPhotoMenu = itemView.findViewById(R.id.img_data_menu);
            progressBar = itemView.findViewById(R.id.loading_img);
            layout = itemView.findViewById(R.id.layout);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    String getItem(int id) {
        return datalist.get(id).getName();
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
