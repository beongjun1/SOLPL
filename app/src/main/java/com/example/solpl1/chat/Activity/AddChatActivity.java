package com.example.solpl1.chat.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;

import com.example.solpl1.R;
import com.example.solpl1.databinding.ActivityAddChatBinding;

public class AddChatActivity extends AppCompatActivity {
    ActivityAddChatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }



}