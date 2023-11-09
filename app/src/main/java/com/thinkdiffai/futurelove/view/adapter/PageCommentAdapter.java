package com.thinkdiffai.futurelove.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thinkdiffai.futurelove.R;
import com.thinkdiffai.futurelove.databinding.TabItemBinding;

import java.util.ArrayList;

public class PageCommentAdapter extends RecyclerView.Adapter<PageCommentAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Integer> pageCommentList;
    public final OnClickListener onClickListener;

    private int row_index  = -1;

    public PageCommentAdapter(Context context, ArrayList<Integer> pageCommentList, OnClickListener onClickListener) {
        this.context = context;
        this.pageCommentList = pageCommentList;
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener{
        void onClickItem(int position);
    }

    @NonNull
    @Override
    public PageCommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TabItemBinding tabItemBinding = TabItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(tabItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PageCommentAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Integer pageEvent = pageCommentList.get(position);
        holder.tabItemBinding.pageEvent.setText(pageEvent.toString());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                row_index = position;
                notifyDataSetChanged();
                onClickListener.onClickItem(position);
//                holder.itemView.setBackgroundColor(R.drawable.color_bg_rcv);
            }
        });
        if(row_index == position){
            holder.itemView.setBackgroundResource(R.color.green_light);
        }else{
            holder.itemView.setBackgroundResource(R.color.white);
        }
    }

    @Override
    public int getItemCount() {
        return pageCommentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TabItemBinding tabItemBinding;
        public ViewHolder(TabItemBinding tabItemBinding) {
            super(tabItemBinding.getRoot());
            this.tabItemBinding = tabItemBinding;
        }
    }
}
