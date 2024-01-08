package com.thinkdiffai.futurelove.view.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentVideoResultsBinding = FragmentVideoResultsBinding.inflate(inflater, container, false);
        return fragmentVideoResultsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initView();
        initAction();
    }

    private void initData() {
        getLinkVideo();
    }

    private void initAction() {
        StyledPlayerView styledPlayerView1 = fragmentVideoResultsBinding.videoSwapFaceResults;
        ExoPlayer exoPlayer1 = new ExoPlayer.Builder(requireActivity()).build();
        styledPlayerView1.setPlayer(exoPlayer1);
        StyledPlayerView styledPlayerView = fragmentVideoResultsBinding.videoSwapFace;
        ExoPlayer exoPlayer = new ExoPlayer.Builder(requireActivity()).build();
        styledPlayerView.setPlayer(exoPlayer);
        MediaItem mediaItem = MediaItem.fromUri(link_vid_goc);
        exoPlayer.addMediaItem(mediaItem);

        exoPlayer.prepare();
        exoPlayer.play();

        fragmentVideoResultsBinding.beforeBtn.setOnClickListener(v -> {
            exoPlayer1.stop();
            exoPlayer.seekTo(0);
            fragmentVideoResultsBinding.beforeBtn.setBackgroundResource(R.drawable.bg_purple_transparent);
            fragmentVideoResultsBinding.afterBtn.setBackgroundResource(R.color.transparent);
            fragmentVideoResultsBinding.afterBtn.setTextSize(16);
            fragmentVideoResultsBinding.beforeBtn.setTextSize(20);
            fragmentVideoResultsBinding.videoSwapFace.setVisibility(View.VISIBLE);
            fragmentVideoResultsBinding.videoSwapFaceResults.setVisibility(View.GONE);
            MediaItem mediaItem12 = MediaItem.fromUri(link_vid_goc);
            exoPlayer.addMediaItem(mediaItem12);
            exoPlayer.prepare();
            exoPlayer.play();
        });
        fragmentVideoResultsBinding.afterBtn.setOnClickListener(v -> {
            exoPlayer.stop();
            exoPlayer1.seekTo(0);
            fragmentVideoResultsBinding.afterBtn.setBackgroundResource(R.drawable.bg_purple_transparent);
            fragmentVideoResultsBinding.beforeBtn.setBackgroundResource(R.color.transparent);
            fragmentVideoResultsBinding.beforeBtn.setTextSize(16);
            fragmentVideoResultsBinding.afterBtn.setTextSize(20);
            fragmentVideoResultsBinding.videoSwapFace.setVisibility(View.GONE);
            fragmentVideoResultsBinding.videoSwapFaceResults.setVisibility(View.VISIBLE);
            MediaItem mediaItem1 = MediaItem.fromUri(link_vid_swap);
            exoPlayer1.addMediaItem(mediaItem1);
            exoPlayer1.prepare();
            exoPlayer1.play();
        });

        fragmentVideoResultsBinding.backBtn.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });
    }

    private void initView() {
    }

    private void getLinkVideo() {
        Bundle args = getArguments();
        try {
            if(args!=null) {
                link_vid_goc = args.getString("link_video_goc");
                link_vid_swap = args.getString("link_video_swap");
            }
        } catch (Exception e){
            Toast.makeText(getActivity(), ""+ e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}