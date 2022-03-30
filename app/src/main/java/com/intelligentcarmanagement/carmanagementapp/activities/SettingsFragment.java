package com.intelligentcarmanagement.carmanagementapp.activities;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.intelligentcarmanagement.carmanagementapp.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}
