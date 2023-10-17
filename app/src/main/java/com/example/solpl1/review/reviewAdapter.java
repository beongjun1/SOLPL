package com.example.solpl1.review;

import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.solpl1.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class reviewAdapter extends RecyclerView.Adapter<reviewAdapter.ViewHolder> {
    private ArrayList<review_item> review_items;
    private String placeTitle;
    private int point;

    public void setReviewAdapter(ArrayList<review_item> review_items) {
        this.review_items = review_items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public reviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_recycler,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull reviewAdapter.ViewHolder holder, int position) {
        if(review_items!=null){
            review_item item = review_items.get(position);
            holder.onBind(item,position);
        }
    }

    @Override
    public int getItemCount() {
        if(review_items ==null){
            return 0;
        }
        else
         return review_items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title,content,date;
        private RatingBar ratingBar;
        private Button button;
        private ImageView img;
        private int currentPosition;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.review_management_loc);
            content = itemView.findViewById(R.id.review_management_text);
            ratingBar= itemView.findViewById(R.id.review_management_rating_bar);
            img = itemView.findViewById(R.id.review_management_img);
            button = itemView.findViewById(R.id.review_management_delete);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String reviewId = review_items.get(getAdapterPosition()).getReview_id();
                    Log.d("review Id : ", "review Id : "+reviewId);
                    deleteDataFromDatabase(reviewId,itemView,currentPosition);
                }
            });
        }

        public void onBind(review_item reviewItem, int position) {
            currentPosition = position;

            title.setText(reviewItem.getTitle());
            placeTitle=reviewItem.getTitle();
            content.setText(reviewItem.getContent());
            float rating = reviewItem.getRatingBar();
            ratingBar.setRating(rating);
            String review_img = reviewItem.getImgUrl();
            if(review_img!=null && !review_img.isEmpty()){
                Picasso.get().load(review_img).into(img);
            }
        }
    }

    private void deleteDataFromDatabase(String reviewId, View itemView, int position) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String currentUserIdToken = user.getUid();
        String userEmail = user.getEmail();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserAccount").child(currentUserIdToken);
        DatabaseReference placesReference = FirebaseDatabase.getInstance().getReference("places").child(placeTitle).child("reviews");

        placesReference.child(reviewId).removeValue();

        databaseReference.child("point").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                point=snapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.child("reviews").child(reviewId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                removeItem(position);
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                String filePath = userEmail + "/review/"+reviewId+"/";
                StorageReference imageRef = storageReference.child(filePath);
                imageRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference imageRef : listResult.getItems()) {
                            imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d("review Activity : ", "이미지 삭제 되었습니다");
                                    databaseReference.child("point").setValue(point-100);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(itemView.getContext(), "이미지 삭제 실패했습니다.",Toast.LENGTH_SHORT).show();
                                    Log.d("review Activity : ", "이미지 삭제 실패");
                                }
                            });

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(itemView.getContext(), "이미지 목록이 없습니다.",Toast.LENGTH_SHORT).show();
                        Log.d("review Activity : ", "이미지 목록 가져오지 못함");
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(itemView.getContext(), "데이터 삭제 실패",Toast.LENGTH_SHORT).show();
                Log.d("review Activity : ", "데이터 삭제 실패 " +e.getMessage());

            }
        });
    }

    private void removeItem(int position) {
        if(review_items!= null &&position >=0 &&position<review_items.size()){
            review_items.remove(position);
            notifyItemRemoved(position);
        }
    }
}
