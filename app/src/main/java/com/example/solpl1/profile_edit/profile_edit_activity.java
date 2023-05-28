package com.example.solpl1.profile_edit;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.solpl1.R;
import com.example.solpl1.UserAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class profile_edit_activity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_GALLERY = 2;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    private Uri imageUri;
    EditText email_edit_text;
    EditText name_edit_text;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        TextView edit_cancel = findViewById(R.id.edit_cancel);
        TextView edit_success = findViewById(R.id.edit_success);

        setUserDataToEditText();

        edit_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        edit_success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeDatabaseSuccess();
            }
        });

        Button changePhotoButton = findViewById(R.id.change_photo_button);
        changePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 다이얼로그 생성(사진 찍기, 갤러리에서 사진 선택)
                AlertDialog.Builder builder = new AlertDialog.Builder(profile_edit_activity.this);
                builder.setTitle("사진 변경");
                builder.setItems(new CharSequence[]{"갤러리", "카메라","취소"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                // 갤러리 선택 시 처리할 코드
                                openGallery();
                                break;
                            case 1:
                                // 카메라 선택 시 처리할 코드
                                openCamera();
                                break;
                            case 2:
                                break;
                        }
                    }
                });
                builder.show();
            }
        });

    }

    private void ChangeDatabaseSuccess() {
        FirebaseUser user_account = FirebaseAuth.getInstance().getCurrentUser();
        user_account.getIdToken(true).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
            @Override
            public void onSuccess(GetTokenResult getTokenResult) {
                String idToken = getTokenResult.getToken();
                DatabaseReference userRef = database.getReference("UserAccount");
                // 이후의 코드는 동일하게 유지
                String name = name_edit_text.getText().toString();
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // 데이터 변경이 완료된 후 호출되는 콜백 메서드
                        // 변경된 데이터에 접근하여 필요한 작업을 수행할 수 있음
                        // 데이터가 존재하는 경우
                        if(snapshot.exists()){
                            UserAccount user =snapshot.getValue(UserAccount.class);

                            // 가져온 데이터를 수정하거나 작업을 수행합니다.
                            user.setName(name);

                            // 변경된 데이터를 다시 Firebase에 저장할 수 있습니다.
                            snapshot.getRef().setValue(user);

                            // 수정된 데이터를 다시 데이터베이스에 저장합니다.
                            userRef.setValue(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // 데이터 저장이 성공한 경우 호출되는 콜백 메서드
                                            // 작업 완료 후 수행할 작업을 추가합니다.
                                            Toast.makeText(profile_edit_activity.this, "데이터가 성공적으로 변경되었습니다.", Toast.LENGTH_SHORT).show();

                                            // 이전 화면으로 돌아가는 코드를 추가할 수 있습니다.
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // 데이터 저장이 실패한 경우 호출되는 콜백 메서드
                                            // 에러 처리 등을 수행합니다.
                                            Toast.makeText(profile_edit_activity.this, "데이터 변경에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            // 데이터가 존재하지 않는 경우
                            Toast.makeText(profile_edit_activity.this, "데이터가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


    }

    private void setUserDataToEditText() {
        // 현재 로그인한 사용자의 이메일을 가져옴
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail = user.getEmail();

        // 데이터베이스에서 UserAccount의 정보를 가져오는 코드
        FirebaseDatabase.getInstance().getReference().child("UserAccount")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                String email = childSnapshot.child("emailId").getValue(String.class);
                                if (email.equals(userEmail)) {
                                    String name = childSnapshot.child("name").getValue(String.class);
//                                    String phone = childSnapshot.child("phone").getValue(String.class);
//                                    String location = childSnapshot.child("location").getValue(String.class);

                                    email_edit_text = findViewById(R.id.profile_email);
                                    email_edit_text.setText(email);
                                    // EditText를 찾아서 가져온 정보로 설정
                                    name_edit_text = findViewById(R.id.profile_name);
                                    name_edit_text.setText(name);

//                                    EditText phoneEditText = findViewById(R.id.profile_phone);
//                                    phoneEditText.setText(phone);
//
//                                    EditText locationEditText = findViewById(R.id.profile_loc);
//                                    locationEditText.setText(location);
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

    // 갤러리 열기
    private void openGallery() {
        // 갤러리에서 사진 선택하는 코드 작성
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    // 카메라 열기
    private void openCamera() {
        // 카메라 앱 호출 인텐트 생성
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 인텐트를 처리할 액티비티가 있는지 확인
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // 이미지 파일을 저장할 임시 파일 생성
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            // 임시 파일이 성공적으로 생성되었을 경우에만 계속 진행
            if (photoFile != null) {
                // 임시 파일의 URI 가져오기
                imageUri = FileProvider.getUriForFile(this, "com.example.mypage.fileprovider", photoFile);

                // 인텐트에 사진 저장 위치 설정
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

                // 카메라 앱 호출
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            } else {
                // 예외 처리: 이미지 파일 생성에 실패한 경우
                Toast.makeText(this, "이미지 파일을 생성할 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        } else {
            // 예외 처리: 카메라 앱이 설치되어 있지 않은 경우
            Toast.makeText(this, "카메라 앱을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    // 이미지 파일을 저장할 임시 파일 생성
    private File createImageFile() throws IOException {
        // 이미지 파일 이름 생성
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        // 이미지 파일이 저장될 디렉토리 생성
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        // 이미지 파일 생성
        File imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);

        return imageFile;
    }

    // 카메라 앱으로부터 사진을 선택한 결과 처리
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView profile_image_view = findViewById(R.id.profile_img);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // 이미지 파일을 가져와서 CircleImageView에 설정
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                bitmap = rotateImage(bitmap);

                profile_image_view.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_GALLERY && resultCode == RESULT_OK) {
            if (data != null) {
                try {
                    Uri uri = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    profile_image_view.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // 예외 처리: 선택한 이미지의 데이터가 null인 경우
                Toast.makeText(this, "이미지를 선택할 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

        // 이미지를 왼쪽으로 90도 회전하는 메서드
        private Bitmap rotateImage (Bitmap sourceBitmap){
            Matrix matrix = new Matrix();
            matrix.postRotate(+90); // 왼쪽으로 90도 회전

            return Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight(), matrix, true);
        }

}
