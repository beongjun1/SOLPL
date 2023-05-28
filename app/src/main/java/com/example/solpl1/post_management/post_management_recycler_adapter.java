package com.example.solpl1.post_management;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.solpl1.R;

import java.util.ArrayList;

public class post_management_recycler_adapter extends RecyclerView.Adapter<post_management_recycler_adapter.ViewHolder> {
    private ArrayList<post_management_item> post_management_items;

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
        if(post_management_items!=null){
            holder.onBind(post_management_items.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return post_management_items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView post_management_name;
        private TextView post_management_time;
        private TextView post_management_title;
        private TextView post_management_content;
        private ImageView post_management_picture;

        int text_content_max_length = 70;
        int text_title_max_length = 14;


        //
        public ViewHolder(View view) {
            super(view);
            post_management_name = view.findViewById(R.id.post_management_name);
            post_management_time = view.findViewById(R.id.post_management_time);
            post_management_title = view.findViewById(R.id.post_management_title);
            post_management_content = view.findViewById(R.id.post_management_content);
            post_management_picture = view.findViewById(R.id.post_management_picture);

        }

        public void onBind(post_management_item post_management_item) {
            String original_title_text = post_management_item.getTitle();
            String truncated_title_text = original_title_text.length() > text_content_max_length ? original_title_text.substring(0, text_content_max_length) + "..." : original_title_text;

            String original_content_text = post_management_item.getContent();
            String truncated_content_text = original_content_text.length() > text_content_max_length ? original_content_text.substring(0, text_content_max_length) + "..." : original_content_text;


            post_management_name.setText(post_management_item.getName());
            post_management_time.setText(post_management_item.getTime());
            post_management_title.setText(truncated_title_text);
            post_management_content.setText(truncated_content_text);
            post_management_picture.setImageResource(post_management_item.getPicture_id());

        }
    }
}
