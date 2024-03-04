package com.thinkdiffai.futurelove.view.fragment;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.thinkdiffai.futurelove.R;
import com.thinkdiffai.futurelove.databinding.FragmentVideoResultsBinding;
import com.thinkdiffai.futurelove.view.Downloader;

public class VideoResultsFragment extends Fragment {

    private FragmentVideoResultsBinding fragmentVideoResultsBinding;

    OnBackPressedCallback callback;
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
            fragmentVideoResultsBinding.textView6.setVisibility(View.GONE);
            fragmentVideoResultsBinding.downloadVideoBtn.setVisibility(View.GONE);
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
            fragmentVideoResultsBinding.textView6.setVisibility(View.VISIBLE);
            fragmentVideoResultsBinding.downloadVideoBtn.setVisibility(View.VISIBLE);
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

        fragmentVideoResultsBinding.videoSwapFace.setOnClickListener(v -> {
            VideoResultsFragmentDirections.ActionVideoResultsFragmentToVideoFullScreenFragment action_video_goc = VideoResultsFragmentDirections.actionVideoResultsFragmentToVideoFullScreenFragment(link_vid_goc);
            NavHostFragment.findNavController(this).navigate(action_video_goc);
        });

        fragmentVideoResultsBinding.videoSwapFaceResults.setOnClickListener(v -> {
            VideoResultsFragmentDirections.ActionVideoResultsFragmentToVideoFullScreenFragment action_video_swap = VideoResultsFragmentDirections.actionVideoResultsFragmentToVideoFullScreenFragment(link_vid_swap);
            NavHostFragment.findNavController(this).navigate(action_video_swap);
        });

        fragmentVideoResultsBinding.downloadVideoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("check_download", "onClick: "+ link_vid_swap);
                Downloader.downloadVideo(requireActivity(),link_vid_swap,"video_swap_future_love");
                Toast.makeText(requireContext(), "Downloaded successfully", Toast.LENGTH_SHORT).show();
            }
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