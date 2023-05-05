package com.nonogram_online.server;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author marton552
 */
public class SearchResponse {
    private String search;
    private boolean levelName = false;
    private boolean userName = false;

    public SearchResponse(String search) {
        this.search = search;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public boolean isLevelName() {
        return levelName;
    }

    public void setLevelName() {
        this.levelName = !levelName;
    }

    public boolean isUserName() {
        return userName;
    }

    public void setUserName() {
        this.userName = !userName;
    }

    @Override
    public String toString() {
        String res = "";
        if(levelName || userName) res = "where ";
        if(levelName) res+="levels.level_name LIKE '%"+search+"%'";
        if(userName && levelName) res+=" and ";
        if(userName) res+="levels.creator_name LIKE '%"+search+"%'";
        return res;
    }
    
}
