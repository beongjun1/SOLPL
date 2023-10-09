package com.example.solpl1.Badge;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.devs.vectorchildfinder.VectorChildFinder;
import com.devs.vectorchildfinder.VectorDrawableCompat;
import com.example.solpl1.R;

public class Touch_Resion extends AppCompatActivity {
    Button select_badge;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.achievement_map);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();

        ImageView imageView = (ImageView) findViewById(R.id.achiv_korea);
        select_badge = (Button) findViewById(R.id.select_badge);
        select_badge.setVisibility(View.VISIBLE);
        Intent intent=getIntent();
        String resion=intent.getStringExtra("resion");
        int count=intent.getIntExtra("count",0);

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

        select_badge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count==0){
                    Toast.makeText(Touch_Resion.this,"뱃지가 아직 잠겨 있습니다.",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(Touch_Resion.this,"대표 뱃지로 설정되었습니다.",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}
