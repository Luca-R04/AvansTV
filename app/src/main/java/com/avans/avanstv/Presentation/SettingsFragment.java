package com.avans.avanstv.Presentation;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.avans.avanstv.R;

import java.util.Locale;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        DarkModeToggleListener();
        LanguageListener();
    }

    //Listener for to update the preference immediately;
    private void DarkModeToggleListener() {
        Preference themeSwitch = findPreference("theme");
        themeSwitch.setOnPreferenceChangeListener((preference, newValue) -> {
            Boolean isDarkMode = (Boolean) newValue;
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            return true;
        });
    }

    private void LanguageListener() {
        Preference languagePreference = findPreference("language");
        languagePreference.setOnPreferenceChangeListener((preference, newValue) -> {
            String language = String.valueOf(newValue);

            Locale locale = new Locale(language);
            Locale.setDefault(locale);
            Resources resources = getResources();
            Configuration config = resources.getConfiguration();
            config.setLocale(locale);
            resources.updateConfiguration(config, resources.getDisplayMetrics());

            return true;
        });
    }
}