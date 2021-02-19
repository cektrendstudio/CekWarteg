package com.cektrend.cekwarteg;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.cektrend.cekwarteg.adapter.DataMenuOwnerAdapter;
import com.cektrend.cekwarteg.data.DataMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DataMenuFragment extends Fragment {
    ProgressDialog pDialog;
    DataMenuOwnerAdapter adapter;
    ArrayList<DataMenu> DataMenu = new ArrayList<DataMenu>();
    private RecyclerView recyclerView;

    @SuppressLint ("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_data_menu, container, false);
        AndroidNetworking.initialize(getActivity());
        recyclerView = root.findViewById(R.id.rv_data_menu);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        tampilDataMenu();
        return root;
    }

    private void tampilDataMenu() {
        //koneksi ke file create.php, jika menggunakan localhost gunakan ip sesuai dengan ip kamu
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        pDialog.setMessage("Memuat ...");
        showDialog();
        //mengirim permintaan untuk menampilkan data barang dari API tampil_barang
        AndroidNetworking.get(BuildConfig.BASE_URL + "getMenu/")
                //            mengirimkan parameter get
                .addQueryParameter("token", "202143200272")
                .setPriority(Priority.MEDIUM).build().getAsJSONArray(new JSONArrayRequestListener() {
            @Override
            public void onResponse(JSONArray response) {
                //    Handle response
                hideDialog();
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject data = response.getJSONObject(i);
                        //    Memasukan data ke dalam class
                        DataMenu.add(new DataMenu(data.getInt("id"), data.getString("code"), data.getString("name"), data.getInt("warteg_id"), data.getInt("price"), data.getBoolean("is_have_stock"), data.getString("created_at"), data.getString("updated_at")));
                    }
                    //memasukan data kedalam adapter untuk ditampilkan
                    adapter = new DataMenuOwnerAdapter(DataMenu, getContext(), getActivity());
                    //    set adapter ke recycler adapter untuk ditampilkan
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    // JSON error
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
