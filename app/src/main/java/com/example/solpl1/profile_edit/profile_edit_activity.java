package com.example.solpl1.profile_edit;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.example.solpl1.R;
import com.example.solpl1.UserAccount;
import com.example.solpl1.mypage.mypage_main_activity;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class profile_edit_activity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_GALLERY = 2;
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    private Uri imageUri;
    EditText email_edit_text;
    EditText name_edit_text;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();

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
                deleteAllImages();
                updateUserProfile();

                finish();
                // 이미지 업데이트 액티비티를 호출하여 결과를 받아 처리합니다.
            }

        });

        Button changePhotoButton = findViewById(R.id.change_photo_button);
        changePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 다이얼로그 생성(사진 찍기, 갤러리에서 사진 선택)
                AlertDialog.Builder builder = new AlertDialog.Builder(profile_edit_activity.this);
                builder.setTitle("사진 변경");
                builder.setItems(new CharSequence[]{"갤러리", "카메라", "취소"}, new DialogInterface.OnClickListener() {
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
    private void updateUserProfile() {
        String newName = name_edit_text.getText().toString().trim();

        // 이미지 업로드 및 URL 업데이트를 위한 코드 추가
        if (imageUri != null) {
            uploadImageAndUpdateProfile(newName);
        } else {
            updateProfileWithoutImage(newName);
        }
        Intent resultIntent = new Intent();
        resultIntent.putExtra("imageUri", imageUri.toString());
        setResult(RESULT_OK, resultIntent);
    }

    //이미지가 선택되지 않은 경우 프로필을 이미지 없이 업데이트하는 updateProfileWithoutImage()
    private void updateProfileWithoutImage(String newName) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("UserAccount").child(user.getUid());
        userRef.child("name").setValue(newName)

                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // 프로필 정보 업데이트 완료 메시지 표시
                        Toast.makeText(profile_edit_activity.this, "프로필 정보가 업데이트되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // 프로필 정보 업데이트 실패 메시지 표시
                        Toast.makeText(profile_edit_activity.this, "프로필 정보를 업데이트하지 못했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //이미지를 업로드하고 프로필을 업데이트하는 uploadImageAndUpdateProfile()
    private void uploadImageAndUpdateProfile(String newName) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String userEmail = user.getEmail();
        // Firebase Storage에 이미지 업로드
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String filePath = userEmail + "/profile_images/";
        StorageReference imageRef = storageRef.child(filePath+"/profile_images/");
        imageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // 이미지 업로드 성공 후 URL 가져오기
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri imageUrl) {
                                // URL을 사용하여 UserAccount의 이미지 URL 업데이트
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("UserAccount").child(user.getUid());
                                userRef.child("imageUrl").setValue(imageUrl.toString())
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // 프로필 정보 업데이트 완료 메시지 표시
                                                Toast.makeText(profile_edit_activity.this, "프로필 정보가 업데이트되었습니다.", Toast.LENGTH_SHORT).show();
                                                // 기존 사진 삭제
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // 프로필 정보 업데이트 실패 메시지 표시
                                                Toast.makeText(profile_edit_activity.this, "프로필 정보를 업데이트하지 못했습니다.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // 이미지 업로드 실패 메시지 표시
                        Toast.makeText(profile_edit_activity.this, "이미지 업로드에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
// 이미지 변경시 이전 사진 삭제 deleteOtherImages()
    private void deleteAllImages() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String userEmail = user.getEmail();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String filePath = userEmail + "/profile_images/";
        StorageReference imageRef = storageRef.child(filePath+"/profile_images/");

        imageRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference imageRef : listResult.getItems()) {
                    imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // 이미지 삭제 성공 메시지 표시
                            Log.d("ProfileEditActivity", "이미지가 삭제되었습니다.");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // 이미지 삭제 실패 메시지 표시
                            Log.d("ProfileEditActivity", "이미지가 삭제되지 않았습니다.");
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // 이미지 목록 가져오기 실패 메시지 표시
                Log.d("ProfileEditActivity", "이미지 목록을 가져오지 못했습니다.");
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
                                if (email != null && email.equals(userEmail)) {
                                    String name = childSnapshot.child("name").getValue(String.class);
//                                    String phone = childSnapshot.child("phone").getValue(String.class);
//                                    String location = childSnapshot.child("location").getValue(String.class);

                                    email_edit_text = findViewById(R.id.profile_email);
                                    email_edit_text.setText(email);
                                    // EditText를 찾아서 가져온 정보로 설정
                                    name_edit_text = findViewById(R.id.profile_name);
                                    name_edit_text.setText(name);

                                    String ImageURl = childSnapshot.child("imageUrl").getValue(String.class);
                                    CircleImageView Profile_img = findViewById(R.id.profile_edit_img);
                                    if (ImageURl != null && !ImageURl.isEmpty()) {
                                        Glide.with(getApplicationContext())
                                                .load(ImageURl)
                                                .into(Profile_img);
                                    }

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

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // 권한이 부여되지 않은 경우 권한 요청
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
            return;
        }
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
                imageUri = FileProvider.getUriForFile(this, "com.example.solpl1.profile_edit.fileprovider", photoFile);

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
    @NonNull
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
        ImageView profile_image_view = findViewById(R.id.profile_edit_img);

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
                    imageUri = uri; // imageUri 업데이트
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

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            // 권한이 부여되었는지 확인
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한이 부여된 경우 카메라 앱 호출
                openCamera();
            } else {
                // 권한이 거부된 경우 처리
                Toast.makeText(this, "카메라 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 이미지를 왼쪽으로 90도 회전하는 메서드
    private Bitmap rotateImage(Bitmap sourceBitmap) {
        Matrix matrix = new Matrix();
        matrix.postRotate(+90); // 왼쪽으로 90도 회전

        return Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight(), matrix, true);
    }

}
