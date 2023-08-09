package com.thinkdiffai.futurelove;

import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.thinkdiffai.futurelove.CustomView.Custom_loading;
import com.thinkdiffai.futurelove.view.fragment.HomeFragment;

public class EventsFragment extends Fragment {
   private Custom_loading custom_loading;
    private Toolbar toolbar;
    private LinearLayoutCompat layoutCompat;
   private DrawerLayout drawerLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_events, container, false);
        custom_loading=view.findViewById(R.id.custom_loading);
        drawerLayout=view.findViewById(R.id.drawer_events);
        toolbar=view.findViewById(R.id.constraintLayout_events);
        layoutCompat=view.findViewById(R.id.ll_events);
        // xu ly navigation
        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(getActivity(),drawerLayout,toolbar,R.string.Open,R.string.Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        layoutCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(EventsFragment.this).navigate(R.id.action_eventsFragment_to_fragmentEventsDetail);
            }
        });
        // xu ly loading
        custom_loading.setVisibility(View.VISIBLE);

        // Start the animation of the progress bar
        Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.right_loading);
        custom_loading.startAnimation(anim);

        custom_loading.animation();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return view;
    }
}