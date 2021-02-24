package com.cektrend.cekwarteg.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cektrend.cekwarteg.R;
import com.cektrend.cekwarteg.data.DataUlasan;

import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class UlasanAdapter extends RecyclerView.Adapter<UlasanAdapter.UlasanViewHolder> {

    private ArrayList<DataUlasan> datalist;
    private DataMenuOwnerAdapter.ItemClickListener mClickListener;
    private Context context;
    private Activity parentActivity;

    public UlasanAdapter(ArrayList<DataUlasan> datalist, Context context, Activity parentActivity) {
        this.datalist = datalist;
        this.context = context;
        this.parentActivity = parentActivity;
    }

    @NonNull
    @Override
    public UlasanAdapter.UlasanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_ulasan, parent, false);
        return new UlasanViewHolder(view);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onBindViewHolder(@NonNull UlasanAdapter.UlasanViewHolder holder, int position) {
        holder.tvName.setText(datalist.get(position).getName());
        holder.tvUlasan.setText(datalist.get(position).getReviewText());
        String strDate = datalist.get(position).getCreated_at();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSX");
            OffsetDateTime odt = OffsetDateTime.parse(strDate, formatter);
            String tgl = DateTimeFormatter.ofPattern("dd MMM uuuu", Locale.ENGLISH).format(odt);
            holder.tvCreated.setText(tgl);
        }

    }

    @Override
    public int getItemCount() {
        return (datalist != null) ? datalist.size() : 0;
    }

    public class UlasanViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvUlasan, tvCreated;

        public UlasanViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_reviewer);
            tvUlasan = itemView.findViewById(R.id.tv_ulasan);
            tvCreated = itemView.findViewById(R.id.tv_created);
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
