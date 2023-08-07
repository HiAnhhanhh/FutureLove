package com.thinkdiffai.futurelove.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thinkdiffai.futurelove.databinding.ItemTimelineEventBinding;
import com.thinkdiffai.futurelove.model.DetailEvent;
import com.thinkdiffai.futurelove.model.DetailEventList;
import com.thinkdiffai.futurelove.model.EventHomeDto;
import com.squareup.picasso.Picasso;

import java.util.List;

public class EventTimelineAdapter extends RecyclerView.Adapter<EventTimelineAdapter.EventTimelineViewHolder> {
    private List<DetailEvent> detailEventLists;
    Context context;

    public void setData(List<DetailEvent> detailEventLists) {
        this.detailEventLists = detailEventLists;
    }

    public EventTimelineAdapter(List<DetailEvent> eventList, IOnClickAddEventListener iOnClickAddEvent, IOnScrollEventList iOnScrollEventList, Context context) {
        this.detailEventLists = eventList;
        this.iOnClickAddEvent = iOnClickAddEvent;
        this.iOnScrollEventList = iOnScrollEventList;
        this.context = context;
    }

    public final IOnClickAddEventListener iOnClickAddEvent;
    public final IOnScrollEventList iOnScrollEventList;
    public interface IOnClickAddEventListener {
        void onClickAddEvent(int id_event);
    }

    // Interface for callback soThuTuSuKien where it scrolls to
    public interface IOnScrollEventList {
        void onScrollPosition(int soThuTuSuKien);
    }

    @NonNull
    @Override
    public EventTimelineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTimelineEventBinding itemTimelineEventBinding = ItemTimelineEventBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new EventTimelineAdapter.EventTimelineViewHolder(itemTimelineEventBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull EventTimelineViewHolder holder, int position) {
        DetailEvent event = detailEventLists.get(position);
        if (event == null)
            return;

        Picasso.get().load(event.getLinkDaSwap()).into(holder.itemTimelineEventBinding.imgContent);

        int commaIndex = event.getRealTime().indexOf(",");
        String date = event.getRealTime().substring(0, commaIndex);
        holder.itemTimelineEventBinding.tvContent.setText(event.getNoiDungSuKien());
        holder.itemTimelineEventBinding.tvDate.setText(date);
        // Toast.makeText(context,  event.getId()+"", Toast.LENGTH_SHORT).show();
        holder.itemTimelineEventBinding.btnAddEvnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iOnClickAddEvent.onClickAddEvent(event.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return null == detailEventLists ? 0 : detailEventLists.size();
    }

    public static class EventTimelineViewHolder extends RecyclerView.ViewHolder {
        private final ItemTimelineEventBinding itemTimelineEventBinding;

        public EventTimelineViewHolder(ItemTimelineEventBinding itemTimelineEventBinding) {
            super(itemTimelineEventBinding.getRoot());
            this.itemTimelineEventBinding = itemTimelineEventBinding;
        }
    }
}
