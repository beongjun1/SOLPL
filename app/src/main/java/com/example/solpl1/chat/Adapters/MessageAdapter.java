package com.example.solpl1.chat.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.solpl1.R;
import com.example.solpl1.chat.Models.MessageModel;
import com.example.solpl1.databinding.ActivityChatDetailBinding;
import com.example.solpl1.databinding.SampleReciverBinding;
import com.example.solpl1.databinding.SampleSenderBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter{

    ArrayList<MessageModel> messageModels;
    Context context;

    int SENDER_VIEW_TYPE = 1;
    int RECIVER_VIEW_TYPE = 2;

    public MessageAdapter(ArrayList<MessageModel> messageModels, Context context) {
        this.messageModels = messageModels;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == SENDER_VIEW_TYPE){       //  메세지 보내는 화면
            View view = LayoutInflater.from(context).inflate(R.layout.sample_sender,parent,false);
            return new SenderViewHolder(view);
        }
        else {                                  // 메세지 받는 화면
            View view = LayoutInflater.from(context).inflate(R.layout.sample_reciver,parent,false);
            return new ReciverViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position){
        if(messageModels.get(position).getUserId().equals(FirebaseAuth.getInstance().getUid())){
            return SENDER_VIEW_TYPE;
        }
        else {
            return RECIVER_VIEW_TYPE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageModel messageModel = messageModels.get(position);
        if(holder.getClass() == SenderViewHolder.class){
            ((SenderViewHolder)holder).senderBinding.chatMessage.setText(messageModel.getMessage());
        }
        else {
            ((ReciverViewHolder)holder).reciverBinding.chatMessage.setText(messageModel.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return messageModels.size();
    }

    public class ReciverViewHolder extends RecyclerView.ViewHolder{
        SampleReciverBinding reciverBinding;

        public ReciverViewHolder(@NonNull View itemView) {
            super(itemView);
            reciverBinding = SampleReciverBinding.bind(itemView);
        }

    }

    public class SenderViewHolder extends RecyclerView.ViewHolder {

        SampleSenderBinding senderBinding;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderBinding = SampleSenderBinding.bind(itemView);
        }
    }


}
