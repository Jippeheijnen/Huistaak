package com.tacocat.huistaak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
        if (item.getItemId() == R.id.action_settings) {//set the event you want to perform when button is clicked
            //you can go to another activity in your app by creating Intent
            Intent intent = new Intent(getApplicationContext(), InstellingenActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoofd);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);


        Button button = (Button) findViewById(R.id.button);

        button.setOnClickListener(v -> {

            Intent intent = new Intent(getApplicationContext(), taakActivity.class);
            startActivity(intent);

        });

        int currentWeek = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
        TextView weekNTextView = (TextView)findViewById(R.id.weekNTextView);
        String thisWeek = "Week " + currentWeek;
        weekNTextView.setText(thisWeek);
    }
}