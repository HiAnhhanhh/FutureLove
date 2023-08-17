package com.thinkdiffai.futurelove.model.event;

public class UploadingEvent {

    private String device_them_su_kien;
    private String ip_them_su_kien;
    private long id_user;
    private String ten_nam;
    private String ten_nu;

    public UploadingEvent(String device_them_su_kien, String ip_them_su_kien, int id_user, String ten_nam, String ten_nu) {
        this.device_them_su_kien = device_them_su_kien;
        this.ip_them_su_kien = ip_them_su_kien;
        this.id_user = id_user;
        this.ten_nam = ten_nam;
        this.ten_nu = ten_nu;
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

    public long getId_user() {
        return id_user;
    }

    public void setId_user(long id_user) {
        this.id_user = id_user;
    }

    public String getTen_nam() {
        return ten_nam;
    }

    public void setTen_nam(String ten_nam) {
        this.ten_nam = ten_nam;
    }

    public String getTen_nu() {
        return ten_nu;
    }

    public void setTen_nu(String ten_nu) {
        this.ten_nu = ten_nu;
    }
}
