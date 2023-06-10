package com.example.solpl1.post_management;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.solpl1.R;
import com.example.solpl1.mypage.my_page_writing_activity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class post_management_recycler_adapter extends RecyclerView.Adapter<post_management_recycler_adapter.ViewHolder> {
    private ArrayList<post_management_item> post_management_items;
    private DatabaseReference databaseReference; // Firebase Realtime Database 참조 객체


    @SuppressLint("NotifyDataSetChanged")
    public void setPost_management_items(ArrayList<post_management_item> list){
        this.post_management_items = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_management_recylcer,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (post_management_items != null) {

            post_management_item item = post_management_items.get(position);
            // ViewHolder에 데이터를 바인딩
            holder.onBind(item, position);
        }
    }
    public void removeItem(int position) {
        // 데이터 리스트가 null이 아니고, 해당 position이 유효한지 확인
        if (post_management_items != null && position >= 0 && position < post_management_items.size()) {
            // 데이터 리스트에서 해당 아이템을 삭제하는 작업을 수행
            post_management_items.remove(position);
            notifyItemRemoved(position);
        }
    }
    public int getItemCount() {
        if (post_management_items == null) {
            return 0; // 리스트가 null인 경우, 아이템 개수는 0으로 처리
        } else {
            return post_management_items.size();
        }
    }
    private void deleteDataFromDatabase(String postId, View itemView, int position) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("post_database");
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String userEmail = user.getEmail();
        // 해당 post_id의 데이터를 삭제
        databaseRef.child(postId).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // 데이터 삭제 성공 시 실행되는 코드
                        Toast.makeText(itemView.getContext(), "데이터 삭제 성공", Toast.LENGTH_SHORT).show();
                        // 어댑터에서 해당 아이템을 삭제하고 RecyclerView 갱신을 호출합니다.
                        removeItem(position);
                        notifyDataSetChanged();
                        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                        String filePath = userEmail + "/post/" +postId + "/";
                        StorageReference imageRef = storageRef.child(filePath);
                        imageRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                            @Override
                            public void onSuccess(ListResult listResult) {
                                for (StorageReference imageRef : listResult.getItems()) {
                                    imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // 이미지 삭제 성공 메시지 표시
                                            Log.d("ProfileEditActivity", "이미지가 삭제되었습니다.");
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // 이미지 삭제 실패 메시지 표시
                                            Log.d("ProfileEditActivity", "이미지가 삭제되지 않았습니다.");
                                        }
                                    });
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // 이미지 목록 가져오기 실패 메시지 표시
                                Log.d("ProfileEditActivity", "이미지 목록을 가져오지 못했습니다.");
                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // 데이터 삭제 실패 시 실행되는 코드
                        Toast.makeText(itemView.getContext(), "데이터 삭제 실패", Toast.LENGTH_SHORT).show();
                        Log.e("DeleteData", "데이터 삭제 실패: " + e.getMessage());
                    }
                });
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView post_management_name;
        private TextView post_management_time;
        private TextView post_management_title;
        private TextView post_management_content;
        private ImageView post_management_picture;
        int text_content_max_length = 70;
        int text_title_max_length = 14;

        Button delete_btn;
        private int currentPosition; // 현재 아이템의 position을 저장할 변수

        //
        public ViewHolder(View view) {
            super(view);
            post_management_name = view.findViewById(R.id.post_management_name);
            post_management_time = view.findViewById(R.id.post_management_time);
            post_management_title = view.findViewById(R.id.post_management_title);
            post_management_content = view.findViewById(R.id.post_management_content);
            post_management_picture = view.findViewById(R.id.post_management_picture);

            delete_btn = view.findViewById(R.id.delete_post_button);

            databaseReference = FirebaseDatabase.getInstance().getReference(); // Firebase Realtime Database 참조 객체 초기화
            delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 클릭된 포지션에 해당하는 데이터를 데이터베이스에서 삭제
                    String postId = post_management_items.get(getAdapterPosition()).getPostId();
                    deleteDataFromDatabase(postId, view,currentPosition);
                }
            });

        }

        public void onBind(post_management_item post_management_item, int position) {
            currentPosition = position; // 현재 아이템의 position 저장

            String original_title_text = post_management_item.getTitle();
            String truncated_title_text = original_title_text.length() > text_title_max_length ? original_title_text.substring(0, text_title_max_length) + "..." : original_title_text;

            String original_content_text = post_management_item.getContent();
            String truncated_content_text = original_content_text.length() > text_content_max_length ? original_content_text.substring(0, text_content_max_length) + "..." : original_content_text;


            post_management_name.setText(post_management_item.getName());
            post_management_time.setText(post_management_item.getTime());
            post_management_title.setText(truncated_title_text);
            post_management_content.setText(truncated_content_text);

            ArrayList<String> urlList = post_management_item.getImageUrl();
            if (urlList != null && !urlList.isEmpty()) {
                String imageUrl = urlList.get(0); // 첫 번째 이미지 URL 사용 또는 원하는 로직에 맞게 선택
                Picasso.get().load(imageUrl).into(post_management_picture);
            }

//            ArrayList<String> profile_img_url = post_management_item.getProfile_img_url();
//            if (profile_img_url != null && !profile_img_url.isEmpty()) {
//                String profile_url = profile_img_url.get(0); // 첫 번째 이미지 URL 사용 또는 원하는 로직에 맞게 선택
//                Picasso.get().load(imageUrl).into(post_management_picture);
//            }
        }
    }
}
