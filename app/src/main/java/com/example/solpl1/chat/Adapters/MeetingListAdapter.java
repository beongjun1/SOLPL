package com.example.solpl1.chat.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.solpl1.R;
import com.example.solpl1.chat.Models.MeetingModel;
import com.example.solpl1.databinding.DialogMeetingListBinding;
import com.example.solpl1.databinding.RecyclerviewMeetingListBinding;

import java.util.ArrayList;

public class MeetingListAdapter extends RecyclerView.Adapter<MeetingListAdapter.ViewHolder>{
    ArrayList<MeetingModel> list;
    Context context;

    public MeetingListAdapter(ArrayList<MeetingModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_meeting_list, parent, false);
        return new MeetingListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MeetingModel meetingModel = list.get(position);

        holder.binding.meetingDate.setText(meetingModel.getDate());
        holder.binding.meetingTitle.setText(meetingModel.getTitle());
        holder.binding.meetingDescription.setText(meetingModel.getDescription());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        RecyclerviewMeetingListBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RecyclerviewMeetingListBinding.bind(itemView);
        }
    }
}
