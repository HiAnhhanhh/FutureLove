package com.thinkdiffai.futurelove.view.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.session.MediaController;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.thinkdiffai.futurelove.databinding.TabVideoItemBinding;
import com.thinkdiffai.futurelove.model.EventVideoModel;
import com.thinkdiffai.futurelove.model.ListVideoHighlightsModel;


import java.util.List;

public class VideoHighLightsAdapter extends RecyclerView.Adapter<VideoHighLightsAdapter.ViewHolder> {
    Context context;

    private final List<EventVideoModel> videoUrls; // Danh sách đường dẫn video

    public VideoHighLightsAdapter(Context context, List<EventVideoModel> videoUrls) {
        this.context = context;
        this.videoUrls = videoUrls;
    }

    @NonNull
    @Override
    public VideoHighLightsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TabVideoItemBinding tabVideoItemBinding = TabVideoItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(tabVideoItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoHighLightsAdapter.ViewHolder holder, int position) {
        ListVideoHighlightsModel listVideoHighlightsModel = videoUrls.get(position).getListVideoHighlightsModels().get(0);
        holder.binData(listVideoHighlightsModel.link_vid_swap);
    }

    @Override
    public int getItemCount() {
        return videoUrls.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TabVideoItemBinding tabVideoItemBinding;

        public ViewHolder(@NonNull TabVideoItemBinding tabVideoItemBinding) {
            super(tabVideoItemBinding.getRoot());
            this.tabVideoItemBinding = tabVideoItemBinding;
        }

        public void binData(String urlVideo) {
            try {
                tabVideoItemBinding.playerView.setVideoURI(Uri.parse(urlVideo));
                tabVideoItemBinding.playerView.setOnPreparedListener(mp -> {
                    mp.setVolume(0f,0f);
                    mp.setLooping(true);
                });
                tabVideoItemBinding.playerView.setOnInfoListener((mp, what, extra) -> {
                    if (MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START == what) {
                         tabVideoItemBinding.progressBar.setVisibility(View.GONE);
                    }
                    if (MediaPlayer.MEDIA_INFO_BUFFERING_START == what) {
                        tabVideoItemBinding.progressBar.setVisibility(View.VISIBLE);
                    }
                    if (MediaPlayer.MEDIA_INFO_BUFFERING_END == what) {
                        tabVideoItemBinding.progressBar.setVisibility(View.VISIBLE);
                    }
                    return false;
                });
                tabVideoItemBinding.playerView.start();

            }catch(Exception e){
                Log.e("TAG", "Error : " + e.getMessage());
            }
        }
    }
}
