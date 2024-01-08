package com.thinkdiffai.futurelove.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListImageUploadModel {
    @SerializedName("image_links_nam")
    private List<String> image_links_anh;

    public ListImageUploadModel(List<String> image_links_anh) {
        this.image_links_anh = image_links_anh;
    }

    public List<String> getImage_links_anh() {
        return image_links_anh;
    }

    public void setImage_links_anh(List<String> image_links_anh) {
        this.image_links_anh = image_links_anh;
    }
}
