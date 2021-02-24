package com.cektrend.cekwarteg;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import com.cektrend.cekwarteg.adapter.DataMenuOwnerAdapter;
import com.cektrend.cekwarteg.data.DataMenuOwner;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.cektrend.cekwarteg.utils.ConstantUtil.MY_SHARED_PREFERENCES;
import static com.cektrend.cekwarteg.utils.ConstantUtil.TOKEN;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    EditText edtWartegName, edtEmail, edtOwnerName, edtAddress, edtPhone, edtUsername, edtDesc;
    String myToken, wartegName, email, ownerName, address, phone, description, photoProfile, username, failedResponse, messageResponse;
    Boolean isSuccess;
    Button btnEditWarteg;
    ImageView imgProfile;
    ProgressDialog pDialog;
    SharedPreferences sharedPreferences;
    ProgressBar progressBar;

    @SuppressLint ("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        initComponents(root);
        btnEditWarteg.setOnClickListener(this);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tampilDataProfile();
        super.onViewCreated(view, savedInstanceState);
    }

    private void tampilDataProfile() {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        pDialog.setMessage("Memuat ...");
        showDialog();
        AndroidNetworking.get(BuildConfig.BASE_URL + "api/auth/me")
                .addHeaders("Authorization", myToken)
                .setTag(this)
                .setPriority(Priority.LOW)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                hideDialog();
                try {
                    isSuccess = response.getBoolean("isSuccess");
                    if (isSuccess) {
                        JSONObject data = response.getJSONObject("data");
                        initResponse(data);
                        Glide.with(getActivity())
                                .load(photoProfile)
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        progressBar.setVisibility(View.GONE);
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        progressBar.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .error(R.drawable.ic_baseline_image_not_supported_24).diskCacheStrategy(DiskCacheStrategy.ALL)
                                .centerCrop()
                                .apply(RequestOptions.bitmapTransform(new RoundedCorners(10)))
                                .into(imgProfile);
                    } else {
                        failedResponse = response.getString("messages");
                        Toast.makeText(getActivity(), failedResponse, Toast.LENGTH_SHORT).show();
                        hideDialog();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError error) {
                Toast.makeText(getActivity(), error.getErrorBody(), Toast.LENGTH_SHORT).show();
                hideDialog();
            }
        });
    }

    public void editdata(String wartegName, String email, String ownerName, String address, String phone, String description, String username) {
        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);
        pDialog.setMessage("Verifikasi ...");

        showDialog();
        AndroidNetworking.post(BuildConfig.BASE_URL + "api/warteg/update")
                .addHeaders("Authorization", myToken)
                .setContentType("multipart/form-data")
                .addBodyParameter("name", wartegName)
                .addBodyParameter("email", email)
                .addBodyParameter("ownerName", ownerName)
                .addBodyParameter("address", address)
                .addBodyParameter("phone", phone)
                .addBodyParameter("description", description)
                .addBodyParameter("username", username)
                .setPriority(Priority.MEDIUM).build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                hideDialog();
                try {
                    isSuccess = response.getBoolean("isSuccess");
                    messageResponse = response.getString("messages");
                    if (isSuccess) {
                        JSONObject data = response.getJSONObject("data");
                        initResponse(data);
                        Toast.makeText(getContext(), messageResponse, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), messageResponse, Toast.LENGTH_SHORT).show();
                    }
                    hideDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError error) {
                Toast.makeText(getContext(), error.getErrorBody(), Toast.LENGTH_SHORT).show();
                hideDialog();
            }
        });
    }

    private void initResponse(JSONObject data) {
        try {
            wartegName = data.getString("name");
            email = data.getString("email");
            ownerName = data.getString("owner_name");
            address = data.getString("address");
            phone = data.getString("phone");
            description = data.getString("description");
            username = data.getString("username");
            photoProfile = data.getString("photo_profile");
            edtWartegName.setText(wartegName);
            edtEmail.setText(email);
            edtOwnerName.setText(ownerName);
            edtAddress.setText(address);
            edtPhone.setText(phone);
            edtDesc.setText(description);
            edtUsername.setText(username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initComponents(View view) {
        progressBar = view.findViewById(R.id.loading_img);
        edtWartegName = view.findViewById(R.id.edt_warteg_name);
        edtEmail = view.findViewById(R.id.edt_email);
        edtOwnerName = view.findViewById(R.id.edt_owner_name);
        edtAddress = view.findViewById(R.id.edt_address);
        edtPhone = view.findViewById(R.id.edt_phone);
        edtUsername = view.findViewById(R.id.edt_username);
        edtDesc = view.findViewById(R.id.edt_desc);
        btnEditWarteg = view.findViewById(R.id.btn_edit_warteg);
        imgProfile = view.findViewById(R.id.img_profile);
        sharedPreferences = getActivity().getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        myToken = sharedPreferences.getString(TOKEN, null);
    }

    private void showDialog() {
        if (!pDialog.isShowing()) pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing()) pDialog.dismiss();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_edit_warteg) {
            String wartegName = edtWartegName.getText().toString().trim();
            String ownerName = edtOwnerName.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String address = edtAddress.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();
            String description = edtDesc.getText().toString().trim();
            String username = edtUsername.getText().toString().trim();
            if (!wartegName.isEmpty() || !ownerName.isEmpty() || !email.isEmpty() || !address.isEmpty() || !phone.isEmpty() || !description.isEmpty() || !username.isEmpty()) {
                editdata(wartegName, email, ownerName, address, phone, description, username);
            } else {
                edtWartegName.setError("Jangan kosongkan kolom ini!");
                edtOwnerName.setError("Jangan kosongkan kolom ini!");
                edtEmail.setError("Jangan kosongkan kolom ini!");
                edtAddress.setError("Jangan kosongkan kolom ini!");
                edtPhone.setError("Jangan kosongkan kolom ini!");
                edtDesc.setError("Jangan kosongkan kolom ini!");
                edtUsername.setError("Jangan kosongkan kolom ini!");
            }
        }
    }
}

