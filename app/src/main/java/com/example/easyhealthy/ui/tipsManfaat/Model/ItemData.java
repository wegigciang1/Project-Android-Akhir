package com.example.easyhealthy.ui.tipsManfaat.Model;

public class ItemData {
    private String judul,image,detail,video;

    public ItemData() {
    }

    public ItemData(String judul, String image, String detail, String video) {
        this.judul = judul;
        this.image = image;
        this.detail = detail;
        this.video = video;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
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
