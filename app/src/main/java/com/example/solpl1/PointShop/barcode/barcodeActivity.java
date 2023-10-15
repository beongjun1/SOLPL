package com.example.solpl1.PointShop.barcode;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.solpl1.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class barcodeActivity extends AppCompatActivity {

    private ImageView barcodeImage,giftImage;
    private TextView barcodeText,giftText,giftStore,changeStore,giftDate;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barcode);

        barcodeImage = findViewById(R.id.barcode_image);
        barcodeText = findViewById(R.id.barcode_text);
        giftImage = findViewById(R.id.barcode_gift_image);
        giftStore = findViewById(R.id.gift_store);
        giftText = findViewById(R.id.barcode_gift_text);
        changeStore = findViewById(R.id.barcode_gift_store);
        giftDate = findViewById(R.id.barcode_gift_date);


        Intent intent = getIntent();
        String key = intent.getStringExtra("key");
        Log.d("barcode_key : ", key);
        String category = intent.getStringExtra("category");
        Log.d("barcode_category : ", category);
        DatabaseReference store = FirebaseDatabase.getInstance().getReference("point").child("category").child(category).child(key);

        store.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String title = snapshot.child("title").getValue(String.class);
                String date = snapshot.child("date").getValue(String.class);
                String imgUrl = snapshot.child("imgUrl").getValue(String.class);
                String store = snapshot.child("store").getValue(String.class);

                giftStore.setText(store);
                giftText.setText(title);
                changeStore.setText(store);
                giftDate.setText(date);

                if (imgUrl != null && !imgUrl.isEmpty()) {
                    Glide.with(getApplicationContext())
                            .load(imgUrl)
                            .into(giftImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        String barcodeNumber = "1234 5789 123456";
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        try {
            Bitmap bitmap = barcodeEncoder.encodeBitmap(barcodeNumber, BarcodeFormat.CODE_128, 700, 200);
            barcodeImage.setImageBitmap(bitmap);
            barcodeText.setText(barcodeNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
