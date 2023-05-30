package com.example.solpl1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneAuth extends AppCompatActivity {
    private EditText mPhone,mPhoneAuthentication;
    private Button mBtnAuthentication,mBtnPhone;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mAuth = FirebaseAuth.getInstance();
        mAuth.setLanguageCode("ko");

        mPhone = findViewById(R.id.et_phone);
        mPhoneAuthentication = findViewById(R.id.et_phone_authentication);
        mBtnPhone = findViewById(R.id.et_phone_btn);
        mBtnAuthentication = findViewById(R.id.et_phone_authentication_btn);

        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Toast.makeText(PhoneAuth.this, "인증코드가 발송되었습니다. 60초 이내에 입력해주세요.", Toast.LENGTH_SHORT).show();

                //전화번호 수정 불가
                mPhone.setFocusable(false);
                mPhone.setClickable(false);

                // 인증하기 버튼 눌렸을때
                mBtnAuthentication.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(isEditTextEmpty(mPhoneAuthentication)){
                            Toast.makeText(PhoneAuth.this, "인증번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                        }
                        if(mPhoneAuthentication.getText().toString().equals(phoneAuthCredential.getSmsCode())) {
                            signInWithPhoneAuthCredential(phoneAuthCredential);
                        }
                        else{
                            Toast.makeText(PhoneAuth.this,"인증번호가 틀렸습니다. 다시 확인해주세요.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(PhoneAuth.this, "오류가 발생했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            }
        };

        // 인증번호 요청 버튼 눌렸을때
        mBtnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isEditTextEmpty(mPhone)){
                    Toast.makeText(PhoneAuth.this, "전화번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                PhoneAuthOptions options =
                        PhoneAuthOptions.newBuilder(mAuth)
                                .setPhoneNumber("+82"+mPhone.getText().toString())       // Phone number to verify
                                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                                .setActivity(PhoneAuth.this)
                                .build();
                PhoneAuthProvider.verifyPhoneNumber(options);
            }
        });


    }
    //빈 필드가 있는지 확인하는 코드
    private boolean isEditTextEmpty(EditText editText) {
        return editText.getText().toString().trim().length() == 0;
    }
    //로그인하는 코드
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = task.getResult().getUser();
                            // Update UI
                            Intent intent = new Intent(PhoneAuth.this,MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(PhoneAuth.this, "전화번호 인증 성공", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("로그인 실패","signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }
}
