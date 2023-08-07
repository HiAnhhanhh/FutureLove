package com.thinkdiffai.futurelove.view.adapter;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;


import com.thinkdiffai.futurelove.R;
import com.thinkdiffai.futurelove.databinding.ItemIndicatorTimelineBinding;

import java.util.List;

public class PaginationTimelineAdapter extends RecyclerView.Adapter<PaginationTimelineAdapter.PaginationViewHolde> {


    private final List<Integer> integerList;
    public final IOnClickMangaItemListener iOnClickMangaItemListener;
    int indexCurrentPage;
    int currentPosition=0;
    public void setCurrentPosition(int currentPosition){
        this.currentPosition = currentPosition;
    }

    public PaginationTimelineAdapter(List<Integer> integerList, IOnClickMangaItemListener iOnClickMangaItemListener) {
        this.integerList = integerList;
        this.iOnClickMangaItemListener = iOnClickMangaItemListener;
        indexCurrentPage =0;
    }
    public interface IOnClickMangaItemListener {
        void onClickItemPage(Integer page, int indexOlderItem);

    }

    @NonNull
    @Override
    public PaginationViewHolde onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemIndicatorTimelineBinding indicatorTimelineBinding = ItemIndicatorTimelineBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PaginationViewHolde(indicatorTimelineBinding);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")

    @Override
    public void onBindViewHolder(@NonNull PaginationViewHolde holder, @SuppressLint("RecyclerView") int position) {
        int  finalPosition;
        if (currentPosition>=0){
            finalPosition = currentPosition;
            indexCurrentPage = finalPosition;
        }else {
             finalPosition = position;
        }

        if(indexCurrentPage==finalPosition){
            holder.indicatorTimelineBinding.layoutItem.setBackgroundResource(R.drawable.bg_indicator_pagination_60);;
        }else {
            holder.indicatorTimelineBinding.layoutItem.setBackgroundResource(R.drawable.bg_indicator_pagination_40);;
        }
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
//                0,
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                1f
//        );
//        holder.itemView.setLayoutParams(layoutParams);
        holder.indicatorTimelineBinding.tvPage.setText(String.valueOf(convertToRomanNumber(finalPosition+1)));
        holder.indicatorTimelineBinding.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iOnClickMangaItemListener.onClickItemPage( finalPosition+1, indexCurrentPage);
//                v.setBackgroundResource(R.drawable.bg_indicator_pagination_orange);
                indexCurrentPage = finalPosition;
                currentPosition=-1;

            }
        });

        currentPosition=-1;
    }

    @Override
    public int getItemCount() {
        return null == integerList?0:integerList.size();
    }


    public static class PaginationViewHolde extends RecyclerView.ViewHolder{
        private final ItemIndicatorTimelineBinding indicatorTimelineBinding;
        public PaginationViewHolde(ItemIndicatorTimelineBinding indicatorTimelineBinding) {
            super(indicatorTimelineBinding.getRoot());
            this.indicatorTimelineBinding = indicatorTimelineBinding;
        }
    }

    public String convertToRomanNumber(int number) {
        int[] values = { 10,9,8,7,6,5,4,3,2,1};
        String[] symbols = {"X", "IX", "VIII", "VII", "VI", "V", "IV", "III", "II", "I"};
        int remainingNumber = number;
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < values.length; i++) {
            while (remainingNumber >= values[i]) {
                result.append(symbols[i]);
                remainingNumber -= values[i];
            }
        }

        return result.toString();
    }



}
