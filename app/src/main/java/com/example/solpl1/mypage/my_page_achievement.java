package com.example.solpl1.mypage;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.devs.vectorchildfinder.VectorChildFinder;
import com.devs.vectorchildfinder.VectorDrawableCompat;
import com.example.solpl1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class my_page_achievement extends AppCompatActivity
{
    String reco_text;
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("UserAccount").child(firebaseAuth.getUid());
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.achievement_map);

        ImageView imageView = (ImageView) findViewById(R.id.achiv_korea);

        VectorChildFinder vector = new VectorChildFinder(this, R.drawable.map_of_south_korea, imageView);
        reco_text="1";
        Toast.makeText(this,reco_text,Toast.LENGTH_SHORT).show();
        if(reco_text.contains("오산")) {
            VectorDrawableCompat.VFullPath path = vector.findPathByName("오산");
            path.setFillColor(Color.RED);
        }
        if(reco_text.contains("광명")) {
            VectorDrawableCompat.VFullPath path1 = vector.findPathByName("광명");
            path1.setFillColor(Color.BLUE);
        }
        if(reco_text.contains("안산")) {
            VectorDrawableCompat.VFullPath path1 = vector.findPathByName("안산");
            path1.setFillColor(Color.BLACK);
        }
        if(reco_text.contains("안양")) {
            VectorDrawableCompat.VFullPath path1 = vector.findPathByName("안양");
            path1.setFillColor(Color.YELLOW);
        }
        if(reco_text.contains("순천")) {
            VectorDrawableCompat.VFullPath path1 = vector.findPathByName("순천");
            path1.setFillColor(Color.GREEN);
        }
        if(reco_text.contains("종로")) {
            VectorDrawableCompat.VFullPath path1 = vector.findPathByName("종로구");
            path1.setFillColor(Color.MAGENTA);
        }

        imageView.invalidate();

    }
}
