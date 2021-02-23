package com.cektrend.cekwarteg;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class DashboardFragment extends Fragment {
    RatingBar ratingFoodMenu;
    TextView tvAmountRating, tvAmountViews, tvAmountFavorite, tvAmountVariant;

    @SuppressLint ("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        initComponents(root);

        return root;
    }

    private void initComponents(View root) {
        ratingFoodMenu = root.findViewById(R.id.rating_food_menu);
        tvAmountRating = root.findViewById(R.id.tv_amount_rating);
        tvAmountViews = root.findViewById(R.id.tv_amount_views);
        tvAmountFavorite = root.findViewById(R.id.tv_amount_favorite);
        tvAmountVariant = root.findViewById(R.id.tv_amount_variant);
    }
}
