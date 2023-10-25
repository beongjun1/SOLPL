package com.example.solpl1.PointShop.payment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import com.example.solpl1.mypage.mypage_main_activity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class paymentActivity extends AppCompatActivity {
    ImageView payment_img;
    TextView payment_store, payment_title, payment_cost, my_point, my_point_btn, use_my_point, order_point, payment_my_point, payment_my_point_final;
    //9개
    LinearLayout payment_btn;
    private static int point, final_point, user_point;
    private String title, store, email;

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

        DatabaseReference storeShop = FirebaseDatabase.getInstance().getReference("point").child("category").child(category).child(key);
        DatabaseReference userAccount = FirebaseDatabase.getInstance().getReference("UserAccount").child(userIdToken);

        storeShop.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                title = snapshot.child("title").getValue(String.class);
                String imgUrl = snapshot.child("imgUrl").getValue(String.class);
                point = snapshot.child("cost").getValue(Integer.class);
                store = snapshot.child("store").getValue(String.class);

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
                email = snapshot.child("emailId").getValue(String.class);
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
                if (user_point >= point) {
                    String productKey = storeShop.push().getKey(); // 자동으로 생성된 고유한 키
                    String orderNumber = UUID.randomUUID().toString(); // 주문번호
                    String barcodeData = "ProductID: " + productKey + ", OrderNumber: " + orderNumber +
                            ", email: " + email + ", point: " + point;

                    int barcodeWidth = 500; // 바코드의 폭
                    int barcodeHeight = 500; // 바코드의 높이
                    Bitmap barcodeBitmap = BarcodeGenerator.generateBarcode(barcodeData, barcodeWidth, barcodeHeight);


                    final_point = user_point - point;
                    userAccount.child("point").setValue(final_point);
                    userAccount.child("pointShop").child("productKey").child(productKey).child("productKey").setValue(productKey);




                    // Firebase Storage에 바코드 이미지 업로드
                    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                    StorageReference barcodeRef = storageRef.child(email+"/pointShop/"+productKey+".png");


                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    barcodeBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] data = baos.toByteArray();

                    UploadTask uploadTask = barcodeRef.putBytes(data);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // 바코드 이미지 업로드 성공 시의 로직
                            String barcodeImageUrl = taskSnapshot.getUploadSessionUri().toString();

                            // 여기에서 바코드 이미지 URL과 함께 데이터베이스에 구매 정보 저장 로직을 추가할 수 있습니다.
                            // 예를 들어, "barcodeImageUrl"을 데이터베이스에 저장하고 필요한 정보를 함께 저장할 수 있습니다.
                            userAccount.child("pointShop").child("productKey").child(productKey).child("barcodeImageUrl").setValue(barcodeImageUrl);

                            // 바코드 이미지 URL과 관련된 로직 추가
                        }
                    });


                    Intent intent1 = new Intent(paymentActivity.this, mypage_main_activity.class);
                    intent1.putExtra("key", key);
                    intent1.putExtra("category", category);
                    finish();
                    startActivity(intent1);
                }
            }
        });
    }

    public class BarcodeGenerator {
        public static Bitmap generateBarcode(String data, int width, int height) {
            BitMatrix bitMatrix;
            try {
                Writer writer = new QRCodeWriter();
                bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, width, height);
            } catch (WriterException e) {
                e.printStackTrace();
                return null;
            }

            int bitmapWidth = bitMatrix.getWidth();
            int bitmapHeight = bitMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.RGB_565);

            for (int x = 0; x < bitmapWidth; x++) {
                for (int y = 0; y < bitmapHeight; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            return bitmap;
        }
    }
}
