package com.example.solpl1.mypage;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.devs.vectorchildfinder.VectorChildFinder;
import com.devs.vectorchildfinder.VectorDrawableCompat;
import com.example.solpl1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class my_page_achievement extends AppCompatActivity
{
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("UserAccount").child(firebaseAuth.getUid());
    RECO_RESION reco_resion = new RECO_RESION();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.achievement_map);

        ImageView imageView = (ImageView) findViewById(R.id.achiv_korea);


        databaseReference.child("reco_resion").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RECO_RESION DB_RESION = snapshot.getValue(RECO_RESION.class);
                reco_resion.set고양(DB_RESION.get고양());
                reco_resion.set순천(DB_RESION.get순천());
                reco_resion.set용인(DB_RESION.get용인());
                reco_resion.set시흥(DB_RESION.get시흥());
                reco_resion.set진주(DB_RESION.get진주());
                reco_resion.set제주(DB_RESION.get제주());
                reco_resion.set화성(DB_RESION.get화성());
                reco_resion.set수원(DB_RESION.get수원());
                reco_resion.set오산(DB_RESION.get오산());

                VectorChildFinder vector = new VectorChildFinder(my_page_achievement.this, R.drawable.map_of_south_korea, imageView);

                if(reco_resion.get오산()>0) {
                    VectorDrawableCompat.VFullPath path = vector.findPathByName("오산");
                    path.setFillColor(Color.BLUE);
                }
                if(reco_resion.get용인()>0) {
                    VectorDrawableCompat.VFullPath path = vector.findPathByName("용인");
                    path.setFillColor(Color.BLUE);
                }
                if(reco_resion.get고양()>0) {
                    VectorDrawableCompat.VFullPath path = vector.findPathByName("고양");
                    path.setFillColor(Color.BLUE);
                }
                if(reco_resion.get시흥()>0) {
                    VectorDrawableCompat.VFullPath path = vector.findPathByName("시흥");
                    path.setFillColor(Color.BLUE);
                }
                if(reco_resion.get수원()>0) {
                    VectorDrawableCompat.VFullPath path = vector.findPathByName("수원");
                    path.setFillColor(Color.BLUE);
                }
                if(reco_resion.get화성()>0) {
                    VectorDrawableCompat.VFullPath path = vector.findPathByName("화성");
                    path.setFillColor(Color.BLUE);
                }
                if(reco_resion.get제주()>0) {
                    VectorDrawableCompat.VFullPath path = vector.findPathByName("제주");
                    path.setFillColor(Color.BLUE);
                }
                if(reco_resion.get진주()>0) {
                    VectorDrawableCompat.VFullPath path = vector.findPathByName("진주");
                    path.setFillColor(Color.BLUE);
                }
                if(reco_resion.get순천()>0) {
                    VectorDrawableCompat.VFullPath path = vector.findPathByName("순천");
                    path.setFillColor(Color.BLUE);
                }

                imageView.invalidate();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}
