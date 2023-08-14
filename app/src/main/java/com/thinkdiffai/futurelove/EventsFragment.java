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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.thinkdiffai.futurelove.CustomView.Custom_loading;
import com.thinkdiffai.futurelove.databinding.FragmentEventsBinding;
import com.thinkdiffai.futurelove.databinding.FragmentHomeBinding;
import com.thinkdiffai.futurelove.model.comment.EventsUser.EventsUser;
import com.thinkdiffai.futurelove.model.comment.EventsUser.Sukien;
import com.thinkdiffai.futurelove.model.comment.EventsUser.SukienX;
import com.thinkdiffai.futurelove.service.api.ApiService;
import com.thinkdiffai.futurelove.service.api.RetrofitClient;
import com.thinkdiffai.futurelove.service.api.Server;
import com.thinkdiffai.futurelove.view.adapter.EventFragmentEventAdapter;
import com.thinkdiffai.futurelove.view.adapter.EventHomeAdapter;
import com.thinkdiffai.futurelove.view.adapter.StringAdapter;
import com.thinkdiffai.futurelove.view.adapter.StringAdapter1;
import com.thinkdiffai.futurelove.view.fragment.HomeFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventsFragment extends Fragment {
    private FragmentEventsBinding fragmentEventsBinding;
    private List<SukienX> sukienXList= new ArrayList<>();
    private List<String> stringList= new ArrayList<>();
    private EventFragmentEventAdapter eventFragmentEventAdapter;
    private StringAdapter stringAdapter;
    private StringAdapter1 stringAdapter1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentEventsBinding= FragmentEventsBinding.inflate(inflater, container, false);
        OnClickNavigation();
        // xu ly rcv in navigation
            XuLyRcycNavigation();
        // xu ly rcv events
            XulyRcy();
        // xu ly navigation
            XulyNavigation();
        fragmentEventsBinding.llEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(EventsFragment.this).navigate(R.id.action_eventsFragment_to_addEventFragment);
            }
        });
        //xu ly viewpaper
            ViewPaper();
        // xu ly loading
        XulyLoading();
        getDataEventUser();
        return fragmentEventsBinding.getRoot();
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
                        //sukienXList=s.getSukien();
                        for (SukienX a: s.getSukien()){
                            stringList.add(a.getTen_su_kien());
                            sukienXList.add(a);
                        }
                    }
                    Glide.with(getContext()).load(sukien.get(0).getSukien().get(0).getLink_nam_goc()).into(fragmentEventsBinding.imgPerson1Events);
                    Glide.with(getContext()).load(sukien.get(0).getSukien().get(0).getLink_nu_goc()).into(fragmentEventsBinding.imgPerson2Events);
                    stringAdapter.notifyDataSetChanged();
                    stringAdapter1.notifyDataSetChanged();
                    fragmentEventsBinding.rcvEvents.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int currentItem = fragmentEventsBinding.vp2Events.getCurrentItem();
                            int totalItems = eventFragmentEventAdapter.getItemCount();
                            if (currentItem < totalItems - 1) {
                                fragmentEventsBinding.vp2Events.setCurrentItem(currentItem + 1);
                            }
                        }
                    });
                    eventFragmentEventAdapter.SetData(sukienXList);
                    eventFragmentEventAdapter.notifyDataSetChanged();
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
        fragmentEventsBinding.btnCommentSk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCommentFragment();
            }
        });
        // Click btn Pairing
        fragmentEventsBinding.btnPairingSk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPairingFragment();
            }
        });
        // Click btn Home
        fragmentEventsBinding.btnHomeSk.setOnClickListener(new View.OnClickListener() {
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
    private void XuLyRcycNavigation(){
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        fragmentEventsBinding.rcvEventsNavigation.setLayoutManager(linearLayoutManager);
        stringAdapter= new StringAdapter(stringList);
        fragmentEventsBinding.rcvEventsNavigation.setAdapter(stringAdapter);
    }
    private void XulyNavigation(){
        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(getActivity(),fragmentEventsBinding.drawerEvents,fragmentEventsBinding.constraintLayoutEvents,R.string.Open,R.string.Close);
        fragmentEventsBinding.drawerEvents.addDrawerListener(toggle);
        toggle.syncState();
    }
    private  void  XulyLoading(){
        fragmentEventsBinding.customLoading.setVisibility(View.VISIBLE);

        // Start the animation of the progress bar
        Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.right_loading);
        fragmentEventsBinding.customLoading.startAnimation(anim);

        fragmentEventsBinding.customLoading.animation();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void XulyRcy(){
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
        fragmentEventsBinding.rcvEvents.setLayoutManager(linearLayoutManager);
        stringAdapter1= new StringAdapter1(stringList, new StringAdapter1.OnItemClickListener() {
            @Override
            public void onItemClick(String item) {

            }
        });
        fragmentEventsBinding.rcvEvents.setAdapter(stringAdapter1);
    }
    private void ViewPaper(){
        eventFragmentEventAdapter= new EventFragmentEventAdapter(sukienXList,getContext());
        fragmentEventsBinding.vp2Events.setAdapter(eventFragmentEventAdapter);
        fragmentEventsBinding.vp2Events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(EventsFragment.this).navigate(R.id.action_eventsFragment_to_eventAndCommentFragment);
            }
        });
    }
}