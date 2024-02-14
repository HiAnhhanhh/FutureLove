package com.thinkdiffai.futurelove.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.thinkdiffai.futurelove.databinding.FragmentChooseVideoBinding;

public class ChooseVideoFragment extends Fragment {

    String receivedData;

    ExoPlayer exoPlayer;
    StyledPlayerView styledPlayerView;
    FragmentChooseVideoBinding fragmentChooseVideoBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentChooseVideoBinding = FragmentChooseVideoBinding.inflate(inflater,container,false);
        return fragmentChooseVideoBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initView();
        initActionAll();
    }

    private void initActionAll() {
        fragmentChooseVideoBinding.backBtn.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });
    }

    private void initAction(ExoPlayer exoPlayer) {
        fragmentChooseVideoBinding.useVideoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseVideoFragmentDirections.ActionChooseVideoFragmentToPickImageToSwapFragment action = ChooseVideoFragmentDirections.actionChooseVideoFragmentToPickImageToSwapFragment(receivedData);
                NavHostFragment.findNavController(ChooseVideoFragment.this).navigate(action);
                exoPlayer.stop();
                Log.d("check_url_your_video", "onClick: "+ receivedData);
            }
        });
    }

    private void initView() {
        loadUrlVideo(receivedData);
    }

    private void loadUrlVideo(String receivedData) {
        styledPlayerView = fragmentChooseVideoBinding.videoSwapFace;
        exoPlayer = new ExoPlayer.Builder(requireActivity()).build();
        styledPlayerView.setPlayer(exoPlayer);
        MediaItem mediaItem = MediaItem.fromUri(receivedData);
        exoPlayer.addMediaItem(mediaItem);
        exoPlayer.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                if(playbackState == Player.STATE_READY){
                    exoPlayer.setPlayWhenReady(true);
                }
            }
        });
        exoPlayer.prepare();
        exoPlayer.play();
        initAction(exoPlayer);
    }

    private void initData() {
        Bundle args = getArguments();
        if (args != null) {
            receivedData = args.getString("url_video");
            Log.d("check_pass_data", "initData: "+ receivedData);
        }else{
            Log.d("check_pass_data", "initData: NO NO NO");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        exoPlayer.release();
    }
}