package com.thinkdiffai.futurelove.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.thinkdiffai.futurelove.databinding.FragmentVideoFullScreenBinding;

public class VideoFullScreenFragment extends Fragment {

    private FragmentVideoFullScreenBinding fragmentVideoFullScreenBinding;
    StyledPlayerView playerView;
    SimpleExoPlayer player;
    String link_video;
    Handler handler;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentVideoFullScreenBinding = FragmentVideoFullScreenBinding.inflate(LayoutInflater.from(requireContext()),container,false);
        return fragmentVideoFullScreenBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        handler = new Handler();
        initAction();
    }

    private void initAction() {
        fragmentVideoFullScreenBinding.videoSwapFace.setOnClickListener(v -> {
            showButton();
            playerView.showController();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideButton();
                }
            },3000);
        });

        fragmentVideoFullScreenBinding.backBtn.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });
    }

    private void hideButton() {
        fragmentVideoFullScreenBinding.backBtn.setVisibility(View.GONE);
    }

    private void showButton() {
        fragmentVideoFullScreenBinding.backBtn.setVisibility(View.VISIBLE);
    }

    private void initData() {
        loadVideo();
        loadUrlVideo();
        displayVideo();
    }
    private void displayVideo() {
        MediaItem mediaItem = MediaItem.fromUri(link_video);
        player.setMediaItem(mediaItem);
        player.prepare();
        player.play();
    }
    private void loadUrlVideo() {
        Bundle bundle = getArguments();
        link_video = bundle.getString("url_video_full");
    }
    private void loadVideo() {
        playerView = fragmentVideoFullScreenBinding.videoSwapFace;
        playerView.hideController();
        initializePlayer();
    }

    private void initializePlayer() {
        player = new SimpleExoPlayer.Builder(requireContext()).build();
        playerView.setPlayer(player);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        player.release();
    }

    @Override
    public void onStop() {
        super.onStop();
        player.release();
    }
}