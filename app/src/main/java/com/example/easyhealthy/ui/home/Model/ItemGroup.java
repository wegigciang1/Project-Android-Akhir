package com.example.easyhealthy.ui.home.Model;

import java.util.ArrayList;

public class ItemGroup {
    public String headerTitle;
    public String categoryId;
    //private ArrayList <ItemData> listResep;

    public ItemGroup () {

    }

    public ItemGroup(String headerTitle, ArrayList<ItemData> listResep) {
        this.headerTitle = headerTitle;
        //this.listResep = listResep;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

//    public ArrayList<ItemData> getListResep() {
//        return listResep;
//    }

//    public void setListResep(ArrayList<ItemData> listResep) {
//        this.listResep = listResep;
//    }
}
