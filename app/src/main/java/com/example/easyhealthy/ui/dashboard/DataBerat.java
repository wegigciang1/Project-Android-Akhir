package com.example.easyhealthy.ui.dashboard;

public class DataBerat {
    private String tanggal;
    private String berat;
    private String id;

    public DataBerat() {
    }

    public DataBerat(String tanggal, String berat, String id) {
        this.tanggal = tanggal;
        this.berat = berat;
        this.id = id;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getId() {
        return id;
    }

    public String getBerat() {
        return berat;
    }

    public void setBerat(String berat) {
        this.berat = berat;
    }

    public void setId(String id) {
        this.id = id;
    }
}
