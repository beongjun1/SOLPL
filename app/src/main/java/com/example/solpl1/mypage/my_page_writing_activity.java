package com.example.solpl1.mypage;

import static android.content.ContentValues.TAG;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.solpl1.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
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

import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class my_page_writing_activity extends AppCompatActivity {

//    private boolean isKeyboardShowing = false;
    private RecyclerView image_recycler_view;
    private my_page_post_image_adpater recycler_view_adapter;

    TextView post_date;
    Button writing_btn_attach_photo;
    ArrayList<Uri> uriList = new ArrayList<>();     // 이미지의 uri를 담을 ArrayList 객체
    private DatabaseReference user_account_database; //실시간 데이터베이스
    DecimalFormat myFormatter = new DecimalFormat("###,###"); //비용을 처리할 때 쉼표를 표시하기 위한 객체
    String result1 = "";
    TextInputEditText writing_editTextPrice;
    EditText post_title;
    EditText post_content;
    EditText post_hashtag;
    ArrayList<String> urlList = new ArrayList<>(); // urlList 객체 초기화

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();
        post_date = findViewById(R.id.post_travel_date_btn);
        writing_btn_attach_photo = findViewById(R.id.writing_btn_attach_photo);

        image_recycler_view = findViewById(R.id.post_image_recycler_view);

        //비용
        writing_editTextPrice = findViewById(R.id.writing_edit_text_price);
        writing_editTextPrice.addTextChangedListener(trip_price);

        //editText에 대한 것들
        post_title = findViewById(R.id.post_feed_title);
        post_content = findViewById(R.id.post_feed_content);
        post_hashtag = findViewById(R.id.post_feed_hashtag);

        setUserNameFromDatabase(); // 데이터베이스에서 userName가져오기


        //이전 화면에서 데이터를 선택하는 화면
        post_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(my_page_writing_activity.this, my_page_writing_date_select_activity.class);
                startActivityForResult(intent, 1111);
            }
        });


        //앨범으로 이동하는 버튼
        writing_btn_attach_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2222);

            }
        });

        Button uploadButton = findViewById(R.id.upload_post);
        // 업로드 버튼 클릭시 이미지와 텍스트를 데이터베이스에 저장, storage에 저장
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uriList.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "이미지를 선택해주세요.", Toast.LENGTH_LONG).show();
                }
                else {
                    if (post_date.getText().toString().equals("날짜 선택")){
                        Toast.makeText(my_page_writing_activity.this, "날짜를 선택해주세요", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if (isEditTextEmpty(post_title) || isEditTextEmpty(writing_editTextPrice) || isEditTextEmpty(post_content)) {
                            Toast.makeText(my_page_writing_activity.this, "해시태그를 제외한 입력필드를 모두 입력해주세요", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            storage_upload();
                        }

                    }

                }
            }

        });

    }
    private boolean isEditTextEmpty(EditText editText) {
        return editText.getText().toString().trim().length() == 0;
    }
    // 앨범에서 액티비티로 돌아온 후 실행되는 메서드
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1111 && resultCode == RESULT_OK) {
            String selectedDate = data.getStringExtra("selectedDate");
            post_date.setText(selectedDate);
        }

        if (data == null) {   // 어떤 이미지도 선택하지 않은 경우
            Toast.makeText(getApplicationContext(), "이미지를 선택하지 않았습니다.", Toast.LENGTH_LONG).show();
        } else {   // 이미지를 하나라도 선택한 경우
            if (data.getClipData() == null) {     // 이미지를 하나만 선택한 경우
                Log.e("single choice: ", String.valueOf(data.getData()));
                Uri imageUri = data.getData();
                uriList.add(imageUri);

                recycler_view_adapter = new my_page_post_image_adpater(uriList, getApplicationContext());
                image_recycler_view.setAdapter(recycler_view_adapter);
                image_recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));

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
                    image_recycler_view.setAdapter(recycler_view_adapter);   // 리사이클러뷰에 어댑터 설정
                    image_recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));     // 리사이클러뷰 수평 스크롤 적용
                }
            }
        }
    }

    // 비용을 입력했을시 ,가 자동으로 추가되는 코드
    TextWatcher trip_price = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(!TextUtils.isEmpty(s.toString()) && !s.toString().equals(result1)){
                result1 = myFormatter.format(Double.parseDouble(s.toString().replaceAll(",","")));
                writing_editTextPrice.setText(result1);
                writing_editTextPrice.setSelection(result1.length());
            }
        }



        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    //storage에 사진 저장
    private void storage_upload() {
        InputStream stream = null;


            DatabaseReference mDatabaseRef; //실시간 데이터베이스
            mDatabaseRef = FirebaseDatabase.getInstance().getReference("post_database");
            String key = mDatabaseRef.child("writing_items").push().getKey(); // 새로운 레코드를 추가할 위치에 대한 key 생성

            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser user = firebaseAuth.getCurrentUser();
            String userEmail = user.getEmail();
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            String filePath = userEmail + "/post/" +key + "/";

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

    //데이터베이스에 객체들을 저장
    private void database_save(String key) {

        DatabaseReference mDatabaseRef; //실시간 데이터베이스
        FirebaseAuth mFirebaseAuth; // 파이어베이스 인증

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("post_database");

        String currentTime = DateFormat.getDateTimeInstance().format(new Date());
        user_account_database = FirebaseDatabase.getInstance().getReference("UserAccount");

        //텍스트 들을 String으로 변환후 저장
        String trip_cost = writing_editTextPrice.getText().toString();
        String trip_date = post_date.getText().toString();
        String trip_title = post_title.getText().toString();
        String trip_content = post_content.getText().toString();
        String post_hashtag_text = post_hashtag.getText().toString().trim();


        // 추출된 해시태그를 리스트로 저장
        List<String> trip_hashtags = extractHashtags(post_hashtag.getText().toString()); // 해시태그 추출

        Map<String, Object> postValues = new HashMap<>();
        //firebase 데이터베이스에 저장
        postValues.put("cost", trip_cost);
        postValues.put("trip_date", trip_date);
        postValues.put("title", trip_title);
        postValues.put("content", trip_content);
        postValues.put("hashtags", trip_hashtags);
        postValues.put("post_date", currentTime);
        postValues.put("post_id", key);

        Map<String, Object> imageValues = new HashMap<>();
        for (String imageUrl : urlList) {
            String imageKey = mDatabaseRef.child(key).child("images").push().getKey();
            imageValues.put(imageKey, imageUrl);
        }


        if (post_hashtag_text != null && post_hashtag_text.contains("#") || post_hashtag_text.isEmpty()) {
            user_account_database.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
                    if (currentUser != null) {
                        String currentUserId = currentUser.getUid();
                        String idToken = snapshot.child(currentUserId).child("idToken").getValue(String.class);

                        if (idToken != null) {
                            postValues.put("id_token", idToken);
                            mDatabaseRef.child(key).setValue(postValues); // post 노드에 레코드 추가
                            mDatabaseRef.child(key).child("images").updateChildren(imageValues);
                            Toast.makeText(getApplicationContext(), "업로드 되었습니다.", Toast.LENGTH_LONG).show();
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
        else {
            Toast.makeText(this, "#을 사용하여 입력하세요", Toast.LENGTH_SHORT).show();
        }
    }



    //해시태그 #으로 된 패턴을 기준으로 List에 저장하는 코드
    private List<String> extractHashtags(String text) {
        List<String> hashtags = new ArrayList<>();
        Pattern pattern = Pattern.compile("#\\w+");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            hashtags.add(matcher.group().substring(1));
        }

        return hashtags;
    }
    private void setUserNameFromDatabase() {
        // 현재 로그인한 사용자의 이메일을 가져오는 코드 (Firebase Authentication을 사용한다고 가정)
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail = currentUser.getEmail();

        // 데이터베이스에서 사용자의 이메일을 기준으로 이름을 가져오는 쿼리 수행
        FirebaseDatabase.getInstance().getReference().child("UserAccount")
                .orderByChild("emailId").equalTo(userEmail)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                String userName = childSnapshot.child("name").getValue(String.class);
                                String ImageUrl = childSnapshot.child("imageUrl").getValue(String.class);
                                if (userName != null && !userName.isEmpty()) {
                                    TextView userNameTextView = findViewById(R.id.writing_name);
                                    userNameTextView.setText(userName);
                                }
                                CircleImageView profile_img = findViewById(R.id.post_profile_img);
                                if (ImageUrl != null && !ImageUrl.isEmpty()) {
                                    Glide.with(getApplicationContext())
                                            .load(ImageUrl)
                                            .into(profile_img);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("DatabaseError", "Error: " + databaseError.getMessage());
                    }
                });
    }



}