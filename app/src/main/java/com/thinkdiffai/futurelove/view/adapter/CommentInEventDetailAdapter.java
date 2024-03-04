package com.thinkdiffai.futurelove.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thinkdiffai.futurelove.databinding.FragmentCommentEventDetailBinding;
import com.thinkdiffai.futurelove.databinding.ItemCommentBinding;
import com.thinkdiffai.futurelove.model.comment.CommentPage;

import java.util.ArrayList;

public class CommentInEventDetailAdapter extends RecyclerView.Adapter<CommentInEventDetailAdapter.ViewHolder> {

    ArrayList<CommentPage> eachEventCommentsListList;
    Context context;

    OnClickComment onClickComment;


    public CommentInEventDetailAdapter(ArrayList<CommentPage> eachEventCommentsListList, Context context) {
        this.eachEventCommentsListList = eachEventCommentsListList;
        this.context = context;
    }

    public CommentInEventDetailAdapter(ArrayList<CommentPage> eachEventCommentsListList, Context context, OnClickComment onClickComment) {
        this.eachEventCommentsListList = eachEventCommentsListList;
        this.context = context;
        this.onClickComment = onClickComment;
    }

    public void updateData(ArrayList<CommentPage> newData){
        eachEventCommentsListList.clear();
        eachEventCommentsListList.addAll(newData);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommentInEventDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCommentBinding itemCommentBinding = ItemCommentBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(itemCommentBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentInEventDetailAdapter.ViewHolder holder, int position) {
        CommentPage commentPage = eachEventCommentsListList.get(position);
        holder.itemCommentBinding.tvContent.setText(commentPage.getNoiDungCmt());
        holder.itemCommentBinding.tvDeviceName.setText(commentPage.getUserName());
        holder.itemCommentBinding.tvTime.setText(commentPage.getThoiGianRelease());
        holder.itemCommentBinding.imageComment.setVisibility(View.GONE);
        int id_comment = commentPage.getIdComment();
        holder.itemView.setOnClickListener( v -> {
            onClickComment.onClick(id_comment, position);
        });
    }

    public interface OnClickComment {
        void onClick(int id_comment,int position);
    }

    @Override
    public int getItemCount() {
        return eachEventCommentsListList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemCommentBinding itemCommentBinding;
        public ViewHolder(@NonNull ItemCommentBinding itemCommentBinding) {
            super(itemCommentBinding.getRoot());
            this.itemCommentBinding = itemCommentBinding;
        }
    }
}
