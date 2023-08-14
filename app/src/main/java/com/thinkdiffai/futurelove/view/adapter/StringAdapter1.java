package com.thinkdiffai.futurelove.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.thinkdiffai.futurelove.R;

import java.util.List;

public class StringAdapter1 extends RecyclerView.Adapter<StringAdapter1.StringViewHolder> {
    private List<String> stringList;
    private OnItemClickListener listener;

    public StringAdapter1(List<String> stringList, OnItemClickListener listener) {
        this.stringList = stringList;
        this.listener = listener;
    }

    @Override
    public StringViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_string_events, parent, false);
        return new StringViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StringViewHolder holder, int position) {
        String item = stringList.get(position);
        holder.textView.setText(item);
        holder.textView.setTextSize(16);
        holder.layoutCompat.setRight(20);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
    public interface OnItemClickListener {
        void onItemClick(String item);
    }
    @Override
    public int getItemCount() {
        return stringList.size();
    }

    public class StringViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        LinearLayoutCompat layoutCompat;
        public StringViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.String_name_event);
            layoutCompat=itemView.findViewById(R.id.ll_item_string);
        }
    }
}
