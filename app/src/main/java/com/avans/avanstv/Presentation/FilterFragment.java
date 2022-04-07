package com.avans.avanstv.Presentation;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.avans.avanstv.R;

import java.util.Locale;

public class FilterFragment extends PreferenceFragmentCompat {
    private static final String TAG = FilterFragment.class.getSimpleName();


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.filter_preference, rootKey);
        SortTitle();
        SortDate();
        FilterGenre();
    }

    //Listener for to update the preference immediately;
    private void SortTitle() {
        Preference sortTitle = findPreference("title");
        assert sortTitle != null;
        sortTitle.setOnPreferenceChangeListener((preference, newValue) -> {
            switch ((String) newValue){
                case "A-Z":
                    Log.d(TAG, "A-Z");
                    break;
                case "Z-A":
                    Log.d(TAG, "Z-A");
                    break;
                default:
                    Log.d(TAG, "Default");

            }
            return true;
        });
    }

    private void SortDate() {
        Preference sortTitle = findPreference("date");
        assert sortTitle != null;
        sortTitle.setOnPreferenceChangeListener((preference, newValue) -> {
            switch ((String) newValue){
                case "Most recent":
                    Log.d(TAG, "Most recent");

                case "Oldest":
                    Log.d(TAG, "Oldest");

                default:
                    Log.d(TAG, "Default");

            }
            return true;
        });
    }

    private void FilterGenre() {
        Preference sortTitle = findPreference("date");
        assert sortTitle != null;
        sortTitle.setOnPreferenceChangeListener((preference, newValue) -> {
            String genre = (String) newValue;
            Log.d(TAG, "FilterGenre: " + genre);

            return true;
        });
    }
}