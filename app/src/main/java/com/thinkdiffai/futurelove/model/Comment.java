package com.thinkdiffai.futurelove.model;


public class Comment {
    public String device_cmt;
    public String dia_chi_ip;
    public int id_comment;
    public long id_toan_bo_su_kien;
    public String imageattach;
    public String noi_dung_cmt;
    public String thoi_gian_release;
    private String link_nam_goc;
    private String link_nu_goc;

    public void setId_toan_bo_su_kien(long id_toan_bo_su_kien) {
        this.id_toan_bo_su_kien = id_toan_bo_su_kien;
    }

    public Comment(String device_cmt, String dia_chi_ip, int id_comment, long id_toan_bo_su_kien, String imageattach, String noi_dung_cmt, String thoi_gian_release, String link_nam_goc, String link_nu_goc) {
        this.device_cmt = device_cmt;
        this.dia_chi_ip = dia_chi_ip;
        this.id_comment = id_comment;
        this.id_toan_bo_su_kien = id_toan_bo_su_kien;
        this.imageattach = imageattach;
        this.noi_dung_cmt = noi_dung_cmt;
        this.thoi_gian_release = thoi_gian_release;
        this.link_nam_goc = link_nam_goc;
        this.link_nu_goc = link_nu_goc;
    }

    public Comment(String device_cmt, String dia_chi_ip, int id_comment, long id_toan_bo_su_kien, String imageattach, String noi_dung_cmt, String thoi_gian_release) {
        this.device_cmt = device_cmt;
        this.dia_chi_ip = dia_chi_ip;
        this.id_comment = id_comment;
        this.id_toan_bo_su_kien = id_toan_bo_su_kien;
        this.imageattach = imageattach;
        this.noi_dung_cmt = noi_dung_cmt;
        this.thoi_gian_release = thoi_gian_release;
    }
    public Comment(String device_cmt, String dia_chi_ip, int id_comment, long id_toan_bo_su_kien, String imageattach, String noi_dung_cmt) {
        this.device_cmt = device_cmt;
        this.dia_chi_ip = dia_chi_ip; // ip cmt
        this.id_comment = id_comment; // x
        this.id_toan_bo_su_kien = id_toan_bo_su_kien; //
        this.imageattach = imageattach;
        this.noi_dung_cmt = noi_dung_cmt;
    }

    public Comment() {
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

    public String getThoi_gian_release() {
        return thoi_gian_release;
    }

    public void setThoi_gian_release(String thoi_gian_release) {
        this.thoi_gian_release = thoi_gian_release;
    }



    public String getDevice_cmt() {
        return device_cmt;
    }

    public void setDevice_cmt(String device_cmt) {
        this.device_cmt = device_cmt;
    }

    public String getDia_chi_ip() {
        return dia_chi_ip;
    }

    public void setDia_chi_ip(String dia_chi_ip) {
        this.dia_chi_ip = dia_chi_ip;
    }

    public int getId_comment() {
        return id_comment;
    }

    public void setId_comment(int id_comment) {
        this.id_comment = id_comment;
    }

    public long getId_toan_bo_su_kien() {
        return id_toan_bo_su_kien;
    }

    public void setId_toan_bo_su_kien(int id_toan_bo_su_kien) {
        this.id_toan_bo_su_kien = id_toan_bo_su_kien;
    }

    public String getImageattach() {
        return imageattach;
    }

    public void setImageattach(String imageattach) {
        this.imageattach = imageattach;
    }

    public String getNoi_dung_cmt() {
        return noi_dung_cmt;
    }

    public void setNoi_dung_cmt(String noi_dung_cmt) {
        this.noi_dung_cmt = noi_dung_cmt;
    }
}
