/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.nonogram_online.user;

/**
 *
 * @author marton552
 */
public class MissionIcon {
    private String title;
    private String icon;
    private int needCount;
    private int currentCount;

    /*
    Küldetés fajták:
    - teljesíts X offline pályát //offlineX
    - teljesíts X online pályát //onlineX
    - készíts X pályát //makeX
    - Játszanak legalább X pályáddal //getplayedX
    - Kapj legalább X értékelést Y minimum rate-el //getratedX
    - ect
    */
    public MissionIcon(String title, String icon, int needCount, int currentCount) {
        this.title = title;
        this.icon = icon;
        this.needCount = needCount;
        this.currentCount = currentCount;
    }

    public String getTitle() {
        return title;
    }

    public String getIcon() {
        return icon;
    }

    public int getNeedCount() {
        return needCount;
    }

    public int getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
    }
    
}
