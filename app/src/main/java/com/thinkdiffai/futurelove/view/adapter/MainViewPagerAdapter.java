package com.thinkdiffai.futurelove.view.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.thinkdiffai.futurelove.view.fragment.CommentFragment;
import com.thinkdiffai.futurelove.view.fragment.HistoryFragment;
import com.thinkdiffai.futurelove.view.fragment.HomeFragment;
import com.thinkdiffai.futurelove.view.fragment.PairingFragment;
import com.thinkdiffai.futurelove.view.fragment.TimelineFragment;

public class MainViewPagerAdapter extends FragmentStateAdapter {


    public MainViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public MainViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public MainViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new HomeFragment();
            case 1:
                return new CommentFragment();
            case 2:
                return new PairingFragment();
            case 3:
                return new TimelineFragment(); //HistoryFragment();
            case 4:
                return new HistoryFragment();
        }
        return new HomeFragment();
    }

    @Override
    public int getItemCount() {
        return 4;
    }  //5
}
