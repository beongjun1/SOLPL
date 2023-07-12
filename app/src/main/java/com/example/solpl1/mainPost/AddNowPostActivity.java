package com.example.solpl1.mainPost;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.solpl1.R;
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

import java.util.Date;

public class AddNowPostActivity extends AppCompatActivity {

    ActivityAddNowPostBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_now_post);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        database.getReference().child("UserAccount")
                .child(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

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
                                                Toast.makeText(getBaseContext() , "게시글이 등록되었습니다.",Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });
                    }
                });
            }
        });

        binding.getRoot();
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