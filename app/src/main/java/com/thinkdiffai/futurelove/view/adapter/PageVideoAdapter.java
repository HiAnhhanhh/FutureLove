package com.thinkdiffai.futurelove.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thinkdiffai.futurelove.R;
import com.thinkdiffai.futurelove.databinding.TabItemBinding;

import java.util.ArrayList;

public class PageVideoAdapter extends RecyclerView.Adapter<PageVideoAdapter.ViewHolder> {

    private ArrayList<Integer> pageVideoArrayList;
    private Context context;

    int row_index = -1;


    public onItemClickListener onItemClickListener;

    public PageVideoAdapter(ArrayList<Integer> pageVideoArrayList, Context context, PageVideoAdapter.onItemClickListener onItemClickListener) {
        this.pageVideoArrayList = pageVideoArrayList;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    public interface onItemClickListener {
        void onClick(int position);
    }

    @NonNull
    @Override
    public PageVideoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TabItemBinding tabItemBinding = TabItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(tabItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PageVideoAdapter.ViewHolder holder, int position) {
        if(position == 0){
            holder.itemView.setBackgroundResource(R.color.green_light);
        }else{
            holder.itemView.setBackgroundResource(R.color.white);
        }
        Integer pageEvent = pageVideoArrayList.get(position);
        int position_adapter = position;
        holder.tabItemBinding.pageEvent.setText(pageEvent.toString());

        holder.tabItemBinding.pageEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row_index = position_adapter;
                notifyDataSetChanged();
                onItemClickListener.onClick(position_adapter);
            }
        });

        if(row_index == position_adapter){
            holder.itemView.setBackgroundResource(R.color.green_light);
        }else{
            holder.itemView.setBackgroundResource(R.color.white);
        }
    }

    @Override
    public int getItemCount() {
        return pageVideoArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TabItemBinding tabItemBinding;
        public ViewHolder(TabItemBinding tabItemBinding) {
            super(tabItemBinding.getRoot());
            this.tabItemBinding = tabItemBinding;
        }
    }
}
