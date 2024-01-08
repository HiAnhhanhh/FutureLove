package com.thinkdiffai.futurelove.model;

import com.google.gson.annotations.SerializedName;

public class ListVideoHighlightsModel {
    @SerializedName("id_video")
    public String id_sukien_video;

    @SerializedName("link_image")
    public String linkimg;

    @SerializedName("link_vid_swap")
    public String link_vid_swap;

    @SerializedName("link_video_goc")
    public String link_vid_goc;

    @SerializedName("thoigian_swap")
    public Double thoi_gian_swap;

    @SerializedName("ten_su_kien")
    public String ten_su_kien;

    @SerializedName("noidung_sukien")
    public String noidung;

    @SerializedName("id_video_swap")
    public String thoigian_sukien;

    @SerializedName("thoigian_taosk")
    public String thoigian_taosk;

    @SerializedName("id_user")
    public int device_tao_vid;

    @SerializedName("count_comment")
    public int count_comment;

    @SerializedName("count_view")
    public int count_view;

    public ListVideoHighlightsModel(String id_sukien_video, String linkimg, String link_vid_swap, String link_vid_goc, Double thoi_gian_swap, String ten_su_kien, String noidung, String thoigian_sukien, String thoigian_taosk, int device_tao_vid, int count_comment, int count_view) {
        this.id_sukien_video = id_sukien_video;
        this.linkimg = linkimg;
        this.link_vid_swap = link_vid_swap;
        this.link_vid_goc = link_vid_goc;
        this.thoi_gian_swap = thoi_gian_swap;
        this.ten_su_kien = ten_su_kien;
        this.noidung = noidung;
        this.thoigian_sukien = thoigian_sukien;
        this.thoigian_taosk = thoigian_taosk;
        this.device_tao_vid = device_tao_vid;
        this.count_comment = count_comment;
        this.count_view = count_view;
    }

    public String getId_sukien_video() {
        return id_sukien_video;
    }

    public void setId_sukien_video(String id_sukien_video) {
        this.id_sukien_video = id_sukien_video;
    }

    public String getLinkimg() {
        return linkimg;
    }

    public void setLinkimg(String linkimg) {
        this.linkimg = linkimg;
    }

    public String getLink_vid_swap() {
        return link_vid_swap;
    }

    public void setLink_vid_swap(String link_vid_swap) {
        this.link_vid_swap = link_vid_swap;
    }

    public String getLink_vid_goc() {
        return link_vid_goc;
    }

    public void setLink_vid_goc(String link_vid_goc) {
        this.link_vid_goc = link_vid_goc;
    }

    public Double getThoi_gian_swap() {
        return thoi_gian_swap;
    }

    public void setThoi_gian_swap(Double thoi_gian_swap) {
        this.thoi_gian_swap = thoi_gian_swap;
    }

    public String getTen_su_kien() {
        return ten_su_kien;
    }

    public void setTen_su_kien(String ten_su_kien) {
        this.ten_su_kien = ten_su_kien;
    }

    public String getNoidung() {
        return noidung;
    }

    public void setNoidung(String noidung) {
        this.noidung = noidung;
    }

    public String getThoigian_sukien() {
        return thoigian_sukien;
    }

    public void setThoigian_sukien(String thoigian_sukien) {
        this.thoigian_sukien = thoigian_sukien;
    }

    public String getThoigian_taosk() {
        return thoigian_taosk;
    }

    public void setThoigian_taosk(String thoigian_taosk) {
        this.thoigian_taosk = thoigian_taosk;
    }

    public int getDevice_tao_vid() {
        return device_tao_vid;
    }

    public void setDevice_tao_vid(int device_tao_vid) {
        this.device_tao_vid = device_tao_vid;
    }

    public int getCount_comment() {
        return count_comment;
    }

    public void setCount_comment(int count_comment) {
        this.count_comment = count_comment;
    }

    public int getCount_view() {
        return count_view;
    }

    public void setCount_view(int count_view) {
        this.count_view = count_view;
    }
}
