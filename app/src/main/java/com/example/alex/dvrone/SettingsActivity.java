package com.example.alex.dvrone;

import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.os.Bundle;

public class SettingsActivity extends PreferenceActivity {

    private EditTextPreference lengthPref, cameraZoomPreferance, maxMemorySize;

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
        lengthPref = (EditTextPreference) findPreference("videoLengthKey");
        lengthPref.setSummary(lengthPref.getText());
        lengthPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                lengthPref.setSummary(o.toString());
                return true;
            }
        });
        maxMemorySize = (EditTextPreference) findPreference("memorySizeKey");
        maxMemorySize.setSummary(maxMemorySize.getText());
        maxMemorySize.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                lengthPref.setSummary(o.toString());
                return true;
            }
        });
    }
}
