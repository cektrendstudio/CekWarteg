package com.cektrend.cekwarteg.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.cektrend.cekwarteg.BuildConfig;
import com.cektrend.cekwarteg.DetailMenuOwnerActivity;
import com.cektrend.cekwarteg.R;
import com.cektrend.cekwarteg.data.DataMenuOwner;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.cektrend.cekwarteg.utils.ConstantUtil.*;

public class DataMenuOwnerAdapter extends RecyclerView.Adapter<DataMenuOwnerAdapter.ViewHolder> {
    private ArrayList<DataMenuOwner> datalist;
    private ItemClickListener mClickListener;
    private Context context;
    private Activity parentActivity;
    String myToken, messageResponse;
    Boolean isSuccess;
    SharedPreferences sharedPreferences;
    ProgressDialog pDialog;

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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View deleteLayout;
        TextView tvMenuName, tvMenuDesc;
        ImageView imgPhotoMenu;
        ProgressBar progressBar;
        ConstraintLayout layout;
        private final Context context;

        ViewHolder(View itemView) {
            super(itemView);

            context = itemView.getContext();
            tvMenuName = itemView.findViewById(R.id.tv_menu_name);
            tvMenuDesc = itemView.findViewById(R.id.tv_menu_desc);
            imgPhotoMenu = itemView.findViewById(R.id.img_data_menu);
            progressBar = itemView.findViewById(R.id.loading_img);
            layout = itemView.findViewById(R.id.layout);
            deleteLayout = itemView.findViewById(R.id.delete_layout);
            sharedPreferences = context.getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
            myToken = sharedPreferences.getString(TOKEN, null);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
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
        holder.tvMenuDesc.setText(datalist.get(position).getDescription());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailMenu = new Intent(context, DetailMenuOwnerActivity.class);
                detailMenu.putExtra(MENU_ID, String.valueOf(datalist.get(position).getId()));
                detailMenu.putExtra(MENU_NAME, datalist.get(position).getName());
                detailMenu.putExtra(MENU_DESC, datalist.get(position).getDescription());
                detailMenu.putExtra(MENU_PHOTO, datalist.get(position).getPhoto());
                detailMenu.putExtra(MENU_PRICE, String.valueOf(datalist.get(position).getPrice()));
                context.startActivity(detailMenu);
            }
        });
        holder.deleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hapusdata(position, String.valueOf(datalist.get(position).getId()));
            }
        });
    }

    private void hapusdata(final int position, String idMenu) {
        pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);
        pDialog.setMessage("Verifikasi ...");

        showDialog();
        AndroidNetworking.post(BuildConfig.BASE_URL + "api/menu/{idMenu}/delete")
                .addHeaders("Authorization", myToken)
                .addPathParameter("idMenu", String.valueOf(idMenu))
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            isSuccess = response.getBoolean("isSuccess");
                            messageResponse = response.getString("messages");
                            if (isSuccess) {
                                datalist.remove(position);
                                notifyItemRemoved(position);
                                Toast.makeText(context, messageResponse, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, messageResponse, Toast.LENGTH_SHORT).show();
                            }
                            hideDialog();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(context, error.getErrorBody(), Toast.LENGTH_SHORT).show();
                        hideDialog();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return (datalist != null) ? datalist.size() : 0;
    }

    String getItem(int id) {
        return datalist.get(id).getName();
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    private void showDialog() {
        if (!pDialog.isShowing()) pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing()) pDialog.dismiss();
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
