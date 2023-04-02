/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.nonogram_online.user;

import java.util.Random;

/**
 *
 * @author marton552
 */
public class User {

    private String username;
    private String usercode;
    private int rank;

    public User(String username, String usercode,int rank) {
        this.username = username;
        this.usercode = usercode;
        this.rank = rank;
    }
    
    public String getFullUsername() {
        return username+"#"+usercode;
    }

    public String getUsername() {
        return username;
    }

    public String getUsercode() {
        return usercode;
    }

    public int getRank() {
        return rank;
    }

    public void lvlUp() {
        this.rank++;
    }

    public boolean isGuest(){
        if(usercode.startsWith("0")) return false;
        return true;
    }
    
    public static String fullCode(int code){
        String scode = code+"";
        String res = "";
        for (int i = 0; i < 4-scode.length(); i++) {
            res+="0";
        }
        return (res+scode);
    }
}
