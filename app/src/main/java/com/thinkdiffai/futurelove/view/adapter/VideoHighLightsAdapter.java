package com.thinkdiffai.futurelove.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
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
        TabVideoItemBinding tabVideoItemBinding;

        PlayerView playerView;
        SimpleExoPlayer player;

        public ViewHolder(@NonNull TabVideoItemBinding tabVideoItemBinding) {
            super(tabVideoItemBinding.getRoot());
            this.tabVideoItemBinding = tabVideoItemBinding;
            playerView = tabVideoItemBinding.playerView1;
            playerView.setUseController(false);
            playerView.hideController();
            initializePlayer();

        }

        private void initializePlayer() {
            player = new SimpleExoPlayer.Builder(itemView.getContext()).build();
            playerView.setPlayer(player);
        }

        public void binData(String urlVideo) {
            MediaItem mediaItem = MediaItem.fromUri(urlVideo);
            player.setMediaItem(mediaItem);
            player.setVolume(0f);
            player.setPlayWhenReady(true);
            player.prepare();
            player.play();
        }
    }
}
