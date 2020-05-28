package com.example.easyhealthy.interfaces;


import com.example.easyhealthy.ui.tipsManfaat.Model.ItemGroup;

import java.util.List;

public interface FirebaseLoadListenerInterface {
    void onFirebaseLoadSucces(List<ItemGroup> itemGroupList);
    void onFirebaseLoadFailed(String message);
}
