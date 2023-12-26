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

import com.bumptech.glide.load.data.ExifOrientationStream;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.thinkdiffai.futurelove.R;
import com.thinkdiffai.futurelove.databinding.FragmentVideoResultsBinding;

public class VideoResultsFragment extends Fragment {

    private FragmentVideoResultsBinding fragmentVideoResultsBinding;


    private String link_vid_swap,link_vid_goc;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentVideoResultsBinding = FragmentVideoResultsBinding.inflate(inflater, container, false);
        return fragmentVideoResultsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initView();
    }

    private void initView() {
        loadUrlVideo();
        loadUrlVideo_Goc();
    }

    private void loadUrlVideo_Goc() {
        StyledPlayerView styledPlayerView1 = fragmentVideoResultsBinding.videoSwapFace;
        ExoPlayer exoPlayer1 = new ExoPlayer.Builder(getActivity()).build();
        styledPlayerView1.setPlayer(exoPlayer1);
        MediaItem mediaItem1 = MediaItem.fromUri(link_vid_goc);
        exoPlayer1.addMediaItem(mediaItem1);
        exoPlayer1.prepare();
        exoPlayer1.play();
    }

    private void loadUrlVideo() {
        StyledPlayerView styledPlayerView = fragmentVideoResultsBinding.videoSwapFaceResults;
        ExoPlayer exoPlayer = new ExoPlayer.Builder(getActivity()).build();
        styledPlayerView.setPlayer(exoPlayer);
        MediaItem mediaItem = MediaItem.fromUri(link_vid_swap);
        exoPlayer.addMediaItem(mediaItem);
        exoPlayer.prepare();
        exoPlayer.play();
    }

    private void initData() {
        getLinkVide();
    }

    private void getLinkVide() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("link_video_swap",0);
        link_vid_swap = sharedPreferences.getString("link_vid_swap","");
        link_vid_goc = sharedPreferences.getString("link_vid_goc","");
    }
}