package com.cektrend.cekwarteg.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cektrend.cekwarteg.R;

public class BestMenuAdapter extends RecyclerView.Adapter<BestMenuAdapter.ViewHolder> {
    @NonNull
    @Override
    public BestMenuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
//        View view = layoutInflater.inflate(R.layout.item_best_menu, parent, false);
//        return new ViewHolder(view);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BestMenuAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
