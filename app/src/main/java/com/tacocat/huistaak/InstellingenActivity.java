package com.tacocat.huistaak;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.EventListener;

public class InstellingenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instellingen);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {// API 5+ solution
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            // Preference root
            PreferenceScreen root = getPreferenceManager().getPreferenceScreen();
            // PREFERENCE device
            final PreferenceCategory cat = (PreferenceCategory) root.getPreference(0);
            final EditTextPreference editTextPref = (EditTextPreference) cat.getPreference(0);
            editTextPref.setOnPreferenceChangeListener((preference, newValue) -> {
                //newValue will be the entryValue for the entry selected
                Intent intent = new Intent(getActivity(), InstellingenActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                getActivity().finish();
                startActivity(intent);
                return true;
            });

            Thread thread = new Thread(() -> {
                try {
                    // url with the unique roster id
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                    String parsedString = "";
                    String urlString = "https://testing.tacocat.site/" +
                            preferences.getString("roster_id", ":(") + ".txt";
                    URL url = new URL(urlString);
                    URLConnection conn = url.openConnection();

                    HttpURLConnection httpConn = (HttpURLConnection) conn;
                    httpConn.setAllowUserInteraction(false);
                    httpConn.setInstanceFollowRedirects(true);
                    httpConn.setRequestMethod("GET");
                    httpConn.connect();

                    try {
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(httpConn.getInputStream(),
                                        StandardCharsets.UTF_8));

                        String[] tasks = new String[32];
                        String[] data;
                        String line = null;
                        String splitBy = ",";

                        boolean done = false;
                        for (int i = 0; i<2; i++){
                            line = reader.readLine();
                        }
                        data = line.split(splitBy);

                        // setting the names from the roster to the preferences here
                        CharSequence[] entries = Arrays.copyOfRange(data, 1, data.length);

                        // PREFERENCE device
                        final ListPreference listPref = (ListPreference) cat.getPreference(1);
                        listPref.setEntries(entries);
                        listPref.setEntryValues(entries);
                        listPref.setOnPreferenceChangeListener((preference, newValue) -> {
                        //newValue will be the entryValue for the entry selected
                            return true;
                        });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            thread.start();

        }
    }
}