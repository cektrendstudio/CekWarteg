package com.cektrend.cekwarteg;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.cektrend.cekwarteg.R;

import java.util.Objects;

public class UlasanFragment extends Fragment {

    @SuppressLint ("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_ulasan, container, false);
        // final TextView textView = root.findViewById(R.id.text_home);
        // Button btnResistor = root.findViewById(R.id.btnResistor);
        // Button btnKapasitor = root.findViewById(R.id.btnKapasitor);
        // Button btnListrik = root.findViewById(R.id.btnListrik);
        // Button btnToko = root.findViewById(R.id.btnToko);
        // Button btnMateri = root.findViewById(R.id.btnMateri);
        // TextView tvNama = root.findViewById(R.id.text_home);

        // final Intent actResistor = new Intent(getActivity(), ResistorActivity.class);
        // final Intent actKapasitor = new Intent(getActivity(), KapasitorActivity.class);
        // final Intent actListrik = new Intent(getActivity(), DasarListrikActivity.class);
        // final Intent materiElektro = new Intent(getActivity(), MateriActivity.class);
        // final String name = Objects.requireNonNull(Objects.requireNonNull(getActivity()).getIntent().getExtras()).getString("NAMA");
        // tvNama.setText("Hello, " + name);


        // btnResistor.setOnClickListener(new View.OnClickListener() {
        //     @Override
        //     public void onClick(View view) {
        //         startActivity(actResistor);
        //     }
        // });

        return root;
    }
}
