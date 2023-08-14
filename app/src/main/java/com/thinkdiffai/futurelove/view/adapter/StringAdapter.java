package com.thinkdiffai.futurelove.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.thinkdiffai.futurelove.R;

import java.util.List;

public class StringAdapter extends RecyclerView.Adapter<StringAdapter.StringViewHolder> {
    private List<String> stringList;

    public StringAdapter(List<String> stringList) {
        this.stringList = stringList;
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
