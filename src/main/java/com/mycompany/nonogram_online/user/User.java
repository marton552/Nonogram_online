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
    private String role;

    public User(String username, String usercode, int rank, String role) {
        this.username = username;
        this.usercode = usercode;
        this.rank = rank;
        this.role = role;
    }

    public String getFullUsername() {
        return username + "#" + usercode;
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

    public String getRole() {
        return role;
    }

    public boolean isAdmin() {
        return role.equals("admin");
    }

    public boolean isMod() {
        return role.equals("mod");
    }

    public void lvlUp() {
        this.rank++;
    }

    public boolean isGuest() {
        return role.equals("guest");
    }

    public static String fullCode(int code) {
        String scode = code + "";
        String res = "";
        for (int i = 0; i < 4 - scode.length(); i++) {
            res += "0";
        }
        return (res + scode);
    }
}
