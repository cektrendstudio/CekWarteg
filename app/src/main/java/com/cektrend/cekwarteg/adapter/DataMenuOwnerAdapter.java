package com.cektrend.cekwarteg.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cektrend.cekwarteg.R;
import com.cektrend.cekwarteg.data.DataMenu;

import java.util.ArrayList;

public class DataMenuOwnerAdapter extends RecyclerView.Adapter<DataMenuOwnerAdapter.ViewHolder> {
    private ArrayList<DataMenu> datalist;
    private ItemClickListener mClickListener;
    private Context context;
    private Activity parentActivity;

    public DataMenuOwnerAdapter(ArrayList<DataMenu> datalist, Context context, Activity parentActivity) {
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
    }

    @Override
    public int getItemCount() {
        return (datalist != null) ? datalist.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvMenuName;
        private Context context;

        ViewHolder(View itemView) {
            super(itemView);

            context = itemView.getContext();
            tvMenuName = itemView.findViewById(R.id.tv_menu_name);
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

    void setFilter(ArrayList<DataMenu> filterList) {
        datalist = new ArrayList<>();
        datalist.addAll(filterList);
        notifyDataSetChanged();
        ;
    }
}
