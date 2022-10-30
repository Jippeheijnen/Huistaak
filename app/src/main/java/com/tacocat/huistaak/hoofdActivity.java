package com.tacocat.huistaak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;

public class hoofdActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                //set the event you want to perform when button is clicked
                //you can go to another activity in your app by creating Intent
                Intent intent = new Intent(getApplicationContext(), InstellingenActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoofd);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);


        Button button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // trying the server shit
                // and aparrently android doesn't like to have network stuff on the main thread lol
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try  {
                            // url with the unique roster id
                            URL url = new URL("https://testing.tacocat.site/test.txt");
                            URLConnection conn = url.openConnection();

                            HttpURLConnection httpConn = (HttpURLConnection) conn;
                            httpConn.setAllowUserInteraction(false);
                            httpConn.setInstanceFollowRedirects(true);
                            httpConn.setRequestMethod("GET");
                            httpConn.connect();

                            InputStream is = httpConn.getInputStream();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                thread.start();



                Intent intent = new Intent(getApplicationContext(), taakActivity.class);
                startActivity(intent);

            }

        });

        int currentWeek = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
        TextView weekNTextView = (TextView)findViewById(R.id.weekNTextView);
        String thisWeek = "Week " + currentWeek;
        weekNTextView.setText(thisWeek);
    }
}