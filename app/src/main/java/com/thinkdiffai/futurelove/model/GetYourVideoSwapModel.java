package com.thinkdiffai.futurelove.model;

import com.google.gson.annotations.SerializedName;

public class GetYourVideoSwapModel {
    @SerializedName("sukien_swap_video")
    public SuKienVideoSwap suKienVideoSwap;

    public static class SuKienVideoSwap{
        @SerializedName("id_saved")
        private String id_saved;
        @SerializedName("link_video_goc")
        private String linkVidGoc;
        @SerializedName("link_image")
        private String link_image;
        @SerializedName("link_vid_da_swap")
        private String link_video_da_swap;
        @SerializedName("thoigian_sukien")
        private String thoi_gian_su_kien;
        @SerializedName("device_tao_vid")
        private String device_tao_vid;
        @SerializedName("ip_tao_vid")
        private String ip_tao_vid;
        @SerializedName("id_user")
        private int id_user;

        public SuKienVideoSwap(String id_saved, String linkVidGoc, String link_image, String link_video_da_swap, String thoi_gian_su_kien, String device_tao_vid, String ip_tao_vid, int id_user) {
            this.id_saved = id_saved;
            this.linkVidGoc = linkVidGoc;
            this.link_image = link_image;
            this.link_video_da_swap = link_video_da_swap;
            this.thoi_gian_su_kien = thoi_gian_su_kien;
            this.device_tao_vid = device_tao_vid;
            this.ip_tao_vid = ip_tao_vid;
            this.id_user = id_user;
        }

        public String getId_saved() {
            return id_saved;
        }

        public void setId_saved(String id_saved) {
            this.id_saved = id_saved;
        }

        public String getLinkVidGoc() {
            return linkVidGoc;
        }

        public void setLinkVidGoc(String linkVidGoc) {
            this.linkVidGoc = linkVidGoc;
        }

        public String getLink_image() {
            return link_image;
        }

        public void setLink_image(String link_image) {
            this.link_image = link_image;
        }

        public String getLink_video_da_swap() {
            return link_video_da_swap;
        }

        public void setLink_video_da_swap(String link_video_da_swap) {
            this.link_video_da_swap = link_video_da_swap;
        }

        public String getThoi_gian_su_kien() {
            return thoi_gian_su_kien;
        }

        public void setThoi_gian_su_kien(String thoi_gian_su_kien) {
            this.thoi_gian_su_kien = thoi_gian_su_kien;
        }

        public String getDevice_tao_vid() {
            return device_tao_vid;
        }

        public void setDevice_tao_vid(String device_tao_vid) {
            this.device_tao_vid = device_tao_vid;
        }

        public String getIp_tao_vid() {
            return ip_tao_vid;
        }

        public void setIp_tao_vid(String ip_tao_vid) {
            this.ip_tao_vid = ip_tao_vid;
        }

        public int getId_user() {
            return id_user;
        }

        public void setId_user(int id_user) {
            this.id_user = id_user;
        }
    }

}
