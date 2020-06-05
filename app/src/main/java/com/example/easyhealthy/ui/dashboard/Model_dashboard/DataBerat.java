package com.example.easyhealthy.ui.dashboard.Model_dashboard;

public class DataBerat {
    private String Tanggal;
    private String Berat;
    private String id;

    public DataBerat() {
    }

    public DataBerat(String tanggal, String berat, String id) {
        this.Tanggal = tanggal;
        this.Berat = berat;
        this.id = id;
    }

    public String getTanggal() {
        return Tanggal;
    }

    public String getBerat() {
        return Berat;
    }

    public String getId() {
        return id;
    }
}
