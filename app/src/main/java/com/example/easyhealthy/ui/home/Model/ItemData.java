package com.example.easyhealthy.ui.home.Model;

public class ItemData {
    private String judul,image;

    public ItemData() {
    }

    public ItemData(String judul, String image) {
        this.judul = judul;
        this.image = image;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
