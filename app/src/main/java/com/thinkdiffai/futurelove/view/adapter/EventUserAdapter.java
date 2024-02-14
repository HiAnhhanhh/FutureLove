package com.thinkdiffai.futurelove.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.thinkdiffai.futurelove.databinding.ItemRcvEvent1Binding;
import com.thinkdiffai.futurelove.model.EventCreateByUser;

import java.util.ArrayList;

public class EventUserAdapter extends RecyclerView.Adapter<EventUserAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<EventCreateByUser.EventDetailModel> eventDetailModels;

    private final ItemClickListener itemClickListener;


    public EventUserAdapter(Context context, ArrayList<EventCreateByUser.EventDetailModel> eventDetailModels, ItemClickListener itemClickListener) {
        this.context = context;
        this.eventDetailModels = eventDetailModels;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public EventUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRcvEvent1Binding itemRcvEvent1Binding = ItemRcvEvent1Binding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(itemRcvEvent1Binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EventUserAdapter.ViewHolder holder, int position) {
        EventCreateByUser.EventDetailModel eventDetailModel = eventDetailModels.get(position);
        Glide.with(context).load(eventDetailModel.link_da_swap).into(holder.itemRcvEvent1Binding.imgContent);
        Glide.with(context).load(eventDetailModel.link_nam_goc).into(holder.itemRcvEvent1Binding.imgPerson1);
        Glide.with(context).load(eventDetailModel.link_nu_goc).into(holder.itemRcvEvent1Binding.imgPerson2);
        holder.itemRcvEvent1Binding.tvEventDetail.setText(eventDetailModel.noi_dung_su_kien);
        holder.itemRcvEvent1Binding.tvContent.setText(eventDetailModel.ten_su_kien);
        holder.itemRcvEvent1Binding.tvDate.setText(eventDetailModel.real_time);
        holder.itemView.setOnClickListener(v -> {
            itemClickListener.onClick(eventDetailModel.id_toan_bo_su_kien,eventDetailModel.so_thu_tu_su_kien);
        });
    }
    @Override
    public int getItemCount() {
        return eventDetailModels.size() ;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemRcvEvent1Binding itemRcvEvent1Binding;
        public ViewHolder(@NonNull ItemRcvEvent1Binding itemRcvEvent1Binding) {
            super(itemRcvEvent1Binding.getRoot());
            this.itemRcvEvent1Binding = itemRcvEvent1Binding;
        }
    }
    public interface ItemClickListener {
        void onClick(String position, int so_thu_tu_su_kien);
    }

}
