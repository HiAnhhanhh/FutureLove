package com.thinkdiffai.futurelove.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thinkdiffai.futurelove.databinding.ItemRcvEvent3Binding;
import com.thinkdiffai.futurelove.model.comment.EventsUser.SukienX;

import java.util.List;


public class EventFragmentEventAdapter extends RecyclerView.Adapter<EventFragmentEventAdapter.EventFragmentViewHolder>{
    private List<SukienX> list;
    Context context;

    public EventFragmentEventAdapter(List<SukienX> list, Context context) {
        this.list = list;
        this.context = context;
    }
    public void SetData(List<SukienX> x){
            this.list=x;
    }
    @NonNull
    @Override
    public EventFragmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRcvEvent3Binding itemRcvEvent3Binding=ItemRcvEvent3Binding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new EventFragmentViewHolder(itemRcvEvent3Binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EventFragmentViewHolder holder, int position) {
        SukienX a=list.get(position);
        holder.itemBinding.tvEventTitle.setText(a.getTen_su_kien());
        holder.itemBinding.tvEventTitle.setText(a.getNoi_dung_su_kien());
        holder.itemBinding.tvCommentNumber.setText(""+a.getCount_comment());
        holder.itemBinding.tvViewNumber.setText(""+a.getCount_view());
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }

    public static class EventFragmentViewHolder extends RecyclerView.ViewHolder {
        private  final ItemRcvEvent3Binding itemBinding;
        public EventFragmentViewHolder(ItemRcvEvent3Binding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }
    }
}
