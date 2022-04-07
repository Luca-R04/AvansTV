package com.avans.avanstv.Presentation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.avans.avanstv.R;
import com.google.android.material.button.MaterialButton;

public class FilterContained extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter_contained, container, false);

        getChildFragmentManager().beginTransaction().replace(R.id.filterPreferenceHolder, new FilterFragment()).commit();


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        MaterialButton backArrow = view.findViewById(R.id.filter_btn_back);
        Button resetButton = view.findViewById(R.id.btn_reset);

        backArrow.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_filterContained_to_exploreFragment));
        resetButton.setOnClickListener(v -> {
            // TODO: make the reset button work
        });
    }

}