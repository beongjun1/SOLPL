package com.example.solpl1;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.solpl1.mypage.RECO_RESION;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mFireBaseAuth;     // 파이어베이스 인증
    private DatabaseReference mDatabaseRef; // 실시간 데이터베이스
    private EditText mEtName, mEtEmail, mEtPassword, mEtPasswordConfirm;
    private Button mBtnRegister; // 회원가입 버튼
    private RadioGroup radioGroup;
    private RadioButton radioButtonMan, radioButtonWoman;

    private String checked = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();

        radioGroup = findViewById(R.id.radioGroup);
        radioButtonMan = findViewById(R.id.radioButtonMan);
        radioButtonWoman = findViewById(R.id.radioButtonWoman);

        mFireBaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mEtName = findViewById(R.id.et_name);
        mEtEmail = findViewById(R.id.et_email);
        mEtPassword = findViewById(R.id.et_password);
        mEtPasswordConfirm = findViewById(R.id.et_pwd_equals);


        mBtnRegister = findViewById(R.id.sign_up);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // 라디오 버튼의 상태가 변경될 때마다 호출됩니다.
                // checkedId는 현재 선택된 라디오 버튼의 ID입니다.

                if (checkedId == R.id.radioButtonMan) {
                    checked = "남성";
                } else if (checkedId == R.id.radioButtonWoman) {
                    checked = "여성";
                }
            }
        });
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 입력필드가 비어있는지 확인
                if (isEditTextEmpty(mEtEmail) || isEditTextEmpty(mEtPassword) || isEditTextEmpty(mEtName)) {
                    Toast.makeText(RegisterActivity.this, "입력필드를 모두 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 비밀번호가 같은지 확인
                if (!isPasswordSame(mEtPassword, mEtPasswordConfirm)) {
                    Toast.makeText(RegisterActivity.this, "비밀번호가 다릅니다. 다시 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 이메일 형식이 아닌 경우
                if (!isValidEmail(mEtEmail.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "올바른 이메일 형식이 아닙니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 비밀번호가 6자리 미만인 경우
                if (mEtPassword.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "비밀번호는 6자리 이상이어야 합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (checked.equals("")){
                    Toast.makeText(RegisterActivity.this, "성별을 체크해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 회원가입 처리 시작
                String strEmail = mEtEmail.getText().toString();
                String strPwd = mEtPassword.getText().toString();
                String strName = mEtName.getText().toString();

                // 이미지 URL을 default 이미지로 설정


                // Firebase Auth 진행
                mFireBaseAuth.createUserWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mFireBaseAuth.getCurrentUser();
                            UserAccount account = new UserAccount();
                            account.setIdToken(firebaseUser.getUid());
                            account.setEmailId(firebaseUser.getEmail());
                            account.setName(strName);
                            account.setPassword(strPwd);
                            account.setReco_text("영수증인식텍스트");
                            account.setBadge("대표뱃지");
                            account.setPoint(0);
                            account.setUserRating(3.0F);

                            account.setGender(checked);
                            mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);
                            Toast.makeText(RegisterActivity.this, "회원가입에 성공했습니다.", Toast.LENGTH_SHORT).show();

                            RECO_RESION reco_resion =new RECO_RESION();
                            reco_resion.set순천(0);
                            mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).child("reco_resion").setValue(reco_resion);
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(RegisterActivity.this, "회원가입에 실패했습니다. 오류: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }


    // 빈 필드가 있는지 확인하는 코드
    private boolean isEditTextEmpty(EditText editText) {
        return editText.getText().toString().trim().length() == 0;
    }

    // 패스워드가 같은지 확인하는 코드
    private boolean isPasswordSame(EditText password1, EditText password2) {
        return password1.getText().toString().equals(password2.getText().toString());
    }

    // 이메일 형식인지 아닌지 파악하는 코드
    private boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
