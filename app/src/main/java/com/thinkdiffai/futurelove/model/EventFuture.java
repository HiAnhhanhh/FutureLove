package com.thinkdiffai.futurelove.model;

import com.google.gson.annotations.SerializedName;

public class EventFuture {
    @SerializedName("Link_img")
    private  String linkImage;

    @SerializedName("image couple")
    public String imageCouple;
    @SerializedName("image husband")
    private  String imageHusband;

    @SerializedName("image wife")
    private  String imageWife;

    @SerializedName("tensukien")
    private  String tensukien;

    @SerializedName("thongtin")
    private  String thongtin;

    public EventFuture() {
    }

    public EventFuture(String linkImage, String imageCouple, String imageHusband, String imageWife, String tensukien, String thongtin) {
        this.linkImage = linkImage;
        this.imageCouple = imageCouple;
        this.imageHusband = imageHusband;
        this.imageWife = imageWife;
        this.tensukien = tensukien;
        this.thongtin = thongtin;
    }

    public String getImageCouple() {
        return imageCouple;
    }

    public void setImageCouple(String imageCouple) {
        this.imageCouple = imageCouple;
    }

    public String getImageHusband() {
        return imageHusband;
    }

    public void setImageHusband(String imageHusband) {
        this.imageHusband = imageHusband;
    }

    public String getImageWife() {
        return imageWife;
    }

    public void setImageWife(String imageWife) {
        this.imageWife = imageWife;
    }

    public String getTensukien() {
        return tensukien;
    }

    public void setTensukien(String tensukien) {
        this.tensukien = tensukien;
    }

    public String getThongtin() {
        return thongtin;
    }

    public void setThongtin(String thongtin) {
        this.thongtin = thongtin;
    }

    public EventFuture(String linkImage, String imageHusband, String imageWife, String tensukien, String thongtin) {
        this.linkImage = linkImage;
        this.imageHusband = imageHusband;
        this.imageWife = imageWife;
        this.tensukien = tensukien;
        this.thongtin = thongtin;
    }

    public String getLinkImage() {
        return linkImage;
    }

    public void setLinkImage(String linkImage) {
        this.linkImage = linkImage;
    }
}
