package com.tacocat.huistaak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;
import java.util.prefs.Preferences;

public class taakActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taak);

        TextView taakTextView = (TextView)findViewById(R.id.taakTextView);

        // get current week and the according task to the selected user.

        int currentWeek = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
        TextView weekNTextView = (TextView)findViewById(R.id.weekNTextView);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String username = preferences.getString("name", ":("); //"" is the default String to return if the preference isn't found

        String result = username +
                " moet in week " +
                currentWeek +
                " heel veel doen!";
        taakTextView.setText(result);

    }
}