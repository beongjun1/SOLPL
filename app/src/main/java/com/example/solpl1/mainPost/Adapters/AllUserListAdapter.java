package com.example.solpl1.mainPost.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.solpl1.R;
import com.example.solpl1.UserAccount;
import com.example.solpl1.chat.Adapters.ChatUserListAdapter;
import com.example.solpl1.chat.Models.ChatItem;
import com.example.solpl1.databinding.RecyclerviewChatUserListBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class AllUserListAdapter extends RecyclerView.Adapter<AllUserListAdapter.ViewHolder>{
    ArrayList<UserAccount> list;
    Context context;
    FirebaseAuth auth;
    FirebaseDatabase database;
    String name;

    public AllUserListAdapter(ArrayList<UserAccount> list, Context context) {
        this.list = list;
        this.context = context;
    }
    public void setFilteredList(ArrayList<UserAccount> filteredList){
        this.list = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_chat_user_list, parent, false);
        return new AllUserListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserAccount userAccount = list.get(position);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        database.getReference().child("UserAccount").child(userAccount.getIdToken()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserAccount userAccount = snapshot.getValue(UserAccount.class);
                Picasso.get()
                        .load(userAccount.getImageUrl())
                        .placeholder(R.drawable.default_profile)
                        .into(holder.binding.profile);

                String email = userAccount.getEmailId();
                String email2 = email.substring(0, email.lastIndexOf("@"));
                name = userAccount.getName() + "(" + email2 + ")";
                holder.binding.name.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        RecyclerviewChatUserListBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RecyclerviewChatUserListBinding.bind(itemView);
        }
    }
}
