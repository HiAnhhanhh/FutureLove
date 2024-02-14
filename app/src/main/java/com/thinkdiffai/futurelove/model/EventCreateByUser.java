package com.thinkdiffai.futurelove.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EventCreateByUser {

    @SerializedName("list_sukien")
    private List<EventDetail> eventDetails;

    public EventCreateByUser(List<EventDetail> eventDetails) {
        this.eventDetails = eventDetails;
    }

    public List<EventDetail> getEventDetails() {
        return eventDetails;
    }

    public void setEventDetails(List<EventDetail> eventDetails) {
        this.eventDetails = eventDetails;
    }

    public static class EventDetail {
        @SerializedName("sukien")
        private List<EventDetailModel> eventDetailModels;

        public EventDetail(List<EventDetailModel> eventDetailModels) {
            this.eventDetailModels = eventDetailModels;
        }

        public List<EventDetailModel> getEventDetailModels() {
            return eventDetailModels;
        }

        public void setEventDetailModels(List<EventDetailModel> eventDetailModels) {
            this.eventDetailModels = eventDetailModels;
        }
    }

    public static class EventDetailModel {
        public String id;
        public String link_nam_goc;
        public String link_nu_goc;
        public String link_nam_chua_swap;
        public String link_nu_chua_swap;
        public String link_da_swap;
        public String real_time;
        public String ten_su_kien;
        public String noi_dung_su_kien;
        public String id_toan_bo_su_kien;
        public int so_thu_tu_su_kien;
        public int id_user;
        public int phantram_loading;
        public int count_comment;
        public int count_view;
        public String ten_nam;
        public String ten_nu;
        public int id_template;

        public EventDetailModel(String id, String link_nam_goc, String link_nu_goc, String link_nam_chua_swap, String link_nu_chua_swap, String link_da_swap, String real_time, String ten_su_kien, String noi_dung_su_kien, String id_toan_bo_su_kien, int so_thu_tu_su_kien, int id_user, int phantram_loading, int count_comment, int count_view, String ten_nam, String ten_nu, int id_template) {
            this.id = id;
            this.link_nam_goc = link_nam_goc;
            this.link_nu_goc = link_nu_goc;
            this.link_nam_chua_swap = link_nam_chua_swap;
            this.link_nu_chua_swap = link_nu_chua_swap;
            this.link_da_swap = link_da_swap;
            this.real_time = real_time;
            this.ten_su_kien = ten_su_kien;
            this.noi_dung_su_kien = noi_dung_su_kien;
            this.id_toan_bo_su_kien = id_toan_bo_su_kien;
            this.so_thu_tu_su_kien = so_thu_tu_su_kien;
            this.id_user = id_user;
            this.phantram_loading = phantram_loading;
            this.count_comment = count_comment;
            this.count_view = count_view;
            this.ten_nam = ten_nam;
            this.ten_nu = ten_nu;
            this.id_template = id_template;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getLink_nam_chua_swap() {
            return link_nam_chua_swap;
        }

        public void setLink_nam_chua_swap(String link_nam_chua_swap) {
            this.link_nam_chua_swap = link_nam_chua_swap;
        }

        public String getLink_nu_chua_swap() {
            return link_nu_chua_swap;
        }

        public void setLink_nu_chua_swap(String link_nu_chua_swap) {
            this.link_nu_chua_swap = link_nu_chua_swap;
        }

        public String getLink_da_swap() {
            return link_da_swap;
        }

        public void setLink_da_swap(String link_da_swap) {
            this.link_da_swap = link_da_swap;
        }

        public String getReal_time() {
            return real_time;
        }

        public void setReal_time(String real_time) {
            this.real_time = real_time;
        }

        public String getTen_su_kien() {
            return ten_su_kien;
        }

        public void setTen_su_kien(String ten_su_kien) {
            this.ten_su_kien = ten_su_kien;
        }

        public String getNoi_dung_su_kien() {
            return noi_dung_su_kien;
        }

        public void setNoi_dung_su_kien(String noi_dung_su_kien) {
            this.noi_dung_su_kien = noi_dung_su_kien;
        }

        public String getId_toan_bo_su_kien() {
            return id_toan_bo_su_kien;
        }

        public void setId_toan_bo_su_kien(String id_toan_bo_su_kien) {
            this.id_toan_bo_su_kien = id_toan_bo_su_kien;
        }

        public int getSo_thu_tu_su_kien() {
            return so_thu_tu_su_kien;
        }

        public void setSo_thu_tu_su_kien(int so_thu_tu_su_kien) {
            this.so_thu_tu_su_kien = so_thu_tu_su_kien;
        }

        public int getId_user() {
            return id_user;
        }

        public void setId_user(int id_user) {
            this.id_user = id_user;
        }

        public int getPhantram_loading() {
            return phantram_loading;
        }

        public void setPhantram_loading(int phantram_loading) {
            this.phantram_loading = phantram_loading;
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

        public int getId_template() {
            return id_template;
        }

        public void setId_template(int id_template) {
            this.id_template = id_template;
        }
    }
}
