package com.thinkdiffai.futurelove.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.thinkdiffai.futurelove.R;
import com.thinkdiffai.futurelove.databinding.FragmentFullScreenImageBinding;
import com.thinkdiffai.futurelove.databinding.FragmentVideoFullScreenBinding;


public class FullScreenImageFragment extends Fragment {

    FragmentFullScreenImageBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFullScreenImageBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    private void initData() {
        loadImv();
    }

    private void loadImv() {
        Bundle bundle = getArguments();
        String imv = null;
        if (bundle != null) {
            imv = bundle.getString("url_image_swap");
        }
        Glide.with(this).load(imv).into(binding.fullScreenImv);
    }
}