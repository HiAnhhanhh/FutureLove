package com.thinkdiffai.futurelove.view.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.thinkdiffai.futurelove.databinding.TabImageItemBinding;

import java.util.ArrayList;
import java.util.List;

public class ImageUploadAdapter extends RecyclerView.Adapter<ImageUploadAdapter.ViewHolder> {

    private ArrayList<String> listImageUpload;
    private Context context;
    private RecyclerViewClickListener recyclerViewClickListener;

    public ImageUploadAdapter(ArrayList<String> listImageUpload, Context context, RecyclerViewClickListener recyclerViewClickListener) {
        this.listImageUpload = listImageUpload;
        this.context = context;
        this.recyclerViewClickListener = recyclerViewClickListener;
    }

    @NonNull
    @Override
    public ImageUploadAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TabImageItemBinding tabImageItemBinding = TabImageItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(tabImageItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageUploadAdapter.ViewHolder holder, int position) {
        String imageUrl = listImageUpload.get(position);
        Glide.with(context)
                .load(imageUrl)
                .into(holder.tabImageItemBinding.shapeImageView);
        holder.tabImageItemBinding.shapeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewClickListener.onItemClickImage(imageUrl);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listImageUpload.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TabImageItemBinding tabImageItemBinding;
        public ViewHolder(@NonNull TabImageItemBinding tabImageItemBinding) {
            super(tabImageItemBinding.getRoot());
            this.tabImageItemBinding = tabImageItemBinding;
        }
    }

    public interface RecyclerViewClickListener {
        void onItemClickImage(String link_img);
    }
}
