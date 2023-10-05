package com.example.solpl1.mainPost.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.solpl1.mainPost.Fragments.MainPostFragment;
import com.example.solpl1.mainPost.Fragments.NowPostFragment;

public class PostFragmentsAdapter extends FragmentStateAdapter {


    public PostFragmentsAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: return new MainPostFragment();
            case 1: return new NowPostFragment();
            default: return new MainPostFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

}