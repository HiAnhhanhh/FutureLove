package com.thinkdiffai.futurelove.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EventVideoModel {
    @SerializedName("sukien_video")
    private List<ListVideoHighlightsModel> listVideoHighlightsModels;

    public EventVideoModel(List<ListVideoHighlightsModel> listVideoHighlightsModels) {
        this.listVideoHighlightsModels = listVideoHighlightsModels;
    }

    public List<ListVideoHighlightsModel> getListVideoHighlightsModels() {
        return listVideoHighlightsModels;
    }

    public void setListVideoHighlightsModels(List<ListVideoHighlightsModel> listVideoHighlightsModels) {
        this.listVideoHighlightsModels = listVideoHighlightsModels;
    }
}
