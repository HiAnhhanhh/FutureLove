package com.thinkdiffai.futurelove.view.fragment;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.thinkdiffai.futurelove.R;
import com.thinkdiffai.futurelove.databinding.FragmentGenBabyResultBinding;

import java.util.Objects;

public class GenBabyResultFragment extends Fragment {

    private FragmentGenBabyResultBinding fragmentGenBabyResultBinding;

    String link_image_baby;

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
    }

    private void initView() {
        Glide.with(requireActivity()).load(link_image_baby).into(fragmentGenBabyResultBinding.imageIvResult);
    }

    private void initData() {
        getLinkImageBaby();
    }

    private void getLinkImageBaby() {
        Bundle args = getArguments();
        if(args!= null){
            link_image_baby = args.getString("uri_genBaby");
            Log.d("check_uri_baby", "getLinkImageBaby: "+ link_image_baby);

        }else{
            Log.d("check_uri_baby", "getLinkImageBaby: NONONO ");
        }
    }
}