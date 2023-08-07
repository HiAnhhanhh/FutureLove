package com.thinkdiffai.futurelove.util;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class PaginationScrollListener extends RecyclerView.OnScrollListener {
    private LinearLayoutManager linearLayoutManager;

    public PaginationScrollListener(LinearLayoutManager linearLayoutManager) {
        this.linearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int visibleItemCount = linearLayoutManager.getChildCount();
        int totalItemCount = linearLayoutManager.getItemCount();
        int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

        if (isLoading()||isLagePage()){
            return;
        }
        if(firstVisibleItemPosition>=0&&(visibleItemCount+firstVisibleItemPosition)>=totalItemCount){
            loadMoreItem();
        }
        if (firstVisibleItemPosition==0&&linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0&&dy<0){
            ReloadItem();
        }



    }

    public abstract void loadMoreItem();
    public abstract boolean isLoading();
    public abstract boolean isLagePage();
    public abstract void ReloadItem();




}
