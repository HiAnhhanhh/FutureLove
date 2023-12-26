package com.thinkdiffai.futurelove.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.browse.MediaBrowser;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ExoPlayer;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;

import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;

import com.thinkdiffai.futurelove.R;
import com.thinkdiffai.futurelove.databinding.ListVideoItemBinding;
import com.thinkdiffai.futurelove.model.ListVideoModel;
import com.thinkdiffai.futurelove.view.fragment.RecyclerViewClickListener;

import java.util.Collections;
import java.util.List;

public class ListVideoAdapter extends RecyclerView.Adapter<ListVideoAdapter.ViewHolder> {


    private List<ListVideoModel> listVideoModelArrayList ;
    public RecyclerViewClickListener onClickListener;


    private Context context;

    public void setData(List<ListVideoModel> data){
        this.listVideoModelArrayList = data;
        Log.d("check_data", "setData: "+ listVideoModelArrayList.size());
        data.clear();
        notifyDataSetChanged();
    }


    public ListVideoAdapter(List<ListVideoModel> listVideoModelArrayList, RecyclerViewClickListener onClickListener, Context context) {
        this.listVideoModelArrayList = listVideoModelArrayList;
        this.onClickListener = onClickListener;
        this.context = context;
    }

    @NonNull
    @Override
    public ListVideoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListVideoItemBinding listVideoItemBinding = ListVideoItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(listVideoItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ListVideoAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ProgressBar progressBar = holder.listVideoItemBinding.progressBar;
        int position_view = position;
        ListVideoModel videoModel = listVideoModelArrayList.get(position_view);
        String urlVideo = videoModel.getLink_video();
        String nameVideo =  videoModel.getNoi_dung();
        int id_video_int = videoModel.getId();
        VideoView videoView = holder.listVideoItemBinding.viewVideo;

//        StyledPlayerView styledPlayerView = holder.itemView.findViewById(R.id.check_video);
//        ExoPlayer exoPlayer = new ExoPlayer.Builder(context).build();
//        styledPlayerView.setPlayer(exoPlayer);
//        MediaItem mediaItem = MediaItem.fromUri(urlVideo);
//        exoPlayer.addMediaItem(mediaItem);
//        exoPlayer.prepare();
//        exoPlayer.play();

        //            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
//            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
//            exoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
//            Uri uriVideo = Uri.parse(urlVideo);
//            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
//            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
//            MediaSource mediaSource = new ExtractorMediaSource(uriVideo, dataSourceFactory, extractorsFactory, null, null);
//            exoPlayerView.setPlayer(exoPlayer);
//            exoPlayer.prepare(mediaSource);
//            exoPlayer.setPlayWhenReady(true);

        try {
//            MediaController mediaController = new MediaController(context);
//            mediaController.setAnchorView(holder.itemView.findViewById(R.id.viewVideo));
//            videoView.setMediaController(mediaController);
            videoView.setVideoURI(Uri.parse(urlVideo));
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setVolume(0f,0f);
                    mp.setLooping(true);
                }
            });
            videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                    if (MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START == what) {
                        progressBar.setVisibility(View.GONE);
                    }
                    if (MediaPlayer.MEDIA_INFO_BUFFERING_START == what) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    if (MediaPlayer.MEDIA_INFO_BUFFERING_END == what) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    return false;
                }
            });
            videoView.start();

        }catch(Exception e){
            Log.e("TAG", "Error : " + e.toString());
        }

        holder.listVideoItemBinding.viewVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.itemView.setVisibility(View.GONE);
                onClickListener.onItemClick(urlVideo,nameVideo,id_video_int);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listVideoModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ListVideoItemBinding listVideoItemBinding;

        public ViewHolder(@NonNull ListVideoItemBinding listVideoItemBinding) {
            super(listVideoItemBinding.getRoot());
            this.listVideoItemBinding = listVideoItemBinding;
        }
    }
}
