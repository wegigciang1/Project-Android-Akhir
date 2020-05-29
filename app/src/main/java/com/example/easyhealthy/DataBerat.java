package com.example.easyhealthy;

public class DataBerat {
    private String tanggal;
    private int berat;
    private String id;

    public DataBerat(){

    }

    public DataBerat(String tanggal, int berat, String id) {
        this.tanggal = tanggal;
        this.berat = berat;
        this.id = id;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getId() {
        return id;
    }

    public int getBerat() {
        return berat;
    }
}
