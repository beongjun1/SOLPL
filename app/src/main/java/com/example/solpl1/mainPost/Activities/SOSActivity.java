package com.example.solpl1.mainPost.Activities;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.solpl1.R;

import java.time.LocalTime;
import java.util.Timer;
import java.util.TimerTask;

public class SOSActivity extends AppCompatActivity {

    Button Okbtn;
    double cur_lat, cur_lon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);
        LocalTime now = null;
        int second = 0;
        final int[] nowSecond = new int[1];
        Timer time = new Timer();
        Okbtn = findViewById(R.id.OKbtn);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(SOSActivity.this,"권한이 없습니다",Toast.LENGTH_SHORT).show();
            return;
        }
        Location loc_Current = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        cur_lat = loc_Current.getLatitude(); //위도
        cur_lon = loc_Current.getLongitude(); //경도

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            now=LocalTime.now();
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            second=now.getSecond();
        }
        nowSecond[0] =second;
        int finalSecond = second;
        time.schedule(new TimerTask() {
            @Override
            public void run() {
                nowSecond[0] +=1;
                if(nowSecond[0]-10>= finalSecond){
                    String phoneNumber = "01089990266";
                    String smsBody = "긴급 구조 요청, 위치는 위도:"+cur_lat+", 경도:"+cur_lon;

                    SendSMS(phoneNumber, smsBody);
                    System.exit(0);
                }
            }
        },0,1000);
        Okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void SendSMS(String phoneNumber, String message){

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber,null,message,null,null);
    }
}
