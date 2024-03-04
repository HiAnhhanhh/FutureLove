package com.thinkdiffai.futurelove.view.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thinkdiffai.futurelove.databinding.ListVideoItemBinding;
import com.thinkdiffai.futurelove.model.EventVideoModel;
import com.thinkdiffai.futurelove.model.ListVideoHighlightsModel;
import com.thinkdiffai.futurelove.view.fragment.RecyclerViewClickListener;

import java.util.List;

public class ShowMoreHighlightAdapter  extends RecyclerView.Adapter<ShowMoreHighlightAdapter.ViewHolder> {

    Context context;
    private final List<EventVideoModel> videoUrls;
    public RecyclerViewClickListener onClickListener;

    public ShowMoreHighlightAdapter(Context context, List<EventVideoModel> videoUrls, RecyclerViewClickListener onClickListener) {
        this.context = context;
        this.videoUrls = videoUrls;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ShowMoreHighlightAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListVideoItemBinding listVideoItemBinding = ListVideoItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(listVideoItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowMoreHighlightAdapter.ViewHolder holder, int position) {
        ProgressBar progressBar = holder.listVideoItemBinding.progressBar;
        int position_view = position;
        ListVideoHighlightsModel listVideoHighlightsModel = videoUrls.get(position_view).getListVideoHighlightsModels().get(0);
        String urlVideo = listVideoHighlightsModel.link_vid_swap;
        int id_video_int = listVideoHighlightsModel.count_comment;
//
        Log.d("check_url_video", "onBindViewHolder: "+ urlVideo);
        VideoView videoView = holder.listVideoItemBinding.viewVideo;

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
                onClickListener.onItemClick(urlVideo,1);
            }
        });

    }

    @Override
    public int getItemCount() {
        return videoUrls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ListVideoItemBinding listVideoItemBinding;
        public ViewHolder(@NonNull ListVideoItemBinding listVideoItemBinding) {
            super(listVideoItemBinding.getRoot());
            this.listVideoItemBinding = listVideoItemBinding;
        }
    }
}
