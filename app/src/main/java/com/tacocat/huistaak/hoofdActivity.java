package com.tacocat.huistaak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoofd);

        Button button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // trying the server shit
                try {

                    // url with the unique roster id
                    URL url = new URL("");
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


                //set the event you want to perform when button is clicked
                //you can go to another activity in your app by creating Intent
                Intent intent = new Intent(getApplicationContext(), InstellingenActivity.class);
                startActivity(intent);
            }
        });

        int currentWeek = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
        TextView weekNTextView = (TextView)findViewById(R.id.weekNTextView);
        String thisWeek = "Week " + currentWeek;
        weekNTextView.setText(thisWeek);
    }
}