package com.tacocat.huistaak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

public class taakActivity extends AppCompatActivity {

    private static boolean isNumeric(String str) {
        return str != null && str.matches("[-+]?\\d*\\.?\\d+");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taak);

        TextView taakTextView = (TextView) findViewById(R.id.taakTextView);

        // get current week and the according task to the selected user.

        int currentWeek = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
        TextView weekNTextView = (TextView) findViewById(R.id.weekNTextView);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String name = preferences.getString("name", ":("); //"" is the default String to return if the preference isn't found

        // trying the server shit
        // and aparrently android doesn't like to have network stuff on the main thread lol
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // url with the unique roster id
                    String parsedString = "";
                    URL url = new URL("https://testing.tacocat.site/test.txt");
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
                        String[] data = new String[32];
                        String test = "";
                        int taskIndex = 0;
                        String line = "";
                        String splitBy = ",";

                        boolean done = false;
                        while ((line = reader.readLine()) != null && !done) {
                            data = line.split(splitBy);
                            test = data[0];
                            if (isNumeric(data[0]) && Integer.parseInt(data[0]) <= currentWeek) {
                                // find the index of the name (and start counting past the week number
                                for (int i = 1; i<data.length; i++) {
                                    if (data[i].equals(name)) {
                                        parsedString = "In week " + currentWeek +
                                                // name from the preferences
                                                " moet " + name +
                                                " de " + tasks[i-1] + " doen!";
                                        done = true;
                                        break;

                                    }
                                }
                            // todo: fix the blindly adding tasks when the first line of the stream isn't a numeral
                            } else {
                                for (int i = 0; i < data.length-1; i++) {
                                    tasks[i] = data[i+1];
                                }
                            }
                        }

                        // setting the correct text
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("taakText", parsedString);
                        editor.apply();

                        // Apply the edits!
                        editor.apply();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        thread.start();

        while (thread.isAlive()) {}

        String test = "";
        test = preferences.getString("taakText", ":(");
        taakTextView.setText(test);


    }
}