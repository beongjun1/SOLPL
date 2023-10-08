package com.example.solpl1.map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.solpl1.R;
import com.example.solpl1.mypage.RECO_RESION;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Place_page extends AppCompatActivity {

    private TextView place_title;
    private ImageView place_image;
    private TextView place_address,place_tel,place_rating;
    private RecyclerView recyclerView;
    private Button btn_review;
    RECO_RESION reco_resion;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("UserAccount").child(firebaseAuth.getUid()).child("reco_resion");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();
        place_title=findViewById(R.id.place_title);
        place_image=findViewById(R.id.place_image);
        place_address=findViewById(R.id.place_address);
        place_tel=findViewById(R.id.place_tel);
        place_rating=findViewById(R.id.place_rating);
        recyclerView=findViewById(R.id.review_recyclerview);
        btn_review=findViewById(R.id.btn_review);
        Intent intent = getIntent();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reco_resion=snapshot.getValue(RECO_RESION.class);

                place_title.setText(intent.getStringExtra("place_title"));
                place_image.setImageResource(intent.getIntExtra("place_image",0));
                place_address.setText(intent.getStringExtra("place_address"));
                place_tel.setText(intent.getStringExtra("place_tel"));
                if(intent.getDoubleExtra("place_rating",0)==0)place_rating.setText("평점:리뷰 수집중입니다.");
                else{
                    place_rating.setText("평점:"+intent.getDoubleExtra("place_rating",0));
                }

                btn_review.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(intent.getStringExtra("resion").equals("순천")&&reco_resion.get순천()>0){
                            Intent intent1 = new Intent(Place_page.this, Place_review.class);
                            intent1.putExtra("title",place_title.getText());
                            startActivity(intent1);
                        }
                        else{
                            Toast.makeText(Place_page.this,"리뷰 권한이 없습니다.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
