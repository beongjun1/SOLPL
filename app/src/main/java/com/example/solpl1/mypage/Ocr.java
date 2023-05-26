package com.example.solpl1.mypage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class Ocr extends AppCompatActivity {
    static final int REQUEST_CODE = 2;
    Uri uri;                //갤러리에서 가져온 이미지에 대한 Uri
    Bitmap bitmap;          //갤러리에서 가져온 이미지를 담을 비트맵
    InputImage image;       //ML 모델이 인식할 인풋 이미지
    TextRecognizer recognizer; //텍스트 인식에 사용될 모델
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recognizer = TextRecognition.getClient(new KoreanTextRecognizerOptions.Builder().build());

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, REQUEST_CODE);
        //TextRecognition(recognizer);
    }
//    @Override
//    protected  void onActivityResult(int requestCode, int resultCode, Intent data){
//        super.onActivityResult(requestCode,resultCode,data);
//        if (requestCode == REQUEST_CODE) {
//            // 갤러리에서 선택한 사진에 대한 uri를 가져온다
//            uri = data.getData();
//
//            setImage(uri);
//        }
//    }
//
//    // uri를 비트맵으로 변환시킨후 이미지뷰에 띄워주고 InputImage를 생성하는 메서드
//    private void setImage(Uri uri) {
//        try{
//            InputStream in = getContentResolver().openInputStream(uri);
//            bitmap = BitmapFactory.decodeStream(in);
//            imageView.setImageBitmap(bitmap);
//
//            image = InputImage.fromBitmap(bitmap, 0);
//            Log.e("setImage", "이미지 to 비트맵");
//        } catch (FileNotFoundException e){
//            e.printStackTrace();
//        }
//    }
//    private void TextRecognition(TextRecognizer recognizer){
//        Task<Text> result = recognizer.process(image)
//                // 이미지 인식에 성공하면 실행되는 리스너
//                .addOnSuccessListener(new OnSuccessListener<Text>() {
//                    @Override
//                    public void onSuccess(Text visionText) {
//                        Log.e("텍스트 인식", "성공");
//                        // Task completed successfully
//                        String resultText = visionText.getText();
//                        text_info.setText(resultText);  // 인식한 텍스트를 TextView에 세팅
//                    }
//                })
//                // 이미지 인식에 실패하면 실행되는 리스너
//                .addOnFailureListener(
//                        new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.e("텍스트 인식", "실패: " + e.getMessage());
//                            }
//                        });
//    }

}
