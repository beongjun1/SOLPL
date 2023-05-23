package com.example.solpl1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class PhoneAuth extends AppCompatActivity {
    private EditText mPhone,mPhoneAuthentication;
    private Button mBtnAuthentication,mBtnPhone;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mPhone = findViewById(R.id.et_phone);
        mPhoneAuthentication = findViewById(R.id.et_phone_authentication);
        mBtnPhone = findViewById(R.id.et_phone_btn);
        mBtnAuthentication = findViewById(R.id.et_phone_authentication_btn);

        // 인증번호 요청 버튼 눌렸을때
        mBtnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isEditTextEmpty(mPhone)){
                    Toast.makeText(PhoneAuth.this, "전화번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        // 인증하기 버튼 눌렸을때
        mBtnAuthentication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isEditTextEmpty(mPhoneAuthentication)){
                    Toast.makeText(PhoneAuth.this, "인증번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }
    //빈 필드가 있는지 확인하는 코드
    private boolean isEditTextEmpty(EditText editText) {
        return editText.getText().toString().trim().length() == 0;
    }
}
