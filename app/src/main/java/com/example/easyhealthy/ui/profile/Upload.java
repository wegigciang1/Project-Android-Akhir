package com.example.easyhealthy.ui.profile;

public class Upload {
    private String nName;
    private String nImageUrl;

    public Upload() {
        //empty constructor
    }

    public Upload(String name, String imageUrl) {
        if (name.trim().equals("")) {
            name = "No Name";
        }
        this.nName = name;
        this.nImageUrl = imageUrl;
    }

    public String getnName() {
        return nName;
    }

    public void setnName(String nName) {
        this.nName = nName;
    }

    public String getnImageUrl() {
        return nImageUrl;
    }

    public void setnImageUrl(String nImageUrl) {
        this.nImageUrl = nImageUrl;
    }
}
