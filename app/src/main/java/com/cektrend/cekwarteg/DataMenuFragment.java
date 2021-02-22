package com.cektrend.cekwarteg;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.cektrend.cekwarteg.adapter.DataMenuOwnerAdapter;
import com.cektrend.cekwarteg.data.DataMenuOwner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.cektrend.cekwarteg.utils.ConstantUtil.MY_SHARED_PREFERENCES;
import static com.cektrend.cekwarteg.utils.ConstantUtil.WARTEG_ID;

public class DataMenuFragment extends Fragment {
    ProgressDialog pDialog;
    DataMenuOwnerAdapter adapter;
    ArrayList<DataMenuOwner> DataMenuOwner = new ArrayList<DataMenuOwner>();
    private RecyclerView recyclerView;
    private String wartegId;
    SharedPreferences sharedPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_data_menu, container, false);
        recyclerView = root.findViewById(R.id.rv_data_menu);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        sharedPreferences = getActivity().getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        wartegId = sharedPreferences.getString(WARTEG_ID, null);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AndroidNetworking.initialize(view.getContext());
        tampilDataMenu();
    }

    private void tampilDataMenu() {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        pDialog.setMessage("Memuat ...");
        showDialog();
        AndroidNetworking.get(BuildConfig.BASE_URL + "api/warteg/{idWarteg}")
                .addPathParameter("idWarteg", String.valueOf(wartegId))
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideDialog();
                        try {
                            Log.e("TAG", "Nama Wateg : " + response.getJSONObject("data").getString("name"));
                            JSONArray jsonArray = new JSONArray(response.getJSONObject("data").getString("menu"));
                            // Log.e("TAG", "Data Menu : " + jsonArray.get(0));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    // Log.e("TAG", "Message : " + data.getString("name"));
                                    DataMenuOwner.add(new DataMenuOwner(data.getInt("id"), data.getString("code"), data.getString("name"), data.getInt("warteg_id"), data.getInt("price"), data.getBoolean("is_have_stock"), data.getString("created_at"), data.getString("updated_at"), data.getString("photo") , data.getString("description")));
                                    adapter = new DataMenuOwnerAdapter(DataMenuOwner, getContext(), getActivity());
                                    recyclerView.setAdapter(adapter);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        //    Handle error
                        Toast.makeText(getContext(), "Gagal memuat, cobalah periksa koneksi internet anda", Toast.LENGTH_SHORT).show();
                        //    memunculkan Toast saat data gagal ditampilkan
                        hideDialog();
                    }
                });
    }

    private void showDialog() {
        if (!pDialog.isShowing()) pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing()) pDialog.dismiss();
    }
}
