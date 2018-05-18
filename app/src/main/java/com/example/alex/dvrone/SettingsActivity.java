package com.example.alex.dvrone;

import android.content.SharedPreferences;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.os.Bundle;

public class SettingsActivity extends PreferenceActivity {

    private EditTextPreference lengthPref;
    private EditTextPreference cameraZoomPreferance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
        //setContentView(R.layout.activity_settings);

        cameraZoomPreferance = (EditTextPreference)findPreference("zoomKey");
        cameraZoomPreferance.setSummary(cameraZoomPreferance.getText());
        cameraZoomPreferance.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                cameraZoomPreferance.setSummary(o.toString());
                return true;
            }
        });
        lengthPref = (EditTextPreference) findPreference("length");
        lengthPref.setSummary(lengthPref.getText());
        lengthPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                lengthPref.setSummary(o.toString());
                return true;
            }
        });
    }
}
