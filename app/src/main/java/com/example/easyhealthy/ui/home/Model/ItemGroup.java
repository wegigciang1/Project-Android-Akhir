package com.example.easyhealthy.ui.home.Model;

import java.util.ArrayList;

public class ItemGroup {
    private String headerTitle;
    private ArrayList <ItemData> listResep;

    public ItemGroup () {

    }

    public ItemGroup(String headerTitle, ArrayList<ItemData> listResep) {
        this.headerTitle = headerTitle;
        this.listResep = listResep;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public ArrayList<ItemData> getListResep() {
        return listResep;
    }

    public void setListResep(ArrayList<ItemData> listResep) {
        this.listResep = listResep;
    }
}
