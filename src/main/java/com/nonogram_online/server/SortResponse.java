/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nonogram_online.server;

import java.util.Arrays;

/**
 *
 * @author marton552
 */
public class SortResponse {

    private int id = 4;
    private int date = 2;
    private int name = 3;
    private int rate = 1;
    private int orderNum = 4;
    private String dateAsc = "";
    private String nameAsc = "";
    private String rateAsc = "";

    public SortResponse() {
    }

    public int getId() {
        return id;
    }

    public void setId() {
        orderNum++;
        this.id = orderNum;
    }

    public int getDate() {
        return date;
    }

    public void setDate() {
        orderNum++;
        this.date = orderNum;
        if (dateAsc == "") {
            dateAsc = "ASC";
        } else if (dateAsc == "ASC") {
            dateAsc = "DESC";
        } else {
            dateAsc = "";
        }
    }

    public int getName() {
        return name;
    }

    public void setName() {
        orderNum++;
        this.name = orderNum;
        if (nameAsc == "") {
            nameAsc = "ASC";
        } else if (nameAsc == "ASC") {
            nameAsc = "DESC";
        } else {
            nameAsc = "";
        }
    }

    public int getRate() {
        return rate;
    }

    public void setRate() {
        orderNum++;
        this.rate = orderNum;
        if (rateAsc == "") {
            rateAsc = "ASC";
        } else if (rateAsc == "ASC") {
            rateAsc = "DESC";
        } else {
            rateAsc = "";
        }
    }

    @Override
    public String toString() {
        int[] order = {id, name, date, rate};
        Arrays.sort(order);
        for (int i = 0; i < order.length / 2; ++i) {
            int temp = order[i];
            order[i] = order[order.length - i - 1];
            order[order.length - i - 1] = temp;
        }
        String res = "";
        for (int i = 0; i < 4; i++) {
            if (order[i] == id) {
                res += " levels.id ASC";
            } else if (order[i] == date) {
                res += " levels.created_date " + dateAsc;
            } else if (order[i] == name) {
                res += " levels.level_name " + nameAsc;
            } else if (order[i] == rate) {
                res += " rate " + rateAsc;
            }
            if (i < 3) {
                res += ",";
            }
        }
        return res;
    }
}
