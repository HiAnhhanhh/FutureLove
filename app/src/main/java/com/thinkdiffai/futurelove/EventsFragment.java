package com.thinkdiffai.futurelove;

import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.thinkdiffai.futurelove.CustomView.Custom_loading;
import com.thinkdiffai.futurelove.model.comment.EventsUser.EventsUser;
import com.thinkdiffai.futurelove.model.comment.EventsUser.Sukien;
import com.thinkdiffai.futurelove.model.comment.EventsUser.SukienX;
import com.thinkdiffai.futurelove.service.api.ApiService;
import com.thinkdiffai.futurelove.service.api.RetrofitClient;
import com.thinkdiffai.futurelove.service.api.Server;
import com.thinkdiffai.futurelove.view.adapter.StringAdapter;
import com.thinkdiffai.futurelove.view.fragment.HomeFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventsFragment extends Fragment {

    private Custom_loading custom_loading;
    private Toolbar toolbar;
    private LinearLayoutCompat layoutCompat;
    private LinearLayoutCompat viewPager2;
    private DrawerLayout drawerLayout;
    private AppCompatButton home,commets,pairing;
    private RecyclerView rcv_events;
    private List<SukienX> sukienXList= new ArrayList<>();
    private List<String> stringList= new ArrayList<>();
    private StringAdapter stringAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_events, container, false);
        //get id
        custom_loading=view.findViewById(R.id.custom_loading);
        drawerLayout=view.findViewById(R.id.drawer_events);
        toolbar=view.findViewById(R.id.constraintLayout_events);
        layoutCompat=view.findViewById(R.id.ll_events);
        viewPager2=view.findViewById(R.id.vp2_events);
        rcv_events=view.findViewById(R.id.rcv_events_navigation);
        home=view.findViewById(R.id.btn_home_sk);
        pairing=view.findViewById(R.id.btn_pairing_sk);
        commets=view.findViewById(R.id.btn_comment_sk);
        OnClickNavigation();
        // xu ly rcv
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        rcv_events.setLayoutManager(linearLayoutManager);
        stringAdapter= new StringAdapter(stringList);
        rcv_events.setAdapter(stringAdapter);
        // xu ly navigation
        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(getActivity(),drawerLayout,toolbar,R.string.Open,R.string.Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        layoutCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(EventsFragment.this).navigate(R.id.action_eventsFragment_to_addEventFragment);
            }
        });
        //xu ly nhan vao su kien xem comment
        viewPager2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(EventsFragment.this).navigate(R.id.action_eventsFragment_to_eventAndCommentFragment);
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
        getDataEventUser();
        return view;
    }
    private void getDataEventUser(){
        ApiService apiService = RetrofitClient.getInstance(Server.DOMAIN2).getRetrofit().create(ApiService.class);
        Call<EventsUser> call = apiService.getEventUser(5);
        call.enqueue(new Callback<EventsUser>() {
            @Override
            public void onResponse(Call<EventsUser> call, Response<EventsUser> response) {
                if (response.isSuccessful() && response.body() != null) {
                    EventsUser eventsUser=response.body();
                    List<Sukien> sukien= eventsUser.getList_sukien();
                    for (Sukien s: sukien){
                        sukienXList=s.getSukien();
                        for (SukienX a: s.getSukien()){
                            stringList.add(a.getTen_su_kien());
                        }
                    }
                    stringAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<EventsUser> call, Throwable t) {

            }
        });
    }
    //
    //xu ly navigation button
    private void OnClickNavigation(){
        commets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCommentFragment();
            }
        });
        // Click btn Pairing
        pairing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPairingFragment();
            }
        });
        // Click btn Home
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToHomeFragment();
            }
        });
    }

    private void goToPairingFragment() {
        NavHostFragment.findNavController(EventsFragment.this).navigate(R.id.action_eventsFragment_to_pairingFragment);
    }

    private void goToHomeFragment() {
        NavHostFragment.findNavController(EventsFragment.this).navigate(R.id.action_eventsFragment_to_homeFragment);
    }

    private void goToCommentFragment() {
        NavHostFragment.findNavController(EventsFragment.this).navigate(R.id.action_eventsFragment_to_commentFragment2);
    }
}