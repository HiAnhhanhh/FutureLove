package com.thinkdiffai.futurelove.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "event_history")
public class EventHomeDto {
    @PrimaryKey(autoGenerate = true)
    private int numberOrder;

    @ColumnInfo()
    private int id;
    @ColumnInfo()
    private int id_toan_bo_su_kien;
    @ColumnInfo()
    private String link_da_swap;
    @ColumnInfo()
    private String link_nam_chua_swap;
    @ColumnInfo()
    private String link_nam_goc;
    @ColumnInfo()
    private String link_nu_chua_swap;
    @ColumnInfo()
    private String link_nu_goc;
    @ColumnInfo()
    private String noi_dung_su_kien;
    @ColumnInfo()
    private String real_time;
    @ColumnInfo()
    private float so_thu_tu_su_kien;
    @ColumnInfo()
    private String ten_su_kien;
    @ColumnInfo()
    private String tom_Luoc_Text;
    public EventHomeDto() {
    }
    public EventHomeDto(int id, int id_toan_bo_su_kien, String link_da_swap, String link_nam_chua_swap, String link_nam_goc, String link_nu_chua_swap, String link_nu_goc, String noi_dung_su_kien, String real_time, float so_thu_tu_su_kien, String ten_su_kien) {
        this.id = id;
        this.id_toan_bo_su_kien = id_toan_bo_su_kien;
        this.link_da_swap = link_da_swap;
        this.link_nam_chua_swap = link_nam_chua_swap;
        this.link_nam_goc = link_nam_goc;
        this.link_nu_chua_swap = link_nu_chua_swap;
        this.link_nu_goc = link_nu_goc;
        this.noi_dung_su_kien = noi_dung_su_kien;
        this.real_time = real_time;
        this.so_thu_tu_su_kien = so_thu_tu_su_kien;
        this.ten_su_kien = ten_su_kien;
    }
    public EventHomeDto(int id, String link_da_swap, String link_nam_chua_swap, String link_nam_goc, String link_nu_chua_swap, String link_nu_goc, String noi_dung_su_kien, String real_time, float so_thu_tu_su_kien, String ten_su_kien, String tom_Luoc_Text) {
        this.id = id;
        this.link_da_swap = link_da_swap;
        this.link_nam_chua_swap = link_nam_chua_swap;
        this.link_nam_goc = link_nam_goc;
        this.link_nu_chua_swap = link_nu_chua_swap;
        this.link_nu_goc = link_nu_goc;
        this.noi_dung_su_kien = noi_dung_su_kien;
        this.real_time = real_time;
        this.so_thu_tu_su_kien = so_thu_tu_su_kien;
        this.ten_su_kien = ten_su_kien;
        this.tom_Luoc_Text = tom_Luoc_Text;
    }
    public void setTom_Luoc_Text(String tom_Luoc_Text) {
        this.tom_Luoc_Text = tom_Luoc_Text;
    }
    public String getTom_Luoc_Text() {
        return tom_Luoc_Text;
    }

    public EventHomeDto(int numberOrder, int id, int id_toan_bo_su_kien, String link_da_swap, String link_nam_chua_swap, String link_nam_goc, String link_nu_chua_swap, String link_nu_goc, String noi_dung_su_kien, String real_time, float so_thu_tu_su_kien, String ten_su_kien) {
        this.numberOrder = numberOrder;
        this.id = id;
        this.id_toan_bo_su_kien = id_toan_bo_su_kien;
        this.link_da_swap = link_da_swap;
        this.link_nam_chua_swap = link_nam_chua_swap;
        this.link_nam_goc = link_nam_goc;
        this.link_nu_chua_swap = link_nu_chua_swap;
        this.link_nu_goc = link_nu_goc;
        this.noi_dung_su_kien = noi_dung_su_kien;
        this.real_time = real_time;
        this.so_thu_tu_su_kien = so_thu_tu_su_kien;
        this.ten_su_kien = ten_su_kien;
    }
    public int getNumberOrder() {
        return numberOrder;
    }

    public void setNumberOrder(int numberOrder) {
        this.numberOrder = numberOrder;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_toan_bo_su_kien() {
        return id_toan_bo_su_kien;
    }

    public void setId_toan_bo_su_kien(int id_toan_bo_su_kien) {
        this.id_toan_bo_su_kien = id_toan_bo_su_kien;
    }

    public String getLink_da_swap() {
        return link_da_swap;
    }

    public void setLink_da_swap(String link_da_swap) {
        this.link_da_swap = link_da_swap;
    }

    public String getLink_nam_chua_swap() {
        return link_nam_chua_swap;
    }

    public void setLink_nam_chua_swap(String link_nam_chua_swap) {
        this.link_nam_chua_swap = link_nam_chua_swap;
    }

    public String getLink_nam_goc() {
        return link_nam_goc;
    }

    public void setLink_nam_goc(String link_nam_goc) {
        this.link_nam_goc = link_nam_goc;
    }

    public String getLink_nu_chua_swap() {
        return link_nu_chua_swap;
    }

    public void setLink_nu_chua_swap(String link_nu_chua_swap) {
        this.link_nu_chua_swap = link_nu_chua_swap;
    }

    public String getLink_nu_goc() {
        return link_nu_goc;
    }

    public void setLink_nu_goc(String link_nu_goc) {
        this.link_nu_goc = link_nu_goc;
    }

    public String getNoi_dung_su_kien() {
        return noi_dung_su_kien;
    }

    public void setNoi_dung_su_kien(String noi_dung_su_kien) {
        this.noi_dung_su_kien = noi_dung_su_kien;
    }

    public String getReal_time() {
        return real_time;
    }

    public void setReal_time(String real_time) {
        this.real_time = real_time;
    }

    public float getSo_thu_tu_su_kien() {
        return so_thu_tu_su_kien;
    }

    public void setSo_thu_tu_su_kien(float so_thu_tu_su_kien) {
        this.so_thu_tu_su_kien = so_thu_tu_su_kien;
    }

    public String getTen_su_kien() {
        return ten_su_kien;
    }

    public void setTen_su_kien(String ten_su_kien) {
        this.ten_su_kien = ten_su_kien;
    }
}
