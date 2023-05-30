package com.example.solpl1.mypage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.solpl1.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class mypage_auth_activity extends AppCompatActivity {
    TextView auth_loc;
    TextView auth_date;
    Button upload_picture, auth_btn;
    static final int REQUEST_CODE = 2;
    ImageView imageView;    //갤러리에서 가져온 이미지를 보여줄 뷰
    Uri uri;                //갤러리에서 가져온 이미지에 대한 Uri
    Bitmap bitmap;          //갤러리에서 가져온 이미지를 담을 비트맵
    InputImage image;       //ML 모델이 인식할 인풋 이미지
    TextRecognizer recognizer; //텍스트 인식에 사용될 모델
    public String text;
    public static Context context_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_auth_page);
        super.onCreate(savedInstanceState);
        context_main = this;

        auth_loc = findViewById(R.id.my_page_auth_loc);
        auth_date = findViewById(R.id.my_page_auth_date);
        upload_picture = findViewById(R.id.my_page_auth_picture_upload_btn);
        auth_btn = findViewById(R.id.my_page_auth_btn);
        imageView=findViewById(R.id.imageView);
        recognizer = TextRecognition.getClient(new KoreanTextRecognizerOptions.Builder().build());

        // Intent에서 전달된 값들을 가져옵니다.
        String date = getIntent().getStringExtra("date");
        String loc = getIntent().getStringExtra("loc");

        // 가져온 값들을 TextView에 설정합니다.
        auth_loc.setText(loc);
        auth_date.setText(date);
        upload_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        auth_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextRecognition(recognizer);
            }
        });
    }

    private void TextRecognition(TextRecognizer recognizer){
        Task<Text> result = recognizer.process(image)
                // 이미지 인식에 성공하면 실행되는 리스너
                .addOnSuccessListener(new OnSuccessListener<Text>() {
                    @Override
                    public void onSuccess(Text visionText) {
                        Log.e("텍스트 인식", "성공");
                        // Task completed successfully
                        String resultText = visionText.getText();
                        text=resultText;  // 인식한 텍스트를 text에 저장
                    }
                })
                // 이미지 인식에 실패하면 실행되는 리스너
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("텍스트 인식", "실패: " + e.getMessage());
                            }
                        });
    }

    protected  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == REQUEST_CODE) {
            // 갤러리에서 선택한 사진에 대한 uri를 가져온다
            uri = data.getData();

            bitmap=resize(this,uri,500);
            imageView.setImageBitmap(bitmap);
            image = InputImage.fromBitmap(bitmap, 0);

        }
    }

    //이미지 사이즈 줄이는 메소드
    private Bitmap resize(Context context, Uri uri, int resize){
        Bitmap resizeBitmap=null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        try {
            BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, options);

            int width = options.outWidth;
            int height = options.outHeight;
            int samplesize = 1;

            while (true) {
                if (width / 2 < resize || height / 2 < resize)
                    break;
                width /= 2;
                height /= 2;
                samplesize *= 2;
            }

            options.inSampleSize = samplesize;
            Bitmap bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, options);
            resizeBitmap=bitmap;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return resizeBitmap;
    }

}
