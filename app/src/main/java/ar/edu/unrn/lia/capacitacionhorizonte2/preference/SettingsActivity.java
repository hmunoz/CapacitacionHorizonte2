package ar.edu.unrn.lia.capacitacionhorizonte2.preference;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;

import ar.edu.unrn.lia.capacitacionhorizonte2.R;


public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingFragment())
                .commit();

    }

    public static class SettingFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
        private static final String TAG = SettingFragment.class.getSimpleName();

        SharedPreferences sharedPreferences;


        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.main);

            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

            PreferenceManager.getDefaultSharedPreferences(getActivity()).registerOnSharedPreferenceChangeListener(this);

            onSharedPreferenceChanged(sharedPreferences, getString(R.string.user_name_key));
            onSharedPreferenceChanged(sharedPreferences, getString(R.string.lista_opcion_key));
            onSharedPreferenceChanged(sharedPreferences, getString(R.string.lista_opcion_multiple_key));
            onSharedPreferenceChanged(sharedPreferences, getString(R.string.alert_email_address_key));
        }

        @Override
        public void onCreatePreferences(Bundle bundle, String s) {

        }


        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

            Preference preference = findPreference(key);
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue(sharedPreferences.getString(key, ""));
                if (prefIndex >= 0) {
                    preference.setSummary(listPreference.getEntries()[prefIndex]);
                }
            } else if (preference instanceof EditTextPreference) {
                preference.setSummary(sharedPreferences.getString(key, ""));
            }
        }
    }


}