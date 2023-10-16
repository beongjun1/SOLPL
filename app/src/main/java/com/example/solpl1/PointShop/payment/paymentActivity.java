package com.example.solpl1.PointShop.payment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.solpl1.PointShop.barcode.barcodeActivity;
import com.example.solpl1.PointShop.detail.detailActivity;
import com.example.solpl1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class paymentActivity extends AppCompatActivity
{
    ImageView payment_img;
    TextView payment_store,payment_title,payment_cost,my_point, my_point_btn,use_my_point,order_point,payment_my_point,payment_my_point_final;
    //9개
    LinearLayout payment_btn;
    private static int point,final_point,user_point;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_buy);
        payment_img = findViewById(R.id.title_img); //상품 이미지
        payment_store = findViewById(R.id.payment_store);//스토어
        payment_title = findViewById(R.id.payment_title);//상품 이름
        payment_cost = findViewById(R.id.payment_cost);// 상품 결제 금액
        my_point = findViewById(R.id.payment_point);//내 포인트
        my_point_btn = findViewById(R.id.point_btn);//클릭시 내 포인트 전액 가져오기
        use_my_point = findViewById(R.id.payment_my_point);
        order_point = findViewById(R.id.order_price);
        payment_my_point = findViewById(R.id.my_point);
        payment_my_point_final = findViewById(R.id.payment_final_cost);
        payment_btn = findViewById(R.id.payment);

        Intent intent = getIntent();
        String key = intent.getStringExtra("key");
        Log.d("barcode_key : ", key);
        String category = intent.getStringExtra("category");
        Log.d("barcode_category : ", category);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String userIdToken = firebaseUser.getUid();
        Log.d("userIdToken : ", userIdToken);

        DatabaseReference store = FirebaseDatabase.getInstance().getReference("point").child("category").child(category).child(key);
        DatabaseReference userAccount = FirebaseDatabase.getInstance().getReference("UserAccount").child(userIdToken);

        store.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String title = snapshot.child("title").getValue(String.class);
                String imgUrl = snapshot.child("imgUrl").getValue(String.class);
                point = snapshot.child("cost").getValue(Integer.class);
                String store = snapshot.child("store").getValue(String.class);

                payment_title.setText(title);
                order_point.setText(Integer.toString(point));
                payment_cost.setText(Integer.toString(point));
                payment_store.setText(store);
                payment_my_point_final.setText(Integer.toString(point));
                if (imgUrl != null && !imgUrl.isEmpty()) {
                    Glide.with(getApplicationContext())
                            .load(imgUrl)
                            .into(payment_img);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        userAccount.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user_point = snapshot.child("point").getValue(Integer.class);
                my_point.setText(Integer.toString(user_point));
                          }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        my_point_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                use_my_point.setText(Integer.toString(user_point));
                payment_my_point.setText(Integer.toString(user_point));
            }
        });

        payment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user_point>=point)
                {
                    final_point = user_point - point;
                    userAccount.child("point").setValue(final_point);
                    Intent intent1 = new Intent(paymentActivity.this, barcodeActivity.class);
                    intent1.putExtra("key", key);
                    intent1.putExtra("category",category);
                    finish();
                    startActivity(intent1);
                }
            }
        });
    }
}
