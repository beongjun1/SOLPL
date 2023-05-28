package com.example.solpl1.mypage;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.solpl1.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class mypage_auth_activity extends AppCompatActivity {
    TextView auth_loc;
    TextView auth_date;
    Button upload_picture;
    Button camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_auth_page);
        super.onCreate(savedInstanceState);

        auth_loc = findViewById(R.id.my_page_auth_loc);
        auth_date = findViewById(R.id.my_page_auth_date);
        camera = findViewById(R.id.my_page_auth_camera_btn);
        upload_picture = findViewById(R.id.my_page_auth_picture_upload_btn);

        // Intent에서 전달된 값들을 가져옵니다.
        String date = getIntent().getStringExtra("date");
        String loc = getIntent().getStringExtra("loc");

        // 가져온 값들을 TextView에 설정합니다.
        auth_loc.setText(loc);
        auth_date.setText(date);
        upload_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 카메라 앱 실행을 위한 Intent 생성
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // 사진이 저장될 임시 파일 생성
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(mypage_auth_activity.this,
                                "com.example.mypage.fileprovider",
                                photoFile);
                        // 생성된 임시 파일의 경로를 Intent에 설정
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, 3333);
                    }
                }
            }
        });
    }



    // 임시 파일 생성을 위한 메서드
    private File createImageFile() throws IOException {
        // 이미지 파일 이름 생성
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        // 이미지가 저장될 디렉토리 생성
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // 생성된 파일의 경로 반환
        return imageFile;
    }

}
