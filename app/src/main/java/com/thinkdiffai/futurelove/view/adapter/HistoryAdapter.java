package com.thinkdiffai.futurelove.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.daimajia.swipe.SwipeLayout;
import com.thinkdiffai.futurelove.databinding.ItemHistoryBinding;
import com.thinkdiffai.futurelove.model.EventHomeDto;
import com.thinkdiffai.futurelove.util.Util;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    private List<EventHomeDto> eventList;
    public final IOnClickItemListener iOnClickItem;

    public void setData(List<EventHomeDto> eventList) {
        this.eventList = eventList;
    }

    public HistoryAdapter(List<EventHomeDto> eventList, IOnClickItemListener iOnClickItem) {
        this.eventList = eventList;
        this.iOnClickItem = iOnClickItem;
    }

    public interface IOnClickItemListener {

        void onClickItem(int id);
        void onClickDelete(int orderNumber_id);

    }


    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHistoryBinding itemHistoryBinding = ItemHistoryBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new HistoryAdapter.HistoryViewHolder(itemHistoryBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        EventHomeDto events = eventList.get(position);
        if (events==null)
            return;


        Glide.with(holder.itemView.getContext())
                .load(events.getLink_nam_goc())
                .into(holder.itemHistoryBinding.imageAvatar1);

        Glide.with(holder.itemView.getContext())
                .load(events.getLink_nu_goc())
                .into(holder.itemHistoryBinding.imageAvatar2);

        holder.itemHistoryBinding.tvTitle.setText(events.getTen_su_kien());
        if (events.getReal_time() == null) {
            holder.itemHistoryBinding.tvTime.setVisibility(View.INVISIBLE);
        } else {
            holder.itemHistoryBinding.tvTime.setVisibility(View.VISIBLE);
            holder.itemHistoryBinding.tvTime.setText(Util.calTimeStampComment(events.getReal_time()));
        }

        holder.itemHistoryBinding.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iOnClickItem.onClickItem(events.getId_toan_bo_su_kien());
            }
        });


        holder.itemHistoryBinding.layoutItem.setShowMode(SwipeLayout.ShowMode.PullOut);
        holder.itemHistoryBinding.layoutItem.addDrag(SwipeLayout.DragEdge.Right, holder.itemHistoryBinding.layoutAction);
        holder.itemHistoryBinding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iOnClickItem.onClickDelete(events.getNumberOrder());
                holder.itemHistoryBinding.layoutItem.close();
            }
        });
    }

    @Override
    public int getItemCount() {
        return null == eventList?0:eventList.size();
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        private final ItemHistoryBinding itemHistoryBinding;
//        public LinearLayout foreGround;
        public HistoryViewHolder(ItemHistoryBinding itemHistoryBinding) {
            super(itemHistoryBinding.getRoot());
            this.itemHistoryBinding = itemHistoryBinding;
//            foreGround = itemHistoryBinding.layoutForeGround;
        }
    }
}
