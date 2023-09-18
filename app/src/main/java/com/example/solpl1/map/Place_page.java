package com.example.solpl1.map;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.solpl1.R;

public class Place_page extends AppCompatActivity {

    TextView place_title;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //Intent intent = getIntent();
        //place_title=findViewById(R.id.place_title);
        //place_title.setText(intent.getStringExtra("title"));
    }
}
