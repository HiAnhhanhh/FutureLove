package com.thinkdiffai.futurelove.model;

import com.google.gson.annotations.SerializedName;

public class GetVideoSwapResponse {
    @SerializedName("sukien_video")
    public SukienVideo sukien_video;

    public static class SukienVideo {
        @SerializedName("id_sukien_video")
        public String id_sukien_video;

        @SerializedName("id_video_swap")
        public String id_video_swap;

        @SerializedName("linkimg")
        public String linkimg;

        @SerializedName("link_vid_swap")
        public String link_vid_swap;

        @SerializedName("link_vid_goc")
        public String link_vid_goc;

        @SerializedName("ten_video")
        public String ten_video;

        @SerializedName("noidung")
        public String noidung;

        @SerializedName("thoigian_sukien")
        public String thoigian_sukien;

        @SerializedName("thoigian_swap")
        public double thoigian_swap;

        @SerializedName("device_tao_vid")
        public String device_tao_vid;

        @SerializedName("ip_tao_vid")
        public String ip_tao_vid;
    }
}