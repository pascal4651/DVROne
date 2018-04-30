package com.example.alex.dvrone;

import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SettingsActivity extends PreferenceActivity {

    private Preference lengthPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
        //setContentView(R.layout.activity_settings);

        lengthPref = getPreferenceManager().findPreference("length");
        lengthPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                lengthPref.setSummary(newValue.toString());
                return true;
            }
        });
    }
}
