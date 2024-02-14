package com.thinkdiffai.futurelove.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class GenBabyModel implements Serializable{

    @SerializedName("sukien_baby")
    private List<DetailGenBabyModel> genBabyModel;

    public GenBabyModel(List<DetailGenBabyModel> genBabyModel) {
        this.genBabyModel = genBabyModel;
    }

    public List<DetailGenBabyModel> getGenBabyModel() {
        return genBabyModel;
    }

    public void setGenBabyModel(List<DetailGenBabyModel> genBabyModel) {
        this.genBabyModel = genBabyModel;
    }

    public static class DetailGenBabyModel {

        @SerializedName("id")
        private String id_image;

        @SerializedName("thongtin")
        private String thong_tin;

        @SerializedName("tomLuocText")
        private String tom_luoc_text;

        @SerializedName("link_nam_goc")
        private String link_nam_goc;

        @SerializedName("link_nu_goc")
        private String link_nu_goc;

        @SerializedName("link_baby_goc")
        private String link_baby_goc;

        @SerializedName("link_da_swap")
        private String link_da_swap;

        @SerializedName("nguoi_swap")
        private String nguoi_swap;

        @SerializedName("id_toan_bo_su_kien")
        private String id_toan_bo_su_kien;

        public DetailGenBabyModel(String id_image, String thong_tin, String tom_luoc_text, String link_nam_goc, String link_nu_goc, String link_baby_goc, String link_da_swap, String nguoi_swap, String id_toan_bo_su_kien) {
            this.id_image = id_image;
            this.thong_tin = thong_tin;
            this.tom_luoc_text = tom_luoc_text;
            this.link_nam_goc = link_nam_goc;
            this.link_nu_goc = link_nu_goc;
            this.link_baby_goc = link_baby_goc;
            this.link_da_swap = link_da_swap;
            this.nguoi_swap = nguoi_swap;
            this.id_toan_bo_su_kien = id_toan_bo_su_kien;
        }

        public String getId_image() {
            return id_image;
        }

        public void setId_image(String id_image) {
            this.id_image = id_image;
        }

        public String getThong_tin() {
            return thong_tin;
        }

        public void setThong_tin(String thong_tin) {
            this.thong_tin = thong_tin;
        }

        public String getTom_luoc_text() {
            return tom_luoc_text;
        }

        public void setTom_luoc_text(String tom_luoc_text) {
            this.tom_luoc_text = tom_luoc_text;
        }

        public String getLink_nam_goc() {
            return link_nam_goc;
        }

        public void setLink_nam_goc(String link_nam_goc) {
            this.link_nam_goc = link_nam_goc;
        }

        public String getLink_nu_goc() {
            return link_nu_goc;
        }

        public void setLink_nu_goc(String link_nu_goc) {
            this.link_nu_goc = link_nu_goc;
        }

        public String getLink_baby_goc() {
            return link_baby_goc;
        }

        public void setLink_baby_goc(String link_baby_goc) {
            this.link_baby_goc = link_baby_goc;
        }

        public String getLink_da_swap() {
            return link_da_swap;
        }

        public void setLink_da_swap(String link_da_swap) {
            this.link_da_swap = link_da_swap;
        }

        public String getNguoi_swap() {
            return nguoi_swap;
        }

        public void setNguoi_swap(String nguoi_swap) {
            this.nguoi_swap = nguoi_swap;
        }

        public String getId_toan_bo_su_kien() {
            return id_toan_bo_su_kien;
        }

        public void setId_toan_bo_su_kien(String id_toan_bo_su_kien) {
            this.id_toan_bo_su_kien = id_toan_bo_su_kien;
        }
    }
}
