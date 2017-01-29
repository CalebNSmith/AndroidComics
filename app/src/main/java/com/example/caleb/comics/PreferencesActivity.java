package com.example.caleb.comics;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by Caleb on 10/11/15.
 */
public class PreferencesActivity extends android.preference.PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static class PreferencesFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

        }
    }
}
