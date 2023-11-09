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
import com.thinkdiffai.futurelove.model.Page;

import java.util.ArrayList;
import java.util.List;

public class PageEventAdapter extends RecyclerView.Adapter<PageEventAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Integer> pageEventList;
    public final OnClickListener onClickListener;

    int row_index = -1;

    boolean check = false;
    public PageEventAdapter(Context context, ArrayList<Integer> pageEventList, OnClickListener onClickListener) {
        this.context = context;
        this.pageEventList = pageEventList;
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener{
        void onClickItem(int position);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TabItemBinding tabItemBinding = TabItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(tabItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PageEventAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Integer pageEvent = pageEventList.get(position);
        holder.tabItemBinding.pageEvent.setText(pageEvent.toString());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row_index = position;
                notifyDataSetChanged();
                onClickListener.onClickItem(position);
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
        return pageEventList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TabItemBinding tabItemBinding;
        public ViewHolder(TabItemBinding tabItemBinding) {
            super(tabItemBinding.getRoot());
            this.tabItemBinding = tabItemBinding;
        }
    }
}
