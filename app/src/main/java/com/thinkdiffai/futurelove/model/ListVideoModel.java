package com.thinkdiffai.futurelove.model;

public class ListVideoModel {
    int id, id_categories, age_video;
    String name_categories, detail, chung_toc, gioi_tinh,link_video,mau_da,noi_dung,thumbnail;

    public ListVideoModel(int id, int id_categories, int age_video, String name_categories, String detail, String chung_toc, String gioi_tinh, String link_video, String mau_da, String noi_dung, String thumbnail) {
        this.id = id;
        this.id_categories = id_categories;
        this.age_video = age_video;
        this.name_categories = name_categories;
        this.detail = detail;
        this.chung_toc = chung_toc;
        this.gioi_tinh = gioi_tinh;
        this.link_video = link_video;
        this.mau_da = mau_da;
        this.noi_dung = noi_dung;
        this.thumbnail = thumbnail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_categories() {
        return id_categories;
    }

    public void setId_categories(int id_categories) {
        this.id_categories = id_categories;
    }

    public int getAge_video() {
        return age_video;
    }

    public void setAge_video(int age_video) {
        this.age_video = age_video;
    }

    public String getName_categories() {
        return name_categories;
    }

    public void setName_categories(String name_categories) {
        this.name_categories = name_categories;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getChung_toc() {
        return chung_toc;
    }

    public void setChung_toc(String chung_toc) {
        this.chung_toc = chung_toc;
    }

    public String getGioi_tinh() {
        return gioi_tinh;
    }

    public void setGioi_tinh(String gioi_tinh) {
        this.gioi_tinh = gioi_tinh;
    }

    public String getLink_video() {
        return link_video;
    }

    public void setLink_video(String link_video) {
        this.link_video = link_video;
    }

    public String getMau_da() {
        return mau_da;
    }

    public void setMau_da(String mau_da) {
        this.mau_da = mau_da;
    }

    public String getNoi_dung() {
        return noi_dung;
    }

    public void setNoi_dung(String noi_dung) {
        this.noi_dung = noi_dung;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
