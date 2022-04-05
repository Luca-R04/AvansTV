package com.avans.avanstv.Presentation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avans.avanstv.R;
import com.google.android.material.button.MaterialButton;


public class SettingsContained extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings_contained, container, false);

        getChildFragmentManager().beginTransaction().replace(R.id.rootPreferenceHolder, new SettingsFragment()).commit();


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        MaterialButton backArrow = view.findViewById(R.id.settings_btn_back);

        backArrow.setOnClickListener(view1 -> Navigation.findNavController(view1).navigate(R.id.action_settingsContained_to_profileFragment));
    }
}