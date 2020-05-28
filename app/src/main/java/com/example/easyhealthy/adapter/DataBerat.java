package com.example.easyhealthy.adapter;

public class DataBerat {
    private String tanggal;
    private String berat;

    public DataBerat(){

    }

    public DataBerat(String tanggal, String berat){
        this.tanggal = tanggal;
        this.berat = berat;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getBerat() {
        return berat;
    }
}
