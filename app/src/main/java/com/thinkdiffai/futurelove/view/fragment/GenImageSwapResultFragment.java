package com.thinkdiffai.futurelove.view.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.thinkdiffai.futurelove.databinding.FragmentGenBabyResultBinding;
import com.thinkdiffai.futurelove.model.GenBabyModel;
import com.thinkdiffai.futurelove.model.GenImageModel;
import com.thinkdiffai.futurelove.view.Downloader;

import java.io.Serializable;

public class GenImageSwapResultFragment extends Fragment {
    private FragmentGenBabyResultBinding fragmentGenBabyResultBinding;
    String link_image_baby;
    String check_state ;
    GenBabyModel detailGenBabyModel;
    GenImageModel suKienImageModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentGenBabyResultBinding = FragmentGenBabyResultBinding.inflate(inflater, container, false);
        return fragmentGenBabyResultBinding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initView();
        initAction();
    }
    private void initAction() {
        fragmentGenBabyResultBinding.backBtn.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });
        fragmentGenBabyResultBinding.downloadVideoBtn.setOnClickListener(v -> {
            Downloader.downloadVideo(requireActivity(),link_image_baby,"image_swap_in_future_love");
        });
        if(check_state.equals("baby")){
            link_image_baby = detailGenBabyModel.getGenBabyModel().get(0).getLink_da_swap();
            Downloader.downloadVideo(requireActivity(),link_image_baby,"image_swap_in_future_love_"+ detailGenBabyModel.getGenBabyModel().get(0).getId_image());
        }else{
            link_image_baby = suKienImageModel.getSuKienImageModel().getLink_da_swap();
            Downloader.downloadVideo(requireActivity(),link_image_baby,"image_swap_in_future_love_"+ suKienImageModel.getSuKienImageModel().getLink_da_swap());
        }
    }
    private void initView() {
        if(check_state.equals("baby")){
            link_image_baby = detailGenBabyModel.getGenBabyModel().get(0).getLink_da_swap();
            Glide.with(requireActivity()).load(link_image_baby).into(fragmentGenBabyResultBinding.imageIvResult);

        }else{
            link_image_baby = suKienImageModel.getSuKienImageModel().getLink_da_swap();
            Glide.with(requireActivity()).load(link_image_baby).into(fragmentGenBabyResultBinding.imageIvResult);
        }
    }
    private void initData() {
        loadState();
        getLinkImageBaby();
    }
    private void loadState() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("check_state_swap",0);
        check_state = sharedPreferences.getString("state","");
    }
    private void getLinkImageBaby() {
        Bundle args = getArguments();
        if(args!= null){
            if(check_state.equals("baby")){
                detailGenBabyModel = (GenBabyModel) args.getSerializable("genBaby");
            }else{
                suKienImageModel = (GenImageModel) args.getSerializable("genImage");
            }
        }else{
            Log.d("check_uri_baby", "getLinkImageBaby: NONONO ");
        }
    }
}