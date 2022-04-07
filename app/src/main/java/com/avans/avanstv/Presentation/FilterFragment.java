package com.avans.avanstv.Presentation;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.avans.avanstv.R;

public class FilterFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.filter_preference, rootKey);
    }
}