package com.example.solpl1.chat.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.solpl1.chat.Fragments.ChatFragment1;
import com.example.solpl1.chat.Fragments.ChatFragment2;
import com.example.solpl1.chat.Fragments.ChatFragment3;


public class FragmentsAdapter extends FragmentStateAdapter {


    public FragmentsAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: return new ChatFragment1();
            case 1: return new ChatFragment2();
            case 2: return new ChatFragment3();
            default: return new ChatFragment1();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }




}
