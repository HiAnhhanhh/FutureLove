package com.thinkdiffai.futurelove.model.comment;

import com.google.gson.annotations.SerializedName;

import retrofit2.http.Field;

public class Comment {
    @SerializedName("avatar_user")
    private String avatarUser;
    @SerializedName("device_cmt")
    private String deviceCmt;
    @SerializedName("dia_chi_ip")
    private String diaChiIp;
    @SerializedName("id_comment")
    private int idComment;
    @SerializedName("id_toan_bo_su_kien")
    private int idToanBoSuKien;
    @SerializedName("id_user")
    private int idUser;
    @SerializedName("imageattach")
    private String imageattach;
    @SerializedName("link_nam_goc")
    private String linkNamGoc;
    @SerializedName("link_nu_goc")
    private String linkNuGoc;
    @SerializedName("noi_dung_cmt")
    private String noiDungCmt;
    @SerializedName("so_thu_tu_su_kien")
    private int soThuTuSuKien;
    @SerializedName("thoi_gian_release")
    private String thoiGianRelease;
    @SerializedName("user_name")
    private String userName;

    public Comment(String avatarUser, String deviceCmt, String diaChiIp, int idComment, int idToanBoSuKien, int idUser, String imageattach, String linkNamGoc, String linkNuGoc, String noiDungCmt, int soThuTuSuKien, String thoiGianRelease, String userName) {
        this.avatarUser = avatarUser;
        this.deviceCmt = deviceCmt;
        this.diaChiIp = diaChiIp;
        this.idComment = idComment;
        this.idToanBoSuKien = idToanBoSuKien;
        this.idUser = idUser;
        this.imageattach = imageattach;
        this.linkNamGoc = linkNamGoc;
        this.linkNuGoc = linkNuGoc;
        this.noiDungCmt = noiDungCmt;
        this.soThuTuSuKien = soThuTuSuKien;
        this.thoiGianRelease = thoiGianRelease;
        this.userName = userName;
    }

    public Comment(int idUser, String content, String device, int idSummary, int soThuTuSuKien, String diaChiIp, String imageAttach) {
        this.deviceCmt = device;
        this.diaChiIp = diaChiIp;
        this.idToanBoSuKien = idSummary;
        this.idUser = idUser;
        this.imageattach = imageAttach;
        this.noiDungCmt = content;
        this.soThuTuSuKien = soThuTuSuKien;
    }

    public String getAvatarUser() {
        return avatarUser;
    }

    public void setAvatarUser(String avatarUser) {
        this.avatarUser = avatarUser;
    }

    public String getDeviceCmt() {
        return deviceCmt;
    }

    public void setDeviceCmt(String deviceCmt) {
        this.deviceCmt = deviceCmt;
    }

    public String getDiaChiIp() {
        return diaChiIp;
    }

    public void setDiaChiIp(String diaChiIp) {
        this.diaChiIp = diaChiIp;
    }

    public int getIdComment() {
        return idComment;
    }

    public void setIdComment(int idComment) {
        this.idComment = idComment;
    }

    public int getIdToanBoSuKien() {
        return idToanBoSuKien;
    }

    public void setIdToanBoSuKien(int idToanBoSuKien) {
        this.idToanBoSuKien = idToanBoSuKien;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getImageattach() {
        return imageattach;
    }

    public void setImageattach(String imageattach) {
        this.imageattach = imageattach;
    }

    public String getLinkNamGoc() {
        return linkNamGoc;
    }

    public void setLinkNamGoc(String linkNamGoc) {
        this.linkNamGoc = linkNamGoc;
    }

    public String getLinkNuGoc() {
        return linkNuGoc;
    }

    public void setLinkNuGoc(String linkNuGoc) {
        this.linkNuGoc = linkNuGoc;
    }

    public String getNoiDungCmt() {
        return noiDungCmt;
    }

    public void setNoiDungCmt(String noiDungCmt) {
        this.noiDungCmt = noiDungCmt;
    }

    public int getSoThuTuSuKien() {
        return soThuTuSuKien;
    }

    public void setSoThuTuSuKien(int soThuTuSuKien) {
        this.soThuTuSuKien = soThuTuSuKien;
    }

    public String getThoiGianRelease() {
        return thoiGianRelease;
    }

    public void setThoiGianRelease(String thoiGianRelease) {
        this.thoiGianRelease = thoiGianRelease;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
