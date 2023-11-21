package com.thinkdiffai.futurelove.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thinkdiffai.futurelove.R;
import com.thinkdiffai.futurelove.databinding.FragmentHomeBinding;
import com.thinkdiffai.futurelove.databinding.FragmentListVideoBinding;


public class ListVideoFragment extends Fragment {

    private FragmentListVideoBinding fragmentListVideoBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentListVideoBinding = FragmentListVideoBinding.inflate(inflater, container, false);
        return fragmentListVideoBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}