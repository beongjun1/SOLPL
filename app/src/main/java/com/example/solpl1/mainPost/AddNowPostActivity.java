package com.example.solpl1.mainPost;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.Presentation;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.solpl1.R;
import com.example.solpl1.UserAccount;
import com.example.solpl1.databinding.ActivityAddNowPostBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Date;

public class AddNowPostActivity extends AppCompatActivity {

    ActivityAddNowPostBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    Uri uri;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNowPostBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        dialog  = new Dialog(AddNowPostActivity.this);
        dialog.setContentView(R.layout.dialog_loading);


        database.getReference().child("UserAccount")
                .child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // 프로필, 이름
        database.getReference().child("UserAccount")
                .child(auth.getCurrentUser()
                        .getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserAccount userAccount = snapshot.getValue(UserAccount.class);
                        if(userAccount.getImageUrl()== null){                                       // 기본 프로필 이미지 설정
                            Picasso.get()                                                           //유저 프로필
                                    .load(R.drawable.default_profile)
                                    .into(binding.profile);
                        } else {
                            Picasso.get()                                                           //유저 프로필
                                    .load(userAccount.getImageUrl())
                                    .placeholder(R.drawable.default_profile)
                                    .into(binding.profile);
                        }
                        String text = userAccount.getName() + "(" + userAccount.getEmailId() + ")";
                        binding.name.setText(text);              // 유저이름
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        binding.nowpostNewAddPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,10);
            }
        });



        // 게시글 올리기 버튼 눌렀을때
        binding.nowpostNewAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                // firebase storage에 사진 저장 저장
                final StorageReference reference = storage.getReference().child("nowPosts")
                        .child(FirebaseAuth.getInstance().getUid())
                        .child(new Date().getTime()+"");
                reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            //firebase database에 내용 저장
                            @Override
                            public void onSuccess(Uri uri) {
                                NowPost nowPost = new NowPost();
                                nowPost.setPostImage(uri.toString());
                                nowPost.setPostDescription(binding.nowpostDescription.getText().toString());
                                nowPost.setPostedBy(FirebaseAuth.getInstance().getUid());
                                nowPost.setPostedAt(new Date().getTime());

                                database.getReference().child("nowPosts")
                                        .push().setValue(nowPost).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                dialog.cancel();
                                                Toast.makeText(AddNowPostActivity.this, "게시글이 등록되었습니다.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });
                    }
                });
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data.getData() != null){
            uri = data.getData();
            binding.nowpostNewImage.setImageURI(uri);
            binding.nowpostNewImage.setVisibility(View.VISIBLE);
        }
    }
}