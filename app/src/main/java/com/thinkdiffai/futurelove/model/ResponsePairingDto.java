package com.thinkdiffai.futurelove.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponsePairingDto {
    @SerializedName(value = "json1",alternate = "Json1")
    private List<EventFuture> Json1;

    @SerializedName(value = "json2",alternate = "Json2")

    private List<TimeResponse> Json2;

    public List<EventFuture> getJson1() {
        return Json1;
    }

    public void setJson1(List<EventFuture> json1) {
        Json1 = json1;
    }

    public List<TimeResponse> getJson2() {
        return Json2;
    }

    public void setJson2(List<TimeResponse> json2) {
        Json2 = json2;
    }

    public class TimeResponse{
        private int id_toan_bo_su_kien;
        private String real_time;


        public TimeResponse() {
        }

        public TimeResponse(int id_toan_bo_su_kien, String real_time) {
            this.id_toan_bo_su_kien = id_toan_bo_su_kien;
            this.real_time = real_time;
        }

        public int getId_toan_bo_su_kien() {
            return id_toan_bo_su_kien;
        }

        public void setId_toan_bo_su_kien(int id_toan_bo_su_kien) {
            this.id_toan_bo_su_kien = id_toan_bo_su_kien;
        }

        public String getReal_time() {
            return real_time;
        }

        public void setReal_time(String real_time) {
            this.real_time = real_time;
        }
    }
}

