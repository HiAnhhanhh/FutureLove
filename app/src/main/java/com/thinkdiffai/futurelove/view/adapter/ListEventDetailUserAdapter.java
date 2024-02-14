package com.thinkdiffai.futurelove.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.thinkdiffai.futurelove.databinding.ItemDetailEventBinding;
import com.thinkdiffai.futurelove.model.ListEventDetailModel;

import java.util.ArrayList;

public class ListEventDetailUserAdapter extends RecyclerView.Adapter<ListEventDetailUserAdapter.ViewHolder> {
    private final ArrayList<ListEventDetailModel.EventDetailModel> listEventDetail;
    private final Context context;
    RecyclerViewClickListener recyclerViewClickListener;

    public ListEventDetailUserAdapter(ArrayList<ListEventDetailModel.EventDetailModel> listEventDetail, Context context, RecyclerViewClickListener recyclerViewClickListener) {
        this.listEventDetail = listEventDetail;
        this.context = context;
        this.recyclerViewClickListener = recyclerViewClickListener;
    }
    @NonNull
    @Override
    public ListEventDetailUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDetailEventBinding itemDetailEventBinding = ItemDetailEventBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(itemDetailEventBinding);
    }
    @Override
    public void onBindViewHolder(@NonNull ListEventDetailUserAdapter.ViewHolder holder, int position) {
        ListEventDetailModel.EventDetailModel eventDetailModel = listEventDetail.get(position);
        holder.itemDetailEventBinding.nameUser.setText(eventDetailModel.user_name_tao_sk);
        holder.itemDetailEventBinding.content.setText(eventDetailModel.noi_dung_su_kien);
        holder.itemDetailEventBinding.dateOfTime.setText(eventDetailModel.real_time);
        Glide.with(context).load(eventDetailModel.link_da_swap).into(holder.itemDetailEventBinding.imageSwap);
        holder.itemView.setOnClickListener(v -> {
            recyclerViewClickListener.onItemClickImage(eventDetailModel.id ,String.valueOf(eventDetailModel.so_thu_tu_su_kien));
        });
    }
    @Override
    public int getItemCount() {
        Log.d("check_event_detail", "getItemCount: "+ listEventDetail.size());
        return listEventDetail.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemDetailEventBinding itemDetailEventBinding;
        public ViewHolder(@NonNull ItemDetailEventBinding itemDetailEventBinding) {
            super(itemDetailEventBinding.getRoot());
             this.itemDetailEventBinding= itemDetailEventBinding;
        }
    }
    public interface RecyclerViewClickListener {
        void onItemClickImage(String id_toan_bo_su_kien,String so_thu_tu_su_kien);
    }
}
