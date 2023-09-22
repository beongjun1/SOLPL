package com.example.solpl1.Badge;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.devs.vectorchildfinder.VectorChildFinder;
import com.devs.vectorchildfinder.VectorDrawableCompat;
import com.example.solpl1.R;

public class Touch_Resion extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.achievement_map);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        ImageView imageView = (ImageView) findViewById(R.id.achiv_korea);
        Intent intent=getIntent();
        String resion=intent.getStringExtra("resion");
        VectorChildFinder vector = new VectorChildFinder(Touch_Resion.this, R.drawable.map_of_south_korea, imageView);
        VectorDrawableCompat.VFullPath path=null;
        switch (resion){
            case "goyang":
                path = vector.findPathByName("고양");
                break;
            case "suwon":
                path = vector.findPathByName("수원");
                break;
            case "siheung":
                path = vector.findPathByName("시흥");
                break;
            case "osan":
                path = vector.findPathByName("오산");
                break;
            case "yongin":
                path = vector.findPathByName("용인");
                break;
            case "hwaseong":
                path = vector.findPathByName("화성");
                break;
            case "suncheon":
                path = vector.findPathByName("순천");
                break;
            case "jinju":
                path = vector.findPathByName("진주");
                break;
            case "jeju":
                path = vector.findPathByName("제주");
                break;
        }

        path.setFillColor(Color.RED);
    }
}
