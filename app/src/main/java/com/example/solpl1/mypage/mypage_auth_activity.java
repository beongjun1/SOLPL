package com.example.solpl1.mypage;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
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
import androidx.exifinterface.media.ExifInterface;

import com.example.solpl1.R;
import com.example.solpl1.UserAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public static Context context_main;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserAccount").child(firebaseAuth.getUid());
    int flag;
    String[] resionArr={"용인","고양","오산","시흥","순천","제주","진주","수원","화성"};
    RECO_RESION reco_resion = new RECO_RESION();
    Date pictureDate,cameraDate,startDate,endDate;
    SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");

    public static final String REGEXP_DATE_TYPE1 = "20[\\d]{2}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])";
    public static final String REGEXP_DATE_TYPE2 = "20[\\d]{2}]\\/(0[1-9]|1[012])\\/(0[1-9]|[12][0-9]|3[01])";
    public static final String REGEXP_DATE_TYPE3 = "20[\\d]{2}\\.(0[1-9]|1[012])\\.(0[1-9]|[12][0-9]|3[01])";

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

        String startDay=date.substring(0,4)+date.substring(5,7)+date.substring(8,10);
        String endDay=date.substring(13,17)+date.substring(18,20)+date.substring(21);
        int endDay2=Integer.parseInt(endDay);
        endDay2+=10000;
        String endAuth=String.valueOf(endDay2);
        Date endAuthDate;
        try {
            startDate = dtFormat.parse(startDay);
            endDate = dtFormat.parse(endDay);
            endAuthDate = dtFormat.parse(endAuth);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


        // 가져온 값들을 TextView에 설정합니다.
        auth_loc.setText(loc);
        auth_date.setText(date);
        upload_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mypage_auth_activity.this,"직접 촬영한 사진만 가능합니다.",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        auth_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    int result=endAuthDate.compareTo(cameraDate);
                    if(result<0){
                        Toast.makeText(mypage_auth_activity.this,"영수증은 여행 후 1년 내에 촬영한 사진이여야 합니다.",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        TextRecognition(recognizer);
                    }
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
                        flag=0;
                        databaseReference.child("reco_text").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(flag==0) {
                                    String value = snapshot.getValue(String.class);
                                    if (value.contains(resultText)) {
                                        Toast.makeText(mypage_auth_activity.this, "이미 인증한 영수증입니다.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        String pictureDay="0";
                                        Pattern pattern = Pattern.compile(REGEXP_DATE_TYPE1);
                                        Matcher matcher=pattern.matcher(resultText);
                                        while (matcher.find())pictureDay=matcher.group().substring(0,4)+matcher.group().substring(5,7)+matcher.group().substring(8);
                                        if(pictureDay.equals("0")){
                                            pattern = Pattern.compile(REGEXP_DATE_TYPE2);
                                            matcher=pattern.matcher(resultText);
                                            while (matcher.find())pictureDay=matcher.group().substring(0,4)+matcher.group().substring(5,7)+matcher.group().substring(8);
                                            if(pictureDay.equals("0")){
                                                pattern = Pattern.compile(REGEXP_DATE_TYPE3);
                                                matcher=pattern.matcher(resultText);
                                                while (matcher.find())pictureDay=matcher.group().substring(0,4)+matcher.group().substring(5,7)+matcher.group().substring(8);
                                            }
                                        }
                                        if(pictureDay.equals(("0"))){
                                            Toast.makeText(mypage_auth_activity.this,"영수증의 날짜가 제대로 인식되도록 사진을 다시 올려주세요.",Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            flag = 1;
                                            try {
                                                pictureDate = dtFormat.parse(pictureDay);
                                            } catch (ParseException e) {
                                                throw new RuntimeException(e);
                                            }
                                            int result1= startDate.compareTo(pictureDate);
                                            int result2=endDate.compareTo(pictureDate);
                                            if(result1>0||result2<0){
                                                Toast.makeText(mypage_auth_activity.this,"여행기간 외에 이용한 영수증은 인증 불가능합니다.",Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                databaseReference.child("reco_text").setValue(value.concat(resultText));
                                                textExtraction(resultText);
                                                Toast.makeText(mypage_auth_activity.this, "인증 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
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
            String imagePath = getRealPathFromURI(uri); // path 경로
            ExifInterface exif = null;
            try {
                exif = new ExifInterface(imagePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String attrDate=exif.getAttribute(ExifInterface.TAG_DATETIME);
            String test=exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
            if(test==null){
                Toast.makeText(mypage_auth_activity.this,"촬영한 사진 외에는 인증할 수 없습니다.",Toast.LENGTH_SHORT).show();
            }
            else {
                String cameraDay=attrDate.substring(0,4)+attrDate.substring(5,7)+attrDate.substring(8,10);
                try {
                    cameraDate = dtFormat.parse(cameraDay);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                int exifDegree = exifOrientationToDegrees(exifOrientation);

                bitmap = BitmapFactory.decodeFile(imagePath);
                bitmap=resize(this,uri,500);
                imageView.setImageBitmap(rotate(bitmap, exifDegree));
                image = InputImage.fromBitmap(rotate(bitmap, exifDegree), 0);
            }
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

    private void textExtraction(String text){
        databaseReference.child("reco_resion").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RECO_RESION DB_RESION = snapshot.getValue(RECO_RESION.class);
                reco_resion.set고양(DB_RESION.get고양());
                reco_resion.set순천(DB_RESION.get순천());
                reco_resion.set용인(DB_RESION.get용인());
                reco_resion.set시흥(DB_RESION.get시흥());
                reco_resion.set진주(DB_RESION.get진주());
                reco_resion.set제주(DB_RESION.get제주());
                reco_resion.set화성(DB_RESION.get화성());
                reco_resion.set수원(DB_RESION.get수원());
                reco_resion.set오산(DB_RESION.get오산());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        for(int i=0;i<resionArr.length;i++){
            if(text.contains(resionArr[i])){
                switch (resionArr[i]) {
                    case "용인":
                        databaseReference.child("reco_resion").child(resionArr[i]).setValue(reco_resion.get용인() + 1);
                        break;
                    case "고양":
                        databaseReference.child("reco_resion").child(resionArr[i]).setValue(reco_resion.get고양() + 1);
                        break;
                    case "순천":
                        databaseReference.child("reco_resion").child(resionArr[i]).setValue(reco_resion.get순천() + 1);
                        break;
                    case "제주":
                        databaseReference.child("reco_resion").child(resionArr[i]).setValue(reco_resion.get제주() + 1);
                        break;
                    case "진주":
                        databaseReference.child("reco_resion").child(resionArr[i]).setValue(reco_resion.get진주() + 1);
                        break;
                    case "화성":
                        databaseReference.child("reco_resion").child(resionArr[i]).setValue(reco_resion.get화성() + 1);
                        break;
                    case "수원":
                        databaseReference.child("reco_resion").child(resionArr[i]).setValue(reco_resion.get수원() + 1);
                        break;
                    case "시흥":
                        databaseReference.child("reco_resion").child(resionArr[i]).setValue(reco_resion.get시흥() + 1);
                        break;
                    case "오산":
                        databaseReference.child("reco_resion").child(resionArr[i]).setValue(reco_resion.get오산() + 1);
                        break;
                }
            }
        }
    }
    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }
    private Bitmap rotate(Bitmap src, float degree) {
        // Matrix 객체 생성
        Matrix matrix = new Matrix();
        // 회전 각도 셋팅
        matrix.postRotate(degree);
        // 이미지와 Matrix 를 셋팅해서 Bitmap 객체 생성
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(),
                src.getHeight(), matrix, true);
    }
    private String getRealPathFromURI(Uri contentUri) {
        int column_index=0;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }
        return cursor.getString(column_index);
    }
}
