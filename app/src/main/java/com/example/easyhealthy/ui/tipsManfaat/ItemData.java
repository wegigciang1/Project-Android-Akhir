package com.example.easyhealthy.ui.tipsManfaat;

public class ItemData {
    private String gambar;
    private String kalori;
    private String nama;
    private String detail;
    private String video;

    public ItemData() {
    }

    public ItemData(String gambar, String kalori, String nama, String detail, String video) {
        this.gambar = gambar;
        this.kalori = kalori;
        this.nama = nama;
        this.detail = detail;
        this.video = video;
    }

    public String getGambar() {
        return gambar;
    }

    public String getKalori() {
        return kalori;
    }

    public String getNama() {
        return nama;
    }

    public String getDetail() {
        return detail;
    }

    public String getVideo() {
        return video;
    }
}
