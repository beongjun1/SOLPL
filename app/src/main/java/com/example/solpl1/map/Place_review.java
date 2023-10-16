package com.example.solpl1.map;

import static android.content.ContentValues.TAG;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.solpl1.R;
import com.example.solpl1.mypage.my_page_post_image_adpater;
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

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Place_review extends AppCompatActivity {
    RatingBar ratingBar;
    Button btn_review_photo, upload_review;
    EditText review_content;
    private RecyclerView recyclerView;
    private my_page_post_image_adpater recycler_view_adapter;
    ArrayList<Uri> uriList = new ArrayList<>();
    private DatabaseReference user_account_database;
    ArrayList<String> urlList = new ArrayList<>();
    String title;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_review);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();

        ratingBar=findViewById(R.id.rating_review);
        btn_review_photo=findViewById(R.id.btn_review_photo);
        upload_review=findViewById(R.id.upload_review);
        review_content=findViewById(R.id.review_content);
        recyclerView=findViewById(R.id.review_image_recycler_view);
        Intent intent = getIntent();
        title=intent.getStringExtra("title");

        btn_review_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2222);

            }
        });

        upload_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uriList.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "이미지를 선택해주세요.", Toast.LENGTH_LONG).show();
                }
                else {
                        storage_upload();
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {   // 어떤 이미지도 선택하지 않은 경우
            Toast.makeText(getApplicationContext(), "이미지를 선택하지 않았습니다.", Toast.LENGTH_LONG).show();
        } else {   // 이미지를 하나라도 선택한 경우
            if (data.getClipData() == null) {     // 이미지를 하나만 선택한 경우
                Log.e("single choice: ", String.valueOf(data.getData()));
                Uri imageUri = data.getData();
                uriList.add(imageUri);

                recycler_view_adapter = new my_page_post_image_adpater(uriList, getApplicationContext());
                recyclerView.setAdapter(recycler_view_adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));

                // Add this line to add the image URI to uriList
                uriList.add(imageUri);
            } else {      // 이미지를 여러장 선택한 경우
                ClipData clipData = data.getClipData();
                Log.e("clipData", String.valueOf(clipData.getItemCount()));

                if (clipData.getItemCount() > 10) {   // 선택한 이미지가 11장 이상인 경우
                    Toast.makeText(getApplicationContext(), "사진은 10장까지 선택 가능합니다.", Toast.LENGTH_LONG).show();
                } else {   // 선택한 이미지가 1장 이상 10장 이하인 경우
                    Log.e(TAG, "multiple choice");

                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        Uri imageUri = clipData.getItemAt(i).getUri();  // 선택한 이미지들의 uri를 가져온다.
                        try {
                            uriList.add(imageUri);  // uri를 list에 담는다.

                        } catch (Exception e) {
                            Log.e(TAG, "File select error", e);
                        }
                    }

                    recycler_view_adapter = new my_page_post_image_adpater(uriList, getApplicationContext());
                    recyclerView.setAdapter(recycler_view_adapter);   // 리사이클러뷰에 어댑터 설정
                    recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));     // 리사이클러뷰 수평 스크롤 적용
                }
            }
        }
    }
    private boolean isEditTextEmpty(EditText editText) {
        return editText.getText().toString().trim().length() == 0;
    }

    private void storage_upload() {
        InputStream stream = null;


        DatabaseReference mDatabaseRef; //실시간 데이터베이스
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("places").child(title).child("reviews");
        String key = mDatabaseRef.child("writing_items").push().getKey(); // 새로운 레코드를 추가할 위치에 대한 key 생성

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String userEmail = user.getEmail();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String filePath = userEmail + "/review/" +key + "/";

        for (int i = 0; i < uriList.size(); i++) {
            Uri imageUri = uriList.get(i);
            if (imageUri == null) {
                continue; // 이미지 URI가 null인 경우 건너뜁니다.
            }
            String filename = "image_" + System.currentTimeMillis() + ".jpg";
            StorageReference imageRef = storageRef.child(filePath + filename);

            try {
                stream = getContentResolver().openInputStream(imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            UploadTask uploadTask = imageRef.putStream(stream);

            int finalI = i; // 람다식에서 사용하기 위해 변수의 final 복사본을 생성합니다.
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    Log.d(TAG, "Image uploaded to Firebase Storage. URL: " + imageUrl);
                    urlList.add(imageUrl);

                    if (finalI == uriList.size() - 1) {
                        // 마지막 이미지의 URL을 가져왔을 때 데이터베이스에 저장
                        database_save(key);
                    }
                });
            });
        }
    }

    private void database_save(String key) {

        DatabaseReference mDatabaseRef; //실시간 데이터베이스
        FirebaseAuth mFirebaseAuth; // 파이어베이스 인증

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("places").child(title).child("reviews");

        String currentTime = DateFormat.getDateTimeInstance().format(new Date());
        user_account_database = FirebaseDatabase.getInstance().getReference("UserAccount");

        //텍스트 들을 String으로 변환후 저장
        String reviewcontent = review_content.getText().toString();
        String rating = String.valueOf(ratingBar.getRating());

        Map<String, Object> reviewValues = new HashMap<>();
        //firebase 데이터베이스에 저장
        reviewValues.put("rating", rating);
        reviewValues.put("content", reviewcontent);
        reviewValues.put("review_date", currentTime);
        reviewValues.put("title", title);
        reviewValues.put("review_id", key);
        reviewValues.put("review_image", urlList.get(0));

        Map<String, Object> imageValues = new HashMap<>();
        for (String imageUrl : urlList) {
            String imageKey = mDatabaseRef.child(key).child("images").push().getKey();
            imageValues.put(imageKey, imageUrl);
        }

            user_account_database.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
                    if (currentUser != null) {
                        String currentUserId = currentUser.getUid();
                        String idToken = snapshot.child(currentUserId).child("idToken").getValue(String.class);
                        String name = snapshot.child(currentUserId).child("name").getValue(String.class);

                        if (idToken != null) {
                            reviewValues.put("id_token", idToken);
                            reviewValues.put("name",name);
                            mDatabaseRef.child(key).setValue(reviewValues); // post 노드에 레코드 추가
                            mDatabaseRef.child(key).child("images").updateChildren(imageValues);
                            Toast.makeText(getApplicationContext(), "업로드 되었습니다.", Toast.LENGTH_LONG).show();
                            FirebaseDatabase.getInstance().getReference("UserAccount").child(idToken).child("reviews").child(key).setValue(reviewValues);
                            FirebaseDatabase.getInstance().getReference("UserAccount").child(idToken).child("reviews").child(key).child("images").updateChildren(imageValues);
                            finish();

                        } else {
                            // 로그인한 사용자의 ID 토큰이 데이터베이스에 없음
                        }
                    } else {
                        // 현재 로그인된 사용자가 없음
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // onCancelled 처리
                }
            });
        }

}
