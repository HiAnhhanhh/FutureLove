package com.thinkdiffai.futurelove.model;

import com.google.gson.annotations.SerializedName;

public class GenImageModel {

    @SerializedName("sukien_2_image")
    private SuKienImageModel suKienImageModel;

    public GenImageModel(SuKienImageModel suKienImageModel) {
        this.suKienImageModel = suKienImageModel;
    }

    public SuKienImageModel getSuKienImageModel() {
        return suKienImageModel;
    }

    public void setSuKienImageModel(SuKienImageModel suKienImageModel) {
        this.suKienImageModel = suKienImageModel;
    }

    public static class SuKienImageModel{
        @SerializedName("id_saved")
        private String id_saved;

        @SerializedName("link_src_goc")
        private String link_src_goc;


        @SerializedName("link_tar_goc")
        private String link_tar_goc;


        @SerializedName("link_da_swap")
        private String link_da_swap;


        @SerializedName("id_toan_bo_su_kien")
        private String id_toan_bo_su_kien;


        @SerializedName("thoigian_sukien")
        private String thoigian_sukien;


        @SerializedName("device_them_su_kien")
        private String device_them_su_kien;

        @SerializedName("ip_them_su_kien")
        private String ip_them_su_kien;

        @SerializedName("id_user")
        private int id_user;

        @SerializedName("count_comment")
        private int count_comment;

        @SerializedName("count_view")
        private int count_view;

        @SerializedName("id_template")
        private int id_template;

        public SuKienImageModel(String id_saved, String link_src_goc, String link_tar_goc, String link_da_swap, String id_toan_bo_su_kien, String thoigian_sukien, String device_them_su_kien, String ip_them_su_kien, int id_user, int count_comment, int count_view, int id_template) {
            this.id_saved = id_saved;
            this.link_src_goc = link_src_goc;
            this.link_tar_goc = link_tar_goc;
            this.link_da_swap = link_da_swap;
            this.id_toan_bo_su_kien = id_toan_bo_su_kien;
            this.thoigian_sukien = thoigian_sukien;
            this.device_them_su_kien = device_them_su_kien;
            this.ip_them_su_kien = ip_them_su_kien;
            this.id_user = id_user;
            this.count_comment = count_comment;
            this.count_view = count_view;
            this.id_template = id_template;
        }

        public String getId_saved() {
            return id_saved;
        }

        public void setId_saved(String id_saved) {
            this.id_saved = id_saved;
        }

        public String getLink_src_goc() {
            return link_src_goc;
        }

        public void setLink_src_goc(String link_src_goc) {
            this.link_src_goc = link_src_goc;
        }

        public String getLink_tar_goc() {
            return link_tar_goc;
        }

        public void setLink_tar_goc(String link_tar_goc) {
            this.link_tar_goc = link_tar_goc;
        }

        public String getLink_da_swap() {
            return link_da_swap;
        }

        public void setLink_da_swap(String link_da_swap) {
            this.link_da_swap = link_da_swap;
        }

        public String getId_toan_bo_su_kien() {
            return id_toan_bo_su_kien;
        }

        public void setId_toan_bo_su_kien(String id_toan_bo_su_kien) {
            this.id_toan_bo_su_kien = id_toan_bo_su_kien;
        }

        public String getThoigian_sukien() {
            return thoigian_sukien;
        }

        public void setThoigian_sukien(String thoigian_sukien) {
            this.thoigian_sukien = thoigian_sukien;
        }

        public String getDevice_them_su_kien() {
            return device_them_su_kien;
        }

        public void setDevice_them_su_kien(String device_them_su_kien) {
            this.device_them_su_kien = device_them_su_kien;
        }

        public String getIp_them_su_kien() {
            return ip_them_su_kien;
        }

        public void setIp_them_su_kien(String ip_them_su_kien) {
            this.ip_them_su_kien = ip_them_su_kien;
        }

        public int getId_user() {
            return id_user;
        }

        public void setId_user(int id_user) {
            this.id_user = id_user;
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

        public int getId_template() {
            return id_template;
        }

        public void setId_template(int id_template) {
            this.id_template = id_template;
        }
    }

}
