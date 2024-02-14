package com.thinkdiffai.futurelove.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thinkdiffai.futurelove.R;
import com.thinkdiffai.futurelove.databinding.FragmentHomeSwapImageBinding;

public class HomeSwapImageFragment extends Fragment {

    String check_type ;
    private FragmentHomeSwapImageBinding fragmentHomeSwapImageBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentHomeSwapImageBinding = FragmentHomeSwapImageBinding.inflate(inflater,container,false);
        return fragmentHomeSwapImageBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAction();
    }

    private void initAction() {
        fragmentHomeSwapImageBinding.genBaby.setOnClickListener(v ->{
            check_type = "GenBaby";
//            HomeSwapImageFragmentDirections action = HomeSwapImageFragmentDirections.actionHomeSwapImageFragmentToSwapGenBabyFragment(check_type);
//            NavHostFragment.findNavController(HomeSwapImageFragment.this).navigate(action);
        });

        fragmentHomeSwapImageBinding.swapImage.setOnClickListener(v->{
            check_type="Gen_2_Image";
//            HomeSwapImageFragmentDirections.ActionHomeSwapImageFragmentToSwapGenBabyFragment action = HomeSwapImageFragmentDirections.actionHomeSwapImageFragmentToSwapGenBabyFragment(check_type);
//            NavHostFragment.findNavController(HomeSwapImageFragment.this).navigate(action);
        });
    }
}