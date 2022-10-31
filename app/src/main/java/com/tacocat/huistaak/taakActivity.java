package com.tacocat.huistaak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;

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
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String name = preferences.getString("name", ":("); //"" is the default String to return if the preference isn't found

        // trying the server shit
        // and aparrently android doesn't like to have network stuff on the main thread lol
        Thread thread = new Thread(() -> {
            try {
                // url with the unique roster id
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
                    String line;
                    String splitBy = ",";

                    boolean done = false;
                    while ((line = reader.readLine()) != null && !done) {
                        data = line.split(splitBy);
                        if (isNumeric(data[0]) && Integer.parseInt(data[0]) <= currentWeek) {
                            // find the index of the name (and start counting past the week number
                            for (int i = 1; i<data.length; i++) {
                                if (data[i].equals(name)) {
                                    parsedString = "In week " + currentWeek +
                                            // name from the preferences
                                            " moet " + name +
                                            " de " + tasks[i] + " doen!";
                                    done = true;
                                    break;

                                }
                            }
                        // todo: fix the blindly adding tasks when the first line of the stream isn't a numeral
                        } else {
                            // set the first row to be the task names.
                            System.arraycopy(data, 1, tasks, 0, data.length - 1);
                        }
                    }

                    // setting the correct text
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("taakText", parsedString);
                    editor.apply();

                    // Apply the edits!
                    editor.apply();

                // logic errors here
                } catch (FileNotFoundException e) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("taakText", "File not found error");
                    editor.apply();
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            // network errors here
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

        thread.start();

        // wait (blocking) for the logic to be processed
        //noinspection StatementWithEmptyBody
        while (thread.isAlive()) {}

        String s;
        // retrieve the parsed csv roster string
        s = preferences.getString("taakText", ":(");
        taakTextView.setText(s);


    }
}