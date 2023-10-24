package com.thinkdiffai.futurelove.model.comment;

public class CommentUser {
    private String avatar_user;
    private String device_cmt;
    private String dia_chi_ip;
    private int id_comment;
    private long id_toan_bo_su_kien;
    private int id_user;
    private String imageattach;
    private String link_nam_goc;

    public String getAvatar_user() {
        return avatar_user;
    }

    public void setAvatar_user(String avatar_user) {
        this.avatar_user = avatar_user;
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

    public void setId_toan_bo_su_kien(long id_toan_bo_su_kien) {
        this.id_toan_bo_su_kien = id_toan_bo_su_kien;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getImageattach() {
        return imageattach;
    }

    public void setImageattach(String imageattach) {
        this.imageattach = imageattach;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNoi_dung_cmt() {
        return noi_dung_cmt;
    }

    public void setNoi_dung_cmt(String noi_dung_cmt) {
        this.noi_dung_cmt = noi_dung_cmt;
    }

    public int getSo_thu_tu_su_kien() {
        return so_thu_tu_su_kien;
    }

    public void setSo_thu_tu_su_kien(int so_thu_tu_su_kien) {
        this.so_thu_tu_su_kien = so_thu_tu_su_kien;
    }

    public String getThoi_gian_release() {
        return thoi_gian_release;
    }

    public void setThoi_gian_release(String thoi_gian_release) {
        this.thoi_gian_release = thoi_gian_release;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public CommentUser(String avatar_user, String device_cmt, String dia_chi_ip, int id_comment, long id_toan_bo_su_kien, int id_user, String imageattach, String link_nam_goc, String link_nu_goc, String location, String noi_dung_cmt, int so_thu_tu_su_kien, String thoi_gian_release, String user_name) {
        this.avatar_user = avatar_user;
        this.device_cmt = device_cmt;
        this.dia_chi_ip = dia_chi_ip;
        this.id_comment = id_comment;
        this.id_toan_bo_su_kien = id_toan_bo_su_kien;
        this.id_user = id_user;
        this.imageattach = imageattach;
        this.link_nam_goc = link_nam_goc;
        this.link_nu_goc = link_nu_goc;
        this.location = location;
        this.noi_dung_cmt = noi_dung_cmt;
        this.so_thu_tu_su_kien = so_thu_tu_su_kien;
        this.thoi_gian_release = thoi_gian_release;
        this.user_name = user_name;
    }

    private String link_nu_goc;
    private String location;
    private String noi_dung_cmt;
    private int so_thu_tu_su_kien;
    private String thoi_gian_release;
    private String user_name;
}
