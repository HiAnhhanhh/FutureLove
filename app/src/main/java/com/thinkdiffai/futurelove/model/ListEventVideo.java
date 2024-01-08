package com.thinkdiffai.futurelove.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListEventVideo {
    @SerializedName("list_sukien_video")
    private List<EventVideoModel> eventVideoModelList;

    public ListEventVideo(List<EventVideoModel> eventVideoModelList) {
        this.eventVideoModelList = eventVideoModelList;
    }

    public List<EventVideoModel> getEventVideoModelList() {
        return eventVideoModelList;
    }

    public void setEventVideoModelList(List<EventVideoModel> eventVideoModelList) {
        this.eventVideoModelList = eventVideoModelList;
    }
}
