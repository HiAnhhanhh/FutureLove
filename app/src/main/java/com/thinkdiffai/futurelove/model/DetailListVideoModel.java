package com.thinkdiffai.futurelove.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DetailListVideoModel {
    @SerializedName("list_sukien_video")
    private List<ListVideoModel> listSukienVideo;

    public DetailListVideoModel(List<ListVideoModel> listSukienVideo) {
        this.listSukienVideo = listSukienVideo;
    }

    public List<ListVideoModel> getListSukienVideo() {
        return listSukienVideo;
    }

    public void setListSukienVideo(List<ListVideoModel> listSukienVideo) {
        this.listSukienVideo = listSukienVideo;
    }
}
